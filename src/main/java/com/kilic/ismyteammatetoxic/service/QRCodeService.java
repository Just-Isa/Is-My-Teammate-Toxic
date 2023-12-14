package com.kilic.ismyteammatetoxic.service;

import java.util.Base64;

import org.springframework.http.ResponseEntity;

public interface QRCodeService {
    public  ResponseEntity<String> generateQRCode(String name, String region, String tag);
    public String[] getLinksForQRCode(Base64 QRCode);
}
