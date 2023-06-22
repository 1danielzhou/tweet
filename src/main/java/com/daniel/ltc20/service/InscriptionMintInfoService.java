package com.daniel.ltc20.service;

import com.daniel.ltc20.domain.InscriptionMintInfo;

import java.util.List;

public interface InscriptionMintInfoService {
    public void insert(InscriptionMintInfo inscriptionMintInfo);

    public void insert(List<InscriptionMintInfo> inscriptionMintInfos);

    public List<InscriptionMintInfo> queryByAddress(String address);

    public InscriptionMintInfo queryById(int id);
}
