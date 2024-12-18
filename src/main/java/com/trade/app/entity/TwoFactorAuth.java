package com.trade.app.entity;

import com.trade.app.enums.VerificationType;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType type;

}
