package com.kilic.ismyteammatetoxic.service;

import org.springframework.http.ResponseEntity;

public interface QRCodeService {
    public  ResponseEntity<String> generateQRCode(String name, String region, String tag);
}
