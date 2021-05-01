package com.example.mfa.security.datas.entities;

import com.example.mfa.security.datas.dtos.MfaDto;
import com.example.mfa.security.datas.dtos.MfaInitDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Getter
@Table(name = "mfa", schema = "security")
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MfaEntity implements Serializable {
    @Id
    @Column
    private long id;
    @Column(length=50)
    private String username;
    @Column(name = "secret_key", length = 512)
    private String secretKey;
    @Column(length=100)
    private String type;

    public MfaEntity(MfaDto mfaDto) {
        this.id = mfaDto.getId();
        this.username = mfaDto.getUsername();
        this.secretKey = mfaDto.getSecretKey();
        this.type = mfaDto.getType();
    }

    public MfaEntity(MfaInitDto mfaInitDto) {
        this.username = mfaInitDto.getUsername();
        this.secretKey = mfaInitDto.getSecretKey();
        this.type = mfaInitDto.getType();
    }

    @Builder
    public MfaEntity(long id, String username, String secretKey, String type) {
        this.id = id;
        this.username = username;
        this.secretKey = secretKey;
        this.type = type;
    }
}
