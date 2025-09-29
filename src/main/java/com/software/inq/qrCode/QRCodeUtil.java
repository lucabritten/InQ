package com.software.inq.qrCode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {

    public static String generateQRCodeBase64(Long userId, Long eventId, Long ticketId) {

        try {

            Map<String, Object> qrData = new HashMap<>();
            qrData.put("userId", userId);
            qrData.put("eventId", eventId);
            qrData.put("ticketId", ticketId);

            String qrContentJson = new ObjectMapper().writeValueAsString(qrData);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContentJson, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);

            return Base64.getEncoder().encodeToString(baos.toByteArray());

        }
        catch (Exception e) {
            throw new RuntimeException("Fehler beim Erzeugen des QR-Codes", e);
        }
    }
}
