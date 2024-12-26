package com.trade.app.service;

import com.trade.app.entity.Coin;
import com.trade.app.entity.User;
import com.trade.app.entity.WatchList;

public interface WatchListService {

    WatchList findUserWatchList(Long userId) throws Exception;
    WatchList createWatchList(User user);
    WatchList findById(Long id) throws Exception;
    Coin addItemToWatchList(Coin coin, User user) throws Exception;


}
