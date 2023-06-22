package com.daniel.ltc20.service.impl;

import com.daniel.ltc20.dao.InscriptionMintInfoDao;
import com.daniel.ltc20.domain.InscriptionMintInfo;
import com.daniel.ltc20.service.InscriptionMintInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscriptionMintInfoServiceImpl implements InscriptionMintInfoService {
    @Autowired
    private InscriptionMintInfoDao inscriptionMintInfoDao;

    @Override
    public void insert(InscriptionMintInfo inscriptionMintInfo) {
        List<InscriptionMintInfo> results = inscriptionMintInfoDao.getInscriptionMintInfoByAddressAndInscriptionName(inscriptionMintInfo.getAddress(),inscriptionMintInfo.getInscriptionName());
        if(results!=null&&results.size()>0){
            System.out.println("数据已经写入数据库");
            return;
        }
        System.out.println("数据还未写入数据库,准备写入");
        inscriptionMintInfoDao.insertInscriptionMintInfo(inscriptionMintInfo);
    }

    @Override
    public void insert(List<InscriptionMintInfo> inscriptionMintInfos) {
        for (InscriptionMintInfo inscriptionMintInfo : inscriptionMintInfos) {
            this.insert(inscriptionMintInfo);
        }
    }

    @Override
    public List<InscriptionMintInfo> queryByAddress(String address) {
        return inscriptionMintInfoDao.getInscriptionMintInfoByAddress(address);
    }

    @Override
    public InscriptionMintInfo queryById(int id) {
        return inscriptionMintInfoDao.getInscriptionMintInfoById(id);
    }
}
