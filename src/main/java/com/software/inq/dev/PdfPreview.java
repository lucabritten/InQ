package com.software.inq.dev;

import com.software.inq.util.PdfUtil;

import java.io.FileOutputStream;
import java.time.LocalDateTime;

public class PdfPreview {
    public static void main(String[] args) throws Exception {
        String eventName = "Spring Boot Konzert";
        String userName = "Max Mustermann";
        LocalDateTime date = LocalDateTime.now();
        Long ticketId = 42L;
        String qrCodeBase64 = "iVBORw0KGgoAAAANSUhEUgAAAMgAAADIAQAAAACFI5MzAAABdklEQVR4Xu2XTY6DMAyFjbrIkiPkJnAxpEbiYvQmOQLLLBCe9xxE0XRm68xIWGpV87F48n9FfzP5/uC0m9yE9jdIFpGHBl0fWV+a4PXeBJ85h6VPUkadVXd3UqSfVTpNEdrmLEMTksNLNwamGdEyyJMCmxBlfsq4SuSvj8w5ENZoLgMCg6+P6nUgh+2ITulO15NAGxVRm4zrFFEr3iRQVqBbOoyL8M6PE1GISRGTYosyqo0LZ5Kh6GkNYq2STaUrYWASs7JFDO0pXlX7EBtTEbIeqgv6BXHyJsVq1CbFWSuuRDGm4O8yoThW7M9TmxuRQSSiN44k1XHhSTJiMkV6XBYzB4c3KdxU7NPaohdtbmSoozrxcuDoemvzIuPxkGVC8yZmHNoi6JerNi+SxfY2+1R4PdVXXYnWiwV9qgu1+RO7IIVni4XIQANCRVnYIExSC4LU4Iq2jmW1+hLlDRv4R4obowWpNdpxbxNf9rYT+dluchPa/yRfnw6Fs7omcYIAAAAASUVORK5CYII="; // Hier ein Dummy-QR-Code einsetzen (z.B. aus einer Datei laden)

        byte[] pdf = PdfUtil.generateTicketPdf(eventName,date, userName, ticketId, qrCodeBase64);

        try (FileOutputStream fos = new FileOutputStream("ticket-preview.pdf")) {
            fos.write(pdf);
        }

        System.out.println("Ticket gespeichert unter ticket-preview.pdf");
    }
}