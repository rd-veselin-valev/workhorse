package com.example.workhorse.batch;

import com.example.workhorse.data.entity.Device;
import com.example.workhorse.data.entity.Market;
import com.example.workhorse.data.repository.DeviceRepository;
import com.example.workhorse.data.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class MarketDeviceItemWriter implements ItemWriter<Market> {
    private final MarketRepository marketRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public void write(Chunk<? extends Market> markets) throws Exception {
        List<Market> savedMarkets = marketRepository.saveAll(markets.getItems().stream()
                .map(m -> (Market) m)
                .collect(Collectors.toList()));

        for (Market savedMarket : savedMarkets) {
            List<Device> devices = deviceRepository.findAll();
            for (Device device : devices) {
                device.setMarket(savedMarket);
            }

            deviceRepository.saveAll(devices);
        }
    }
}

