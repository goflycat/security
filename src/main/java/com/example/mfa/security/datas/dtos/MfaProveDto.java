package com.example.mfa.security.datas.dtos;

import com.example.mfa.security.datas.entities.MfaEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class MfaProveDto implements Serializable {
    private String secretKey;
    private String type;

    public MfaProveDto(MfaEntity mfaEntity) {
        if(Optional.ofNullable(mfaEntity).isPresent()){
            this.secretKey = mfaEntity.getSecretKey();
            this.type = mfaEntity.getType();
        }
    }

    @Builder
    public MfaProveDto(String secretKey, String type) {
        this.secretKey = secretKey;
        this.type = type;
    }
}
