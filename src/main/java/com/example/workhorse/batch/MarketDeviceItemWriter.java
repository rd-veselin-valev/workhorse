package com.example.workhorse.batch;

import com.example.workhorse.data.entity.Device;
import com.example.workhorse.data.entity.Market;
import com.example.workhorse.data.repository.DeviceRepository;
import com.example.workhorse.data.repository.MarketRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class MarketDeviceItemWriter implements ItemWriter<Market> {
    private final MarketRepository marketRepository;
    private final DeviceRepository deviceRepository;

    private final List<Device> allDevices = new ArrayList<>();
    private final AtomicInteger deviceIndex = new AtomicInteger(0);
    private boolean remainingDevicesCompleted;

    @PostConstruct
    public void loadDevices() {
        allDevices.addAll(deviceRepository.findAll());
        System.out.printf("Loaded %d devices.\n", allDevices.size());
        marketRepository.deleteAll();
    }

    @Override
    public void write(Chunk<? extends Market> markets) throws Exception {
        try {
            System.out.println("Writing chunk of size: " + markets.getItems().size());
            var savedMarkets = marketRepository.saveAll(markets.getItems().stream()
                    .map(m -> (Market) m)
                    .collect(Collectors.toList()));
            System.out.println("Saved " + savedMarkets.size() + " markets.");

            var updatedDevices = new ArrayList<Device>();
            if (!remainingDevicesCompleted) {
                for (var i = 0; i < 400; i++) {
                    if (deviceIndex.get() < allDevices.size()) {
                        Device device = allDevices.get(deviceIndex.getAndIncrement());
                        device.setMarket(savedMarkets.get(i));
                        updatedDevices.add(device);
                    }
                }
                remainingDevicesCompleted = true;
            }
            for (Market market : savedMarkets) {
                for (int i = 0; i < 2; i++) {
                    if (deviceIndex.get() < allDevices.size()) {
                        Device device = allDevices.get(deviceIndex.getAndIncrement());
                        device.setMarket(market);
                        updatedDevices.add(device);
                    }
                }
            }
            System.out.println("Updating " + updatedDevices.size() + " devices.");
            deviceRepository.saveAll(updatedDevices);
            System.out.println("Devices saved successfully.");
        } catch (Exception e) {
            System.out.printf("Error while writing records: %s\n", e.getMessage());
            throw e;
        }
    }
}

