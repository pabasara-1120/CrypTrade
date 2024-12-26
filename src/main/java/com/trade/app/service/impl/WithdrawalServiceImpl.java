package com.trade.app.service.impl;

import com.trade.app.entity.User;
import com.trade.app.entity.Withdrawal;
import com.trade.app.enums.WithdrawalStatus;
import com.trade.app.repository.WithdrawalRepository;
import com.trade.app.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = Withdrawal.builder()
                .amount(amount)
                .user(user)
                .status(WithdrawalStatus.PENDING)
                .build();

        return withdrawalRepository.save(withdrawal);


    }

    @Override
    public Withdrawal proceedWithdrawal(Long withdrawalId, boolean accepted) throws Exception {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        if(withdrawal.isEmpty()){
            throw new Exception("Withdrawal not found");
        }
        Withdrawal withdrawal1 = withdrawal.get();
        withdrawal1.setDate(LocalDateTime.now());

        if(accepted){
            withdrawal1.setStatus(WithdrawalStatus.SUCCESS);
        }
        else {
            withdrawal1.setStatus(WithdrawalStatus.PENDING);
        }
        return withdrawalRepository.save(withdrawal1);

    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequests() {
        return withdrawalRepository.findAll();
    }
}
