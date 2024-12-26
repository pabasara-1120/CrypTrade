package com.trade.app.service;

import com.trade.app.entity.User;
import com.trade.app.entity.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount, User user);
    Withdrawal proceedWithdrawal(Long withdrawalId, boolean isAccepted) throws Exception;
    List<Withdrawal>  getUserWithdrawalHistory(User user);
    List<Withdrawal> getAllWithdrawalRequests();
}
