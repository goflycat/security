package com.example.mfa.security.services;

import com.example.mfa.security.datas.dtos.MfaDto;
import com.example.mfa.security.datas.dtos.MfaInitDto;
import com.example.mfa.security.datas.dtos.MfaProveDto;
import com.example.mfa.security.datas.entities.MfaEntity;
import com.example.mfa.security.repositories.MfaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MfaServiceImpl implements MfaService {

    private final MfaRepository mfaRepository;

    @Autowired
    public MfaServiceImpl(MfaRepository mfaRepository) {
        this.mfaRepository = mfaRepository;
    }

    @Override
    public MfaDto getMfa(String username){
        return new MfaDto(mfaRepository.findByUsername(username));
    }

    @Override
    public MfaProveDto getMfaSerialNumber(String username){
        return new MfaProveDto(mfaRepository.findByUsername(username));
    }

    @Override
    public MfaInitDto setMfa(MfaInitDto mfaInitDto){
        mfaRepository.save(new MfaEntity(mfaInitDto));
        return mfaInitDto;
    }

    @Override
    public MfaDto setMfa(MfaDto mfaDto){
        mfaRepository.save(new MfaEntity(mfaDto));
        return mfaDto;
    }

    @Override
    public void deleteMfa(String username){
        mfaRepository.delete(mfaRepository.findByUsername(username));
    }
}
