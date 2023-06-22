package com.daniel.ltc20.controller;

import com.daniel.ltc20.domain.InscriptionMintInfo;
import com.daniel.ltc20.service.InscriptionMintInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inscription/mint")
public class InscriptionMintInfoController {
    @Autowired
    private InscriptionMintInfoService inscriptionMintInfoService;

    @RequestMapping(value = "/queryByAddress", method = RequestMethod.GET)
    public List<InscriptionMintInfo> queryByAddress(@RequestParam("address") String address) {
        return inscriptionMintInfoService.queryByAddress(address);
    }

    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public InscriptionMintInfo queryById(@RequestParam("id") int id) {
        InscriptionMintInfo inscriptionMintInfo = inscriptionMintInfoService.queryById(id);
        return inscriptionMintInfo;
    }
}
