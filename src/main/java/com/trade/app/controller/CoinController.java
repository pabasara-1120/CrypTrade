package com.trade.app.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.app.entity.Coin;
import com.trade.app.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    ResponseEntity<List<Coin>> getCoinList(@RequestParam(required = false, name = "page") int page){
        List<Coin> coinList = coinService.getCoinList(page);
        return ResponseEntity.ok(coinList);

    }

    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId, @RequestParam("days") int days) throws Exception {
        String res = coinService.getMarketChart(coinId,days);
        JsonNode jsonNode = objectMapper.readTree(res);
        return ResponseEntity.ok(jsonNode);

    }

    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
        String coin = coinService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }
    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
        String coin = coinService.getTop50Coins();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }
    @GetMapping("/trading")
    ResponseEntity<JsonNode> getTradingCoins() throws Exception {
        String coins = coinService.getTradingCoins();
        JsonNode jsonNode = objectMapper.readTree(coins);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String details = coinService.coinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(details);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/get-coin/{coinId}")
    ResponseEntity<Coin> getCoinById(@PathVariable String coinId) throws Exception {
        Coin coin = coinService.findById(coinId);
        return ResponseEntity.ok(coin);
    }


}
