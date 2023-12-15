package com.kilic.ismyteammatetoxic.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface QRCodeService {
    public  ResponseEntity<String> generateQRCode(String name, String region, String tag);
}
