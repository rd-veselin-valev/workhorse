package com.example.workhorse.service;

import com.example.workhorse.data.entity.Device;
import com.example.workhorse.data.entity.Market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface MarketService {
    List<Market> getMarketsFromSource();

    List<Device> getDevicesFromTarget();

    Map<Market, List<Device>> assignDevicesToMarkets(List<Market> marketsFromSource, List<Device> devicesFromTarget);

    void insertMarketsIntoTarget(ArrayList<Market> markets);

    void updateDevicesWithMarketIds(Map<Market, List<Device>> assignment);
}
