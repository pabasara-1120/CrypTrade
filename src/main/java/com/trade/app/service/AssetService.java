package com.trade.app.service;

import com.trade.app.entity.Asset;
import com.trade.app.entity.Coin;
import com.trade.app.entity.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity);
    Asset findAssetById(Long assetId) throws Exception;
    Asset getAssetByUserIdAndId(Long userId,Long assetId);
    List<Asset> getUserAssets(Long userId);
    Asset  updateAsset(Long assetId, double quantity) throws Exception;
    Asset findAssetByUserAndCoinId(Long userId,String coinId);
    void deleteAsset(Long assetId);
}
