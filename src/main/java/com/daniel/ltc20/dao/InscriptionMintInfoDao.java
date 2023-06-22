package com.daniel.ltc20.dao;

import com.daniel.ltc20.domain.InscriptionMintInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InscriptionMintInfoDao {
    void insertInscriptionMintInfo(InscriptionMintInfo inscriptionMintInfo);

    void updateInscriptionMintInfo(InscriptionMintInfo inscriptionMintInfo);

    void deleteInscriptionMintInfo(int id);

    InscriptionMintInfo getInscriptionMintInfoById(int id);

    List<InscriptionMintInfo> getInscriptionMintInfoByAddressAndInscriptionName(String address,String inscriptionName);

    List<InscriptionMintInfo> getAllInscriptionMintInfo();

    List<InscriptionMintInfo> getInscriptionMintInfoByAddress(String address);
}
