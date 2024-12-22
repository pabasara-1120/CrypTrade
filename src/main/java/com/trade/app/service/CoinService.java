package com.trade.app.service;


import com.trade.app.entity.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page);
    String getMarketChart(String coinId, int days) throws Exception;
    String coinDetails(String coinID) throws Exception;
    String searchCoin(String keyword) throws Exception;
    String getTop50Coins() throws Exception;
    String getTradingCoins() throws Exception;
    Coin findById(String id) throws Exception;
}
