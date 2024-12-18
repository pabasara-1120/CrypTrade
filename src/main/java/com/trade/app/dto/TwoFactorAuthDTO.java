package com.trade.app.dto;

import com.trade.app.enums.VerificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorAuthDTO {

    private boolean enabled;

    private VerificationType type;
}

