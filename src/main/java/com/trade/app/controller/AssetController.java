package com.trade.app.controller;

import com.trade.app.entity.Asset;
import com.trade.app.entity.User;
import com.trade.app.repository.AssetRepository;
import com.trade.app.service.AssetService;
import com.trade.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset = assetService.findAssetById(assetId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/coin/{coin}")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@PathVariable String coinId,
                                                           @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Asset asset = assetService.findAssetByUserAndCoinId(user.getId(),coinId);
        return ResponseEntity.ok(asset);
    }
    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetByUser(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Asset> assets = assetService.getUserAssets(user.getId());
        return ResponseEntity.ok(assets);
    }
}
