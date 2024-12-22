package com.trade.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trade.app.entity.Coin;
import com.trade.app.repository.CoinRepository;
import com.trade.app.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CoinServiceImpl implements CoinService {

    private ObjectMapper objectMapper;

    @Autowired
    private CoinRepository coinRepository;

    @Override
    public List<Coin> getCoinList(int page) {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10page="+page;
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters",httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Coin>>() {
            });
         } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId+ "markets_chart?vs_currency=usd&days="+days;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public String coinDetails(String coinId) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId;
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            Coin coin = new Coin();
            coin.setId(jsonNode.get("id").asText());
            coin.setName(jsonNode.get("name").asText());
            coin.setSymbol(jsonNode.get("symbol").asText());
            coin.setImage(jsonNode.get("image").get("large").asText() );
            JsonNode marketData = jsonNode.get("market_data");
            coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
            coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            coin.setTotalVolume(marketData.get("total_volume").get("usd").asInt());
            coin.setHigh24h(marketData.get("high_24").get("usd").asDouble());
            coin.setLow24h(marketData.get("low_24").get("usd").asDouble());
            coin.setPriceChange24h(marketData.get("price_change_24").get("usd").asDouble());
            coin.setPriceChangePercentage24h(marketData.get("market_cap_change_percentage_24").get("usd").asDouble());
            coin.setTotalSupply(marketData.get("total_supply").get("usd").asLong());

            coinRepository.save(coin);

            return response.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String searchCoin(String keyword) throws Exception {
        try {
            String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public String getTop50Coins() throws Exception {
        try {
            String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10page=1";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTradingCoins() throws Exception {
        try {
            String url = "https://api.coingecko.com/api/v3/search/trading";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Coin findById(String id) throws Exception {
        Optional<Coin> optionalCoin = coinRepository.findById(id);
        if(optionalCoin.isPresent()){
            return optionalCoin.get();
        }
        throw new Exception("Coin not found");
    }

}
