package com.trade.app.service.impl;

import com.trade.app.entity.Asset;
import com.trade.app.entity.Coin;
import com.trade.app.entity.User;
import com.trade.app.repository.AssetRepository;
import com.trade.app.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;


    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = Asset.builder()
                .coin(coin)
                .quantity(quantity)
                .user(user)
                .buyingPrice(coin.getCurrentPrice())
                .build();

        return assetRepository.save(asset);

    }

    @Override
    public Asset findAssetById(Long assetId) throws Exception {
        Optional<Asset> asset = assetRepository.findById(assetId);
        if(asset.isPresent()){
            return asset.get();
        }
        else {
            throw new Exception("Asset not found");
        }
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return assetRepository.findByUserIdAndId(userId, assetId);
    }

    @Override
    public List<Asset> getUserAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {
        Optional<Asset> currentAsset = assetRepository.findById(assetId);
        if(currentAsset.isPresent()){
            Asset asset = currentAsset.get();
            asset.setQuantity(quantity+asset.getQuantity());
            return assetRepository.save(asset);
        }
        else {
            throw new Exception("Asset not found");
        }

    }

    @Override
    public Asset findAssetByUserAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);

    }
}
