package com.example.mfa.security.repositories;

import com.example.mfa.security.datas.entities.MfaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MfaRepository extends JpaRepository<MfaEntity, Long> {
    MfaEntity findByUsername(String username);
}
