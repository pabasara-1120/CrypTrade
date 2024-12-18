package com.trade.app.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String jwt;
    private boolean status;
    private String message;
    private boolean isTwoFactorAuthEnabled;
    private String session;
}
