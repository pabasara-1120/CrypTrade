package com.trade.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trade.app.entity.TwoFactorAuth;
import com.trade.app.enums.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String fullName;

    private String email;

    private USER_ROLE role;

    private TwoFactorAuthDTO twoFactorAuthDTO;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
