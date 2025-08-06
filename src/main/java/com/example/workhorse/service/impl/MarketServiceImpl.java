package com.example.workhorse.service.impl;

import com.example.workhorse.data.entity.Device;
import com.example.workhorse.data.entity.Market;
import com.example.workhorse.data.repository.MarketRepository;
import com.example.workhorse.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {
    private final MarketRepository marketRepository;

    @Override
    public List<Market> getMarketsFromSource() {
        return List.of();
    }

    @Override
    public List<Device> getDevicesFromTarget() {
        return List.of();
    }

    @Override
    public Map<Market, List<Device>> assignDevicesToMarkets(List<Market> marketsFromSource, List<Device> devicesFromTarget) {
        return Map.of();
    }

    @Override
    public void insertMarketsIntoTarget(ArrayList<Market> markets) {

    }

    @Override
    public void updateDevicesWithMarketIds(Map<Market, List<Device>> assignment) {

    }
}
