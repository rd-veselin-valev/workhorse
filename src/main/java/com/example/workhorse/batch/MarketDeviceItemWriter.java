package com.example.workhorse.batch;

import com.example.workhorse.data.entity.Device;
import com.example.workhorse.data.entity.Market;
import com.example.workhorse.data.repository.DeviceRepository;
import com.example.workhorse.data.repository.MarketRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
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
        marketRepository.deleteAll();
    }

    @Override
    public void write(Chunk<? extends Market> markets) throws Exception {
        try {
            log.info("Writing chunk of size: {}", markets.getItems().size());
            var savedMarkets = marketRepository.saveAll(markets.getItems().stream()
                    .map(m -> (Market) m)
                    .collect(Collectors.toList()));

            var updatedDevices = new ArrayList<Device>();
            if (!remainingDevicesCompleted) {
                for (var i = 0; i < 400; i++) {
                    assignDeviceToMarket(savedMarkets.get(i), updatedDevices);
                }
                remainingDevicesCompleted = true;
            }
            for (Market market : savedMarkets) {
                for (int i = 0; i < 2; i++) {
                    assignDeviceToMarket(market, updatedDevices);
                }
            }
            deviceRepository.saveAll(updatedDevices);
        } catch (Exception e) {
            log.info("Error while writing records: {}\n", e.getMessage());
            throw e;
        }
    }

    private void assignDeviceToMarket(Market savedMarkets, ArrayList<Device> updatedDevices) {
        if (deviceIndex.get() < allDevices.size()) {
            Device device = allDevices.get(deviceIndex.getAndIncrement());
            device.setMarket(savedMarkets);
            updatedDevices.add(device);
        }
    }
}

