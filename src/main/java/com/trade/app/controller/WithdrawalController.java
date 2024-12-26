package com.trade.app.controller;

import com.trade.app.entity.User;
import com.trade.app.entity.Wallet;
import com.trade.app.entity.WalletTransaction;
import com.trade.app.entity.Withdrawal;
import com.trade.app.service.UserService;
import com.trade.app.service.WalletService;
import com.trade.app.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class WithdrawalController {

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WalletService walletService;




    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<Withdrawal> requestWithdrawal(@RequestHeader("Authorization") String jwt, @PathVariable Long amount) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalance(wallet,-amount);

        return ResponseEntity.ok(withdrawal);

    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accepted}")
    public ResponseEntity<Withdrawal> proceedWithdrawal(@PathVariable Long id, @PathVariable boolean accepted,
                                                        @RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal = withdrawalService.proceedWithdrawal(id,accepted);
        Wallet wallet = walletService.getUserWallet(user);
        if(!accepted){
            walletService.addBalance(wallet, withdrawal.getAmount());
        }
        return ResponseEntity.ok(withdrawal);
    }

    @GetMapping("api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawals = withdrawalService.getUserWithdrawalHistory(user);
        return ResponseEntity.ok(withdrawals);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawal(@RequestHeader("Authorization")String jwt){
        List<Withdrawal> withdrawals = withdrawalService.getAllWithdrawalRequests();
        return ResponseEntity.ok(withdrawals);
    }
}
