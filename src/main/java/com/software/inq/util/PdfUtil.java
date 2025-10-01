package com.software.inq.util;


import org.openpdf.text.*;
import org.openpdf.text.pdf.*;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class PdfUtil {

    private static final Color STUB_BG_COLOR = new Color(169, 169, 169); // grau
    private static final Color MAIN_BG_COLOR = new Color(0, 128, 128);   // teal
    private static final Color QR_BG_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = Color.BLACK;

    private static final float STUB_WIDTH = 50f;
    private static final float QR_WIDTH = 130f; // etwas breiter, damit QR mehr Platz hat

    private PdfUtil() {
        // Utility-Klasse, kein Instanziieren nötig
    }

    public static byte[] generateTicketPdf(
            String eventName,
            LocalDateTime eventDate,
            String userName,
            String userEmail,
            Long ticketId,
            String qrCodeBase64
    ) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Rectangle pageSize = PageSize.A6.rotate();
        Document document = new Document(pageSize, 0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();
        float mainWidth = pageWidth - STUB_WIDTH - QR_WIDTH;

        drawBackgroundSections(canvas, pageHeight, pageWidth, mainWidth);
        drawSectionBorders(canvas, pageHeight, pageWidth, mainWidth);

        drawStubSection(canvas, pageHeight, ticketId);
        drawMainSection(canvas, pageHeight, mainWidth, eventName, eventDate, userName, userEmail);
        drawQrSection(canvas, pageHeight, mainWidth, qrCodeBase64);

        document.close();
        return baos.toByteArray();
    }

    private static void drawBackgroundSections(PdfContentByte canvas, float pageHeight, float pageWidth, float mainWidth) {
        // Stub links
        canvas.saveState();
        canvas.setColorFill(STUB_BG_COLOR);
        canvas.rectangle(0, 0, STUB_WIDTH, pageHeight);
        canvas.fill();
        canvas.restoreState();

        // Hauptbereich
        canvas.saveState();
        canvas.setColorFill(MAIN_BG_COLOR);
        canvas.rectangle(STUB_WIDTH, 0, mainWidth, pageHeight);
        canvas.fill();
        canvas.restoreState();

        // QR-Bereich rechts
        canvas.saveState();
        canvas.setColorFill(QR_BG_COLOR);
        canvas.rectangle(STUB_WIDTH + mainWidth, 0, QR_WIDTH, pageHeight);
        canvas.fill();
        canvas.restoreState();
    }

    private static void drawSectionBorders(PdfContentByte canvas, float pageHeight, float pageWidth, float mainWidth) {
        canvas.saveState();
        canvas.setColorStroke(BORDER_COLOR);
        canvas.setLineWidth(1f);

        // Linien zwischen den Abschnitten
        canvas.moveTo(STUB_WIDTH, 0);
        canvas.lineTo(STUB_WIDTH, pageHeight);

        canvas.moveTo(STUB_WIDTH + mainWidth, 0);
        canvas.lineTo(STUB_WIDTH + mainWidth, pageHeight);

        // Außenrahmen
        canvas.rectangle(0, 0, pageWidth, pageHeight);
        canvas.stroke();
        canvas.restoreState();
    }

    private static void drawStubSection(PdfContentByte canvas, float pageHeight, Long ticketId) {
        Font stubFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.BLACK);
        Phrase ticketNumber = new Phrase("No. " + ticketId, stubFont);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                ticketNumber,
                STUB_WIDTH / 2,
                pageHeight / 2,
                90
        );
    }

    private static void drawMainSection(
            PdfContentByte canvas,
            float pageHeight,
            float mainWidth,
            String eventName,
            LocalDateTime eventDate,
            String userName,
            String userEmail
    ) {
        Font eventDateFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Font eventNameFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.WHITE);
        Font ticketFont = new Font(Font.HELVETICA, 36, Font.BOLD, Color.WHITE);
        Font userNameFont = new Font(Font.HELVETICA, 16, Font.NORMAL, Color.WHITE);
        Font userEmailFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.WHITE);


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        String formattedDate = eventDate.format(dateFormatter);

        // Event-Datum oben links
        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_LEFT,
                new Phrase(formattedDate, eventDateFont),
                STUB_WIDTH + 8,
                pageHeight - 20,
                0
        );

        // Eventname unter dem Datum, linksbündig
        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_LEFT,
                new Phrase(eventName, eventNameFont),
                STUB_WIDTH + 8,
                pageHeight - 40,
                0
        );

        // "TICKET" groß in der Mitte
        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("TICKET", ticketFont),
                STUB_WIDTH + mainWidth / 2,
                pageHeight / 2 + 10,
                0
        );

        // Benutzername unterhalb
        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase(userName, userNameFont),
                STUB_WIDTH + mainWidth / 2,
                pageHeight / 2 - 30,
                0
        );

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase(userEmail, userEmailFont),
                STUB_WIDTH + mainWidth / 2,
                pageHeight / 2 - 40,
                0
        );
    }

    private static void drawQrSection(
            PdfContentByte canvas,
            float pageHeight,
            float mainWidth,
            String qrCodeBase64
    ) {
        try {
            byte[] qrBytes = Base64.getDecoder().decode(qrCodeBase64);
            if (qrBytes.length == 0) {
                return;
            }

            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleToFit(QR_WIDTH - 20, pageHeight - 40);

            float qrX = STUB_WIDTH + mainWidth + (QR_WIDTH - qrImage.getScaledWidth()) / 2;
            float qrY = (pageHeight - qrImage.getScaledHeight()) / 2;

            qrImage.setAbsolutePosition(qrX, qrY);
            canvas.addImage(qrImage);

            // Add label "Scan Here" below the QR code with updated style and position
            Font labelFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(50, 50, 50));
            Phrase label = new Phrase("Scan Here", labelFont);

            // Draw decorative dashed line above the label
            canvas.saveState();
            canvas.setLineWidth(1f);
            canvas.setColorStroke(new Color(50, 50, 50));
            canvas.setLineDash(3f, 3f);
            float lineWidth = 40f;
            float lineXStart = STUB_WIDTH + mainWidth + QR_WIDTH / 2 - lineWidth / 2;
            float lineXEnd = lineXStart + lineWidth;
            float lineY = qrY;
            canvas.moveTo(lineXStart, lineY);
            canvas.lineTo(lineXEnd, lineY);
            canvas.stroke();
            canvas.restoreState();

            ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_CENTER,
                    label,
                    STUB_WIDTH + mainWidth + QR_WIDTH / 2,
                    qrY - 25,
                    0
            );
        } catch (Exception e) {
            // Fehler beim Zeichnen des QR-Codes ignorieren
        }
    }
}