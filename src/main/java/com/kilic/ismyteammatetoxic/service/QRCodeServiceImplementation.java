package com.kilic.ismyteammatetoxic.service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.glxn.qrgen.javase.QRCode;


@Service
public class QRCodeServiceImplementation implements QRCodeService  {

    @Override
    public ResponseEntity<String> generateQRCode(String name, String region, String tag) {
         try {

            String linkForQrCode = "https://www.just-isa.de/#/links?region=" + region + "&name=" + name + "&tag=" + tag;

            // Generate QR code using QRGen
            ByteArrayOutputStream stream = QRCode.from(linkForQrCode).withSize(250, 250).stream();

            // Convert QR code image to byte array
            byte[] qrCode = stream.toByteArray();

            // Encode the byte array into base64
            String base64Image = Base64.getEncoder().encodeToString(qrCode);

            // Return the base64 string as a response
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions or invalid input
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
