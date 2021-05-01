package com.example.mfa.security.datas.dtos;

import com.example.mfa.security.datas.entities.MfaEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Optional;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MfaDto implements Serializable {
    private long id;
    private String username;
    private String secretKey;
    private String type;
    private String otpNumber;

    public MfaDto(MfaEntity mfaEntity) {
        if(Optional.ofNullable(mfaEntity).isPresent()){
            this.id = mfaEntity.getId();
            this.username = mfaEntity.getUsername();
            this.secretKey = mfaEntity.getSecretKey();
            this.type = mfaEntity.getType();
        }
    }

    @Builder
    public MfaDto(long id, String username, String secretKey, String type, String otpNumber) {
        this.id = id;
        this.username = username;
        this.secretKey = secretKey;
        this.type = type;
        this.otpNumber = otpNumber;
    }
}
