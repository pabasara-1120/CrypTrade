package com.trade.app.controller;

import com.trade.app.entity.Coin;
import com.trade.app.entity.User;
import com.trade.app.entity.WatchList;
import com.trade.app.service.CoinService;
import com.trade.app.service.UserService;
import com.trade.app.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.findUserWatchList(user.getId());
        return ResponseEntity.ok(watchList);
    }

    @PostMapping("/create")
    public ResponseEntity<WatchList> createWatchList(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.createWatchList(user);
        return ResponseEntity.ok(watchList);

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<WatchList> getWatchList(@PathVariable Long id) throws Exception {
        WatchList watchList = watchListService.findById(id);
        return ResponseEntity.ok(watchList);

    }
    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addToWatchList(@RequestHeader("Authorization")String jwt, @PathVariable String coinId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(coinId);
        Coin coin1 = watchListService.addItemToWatchList(coin,user);
        return ResponseEntity.ok(coin1);

    }
}
