package com.kilic.ismyteammatetoxic.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kilic.ismyteammatetoxic.service.QRCodeService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api")

public class QRCodeController {

    @Autowired QRCodeService qrCodeService;
    Logger logger = LoggerFactory.getLogger(LolGameRestController.class);

    @GetMapping("/qr/generate/{name}")
    public @ResponseBody ResponseEntity<String> getLolMatcheHistory(
            @PathVariable String name,
            @RequestParam String region,
            @RequestParam String summonerTag) {
                return qrCodeService.generateQRCode(name, region, summonerTag);
            }
}
