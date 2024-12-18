package com.trade.app.utility;

import com.trade.app.dto.TwoFactorAuthDTO;
import com.trade.app.dto.UserDTO;
import com.trade.app.entity.TwoFactorAuth;
import com.trade.app.entity.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .twoFactorAuthDTO(
                        TwoFactorAuthDTO.builder()
                                .enabled(user.getTwoFactorAuth().isEnabled())
                                .type(user.getTwoFactorAuth().getType())
                                .build()
                )
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .twoFactorAuth(
                        userDTO.getTwoFactorAuthDTO() != null
                                ? TwoFactorAuth.builder()
                                .isEnabled(userDTO.getTwoFactorAuthDTO().isEnabled())
                                .type(userDTO.getTwoFactorAuthDTO().getType())
                                .build()
                                : new TwoFactorAuth()
                )
                .password(userDTO.getPassword())
                .build();
    }


}
