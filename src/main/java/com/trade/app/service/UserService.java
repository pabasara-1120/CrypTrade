package com.trade.app.service;

import com.trade.app.dto.UserDTO;
import com.trade.app.entity.User;
import com.trade.app.enums.VerificationType;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User findUserProfileByJwt(String jwt) throws Exception;

    public UserDTO findUserProfileDtoByJwt(String jwt) throws Exception;


    public UserDTO enableTwoFactorAuthentication(VerificationType verificationType, User user);

    public UserDTO updatePassword(UserDTO userDTO,String newPassword);


}
