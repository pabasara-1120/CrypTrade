package com.trade.app.service.impl;

import com.trade.app.config.JwtProvider;
import com.trade.app.dto.UserDTO;
import com.trade.app.entity.TwoFactorAuth;
import com.trade.app.entity.User;
import com.trade.app.enums.VerificationType;
import com.trade.app.repository.UserRepository;
import com.trade.app.service.UserService;
import com.trade.app.utility.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if(user==null){
            throw new Exception("User not found");
        }

        return user;

    }

    public UserDTO findUserProfileDtoByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if(user==null){
            throw new Exception("User not found");
        }

        return UserMapper.toDTO(user);

    }


    @Override
    public UserDTO enableTwoFactorAuthentication(VerificationType verificationType, User user) {
        TwoFactorAuth twoFactorAuth = TwoFactorAuth.builder()
                .isEnabled(true)
                .type(verificationType)
                .build();
        user.setTwoFactorAuth(twoFactorAuth);
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO updatePassword(UserDTO userDTO, String newPassword) {
        User user = UserMapper.toEntity(userDTO);
        user.setPassword(newPassword);
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }


}
