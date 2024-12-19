package com.trade.app.service;

import com.trade.app.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User findUserProfileByJwt(String jwt);
    public User findUserByEmail(String email);


}
