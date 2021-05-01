package com.example.mfa.security.services;

import com.example.mfa.security.datas.dtos.MfaDto;
import com.example.mfa.security.datas.dtos.MfaInitDto;
import com.example.mfa.security.datas.dtos.MfaProveDto;

public interface MfaService {
    MfaDto getMfa(String username);

    MfaProveDto getMfaSerialNumber(String username);

    MfaInitDto setMfa(MfaInitDto mfaInitDto);

    MfaDto setMfa(MfaDto mfaDto);

    void deleteMfa(String username);
}
