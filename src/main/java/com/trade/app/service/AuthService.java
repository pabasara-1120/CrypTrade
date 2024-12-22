package com.trade.app.service;

import com.trade.app.dto.LoginDTO;
import com.trade.app.dto.UserDTO;
import com.trade.app.response.AuthResponse;

public interface AuthService {

    AuthResponse register(UserDTO userDTO) throws Exception;
    AuthResponse login(LoginDTO userDTO) throws Exception;
    AuthResponse verifySigninOtp(String otp, String id) throws Exception;


}
