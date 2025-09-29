package com.software.inq.util;


import org.openpdf.text.*;
import org.openpdf.text.pdf.*;
import java.awt.Color;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class PdfUtil {

    public static byte[] generateTicketPdf(String eventName, String userName, Long ticketId, String qrCodeBase64) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Use A6 landscape (A6 = 105mm x 148mm; in points: 297 x 420)
        Rectangle pageSize = PageSize.A6.rotate();
        Document document = new Document(pageSize, 18, 18, 18, 18);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        // Draw background color
        Color bgColor = new Color(125, 125, 67);
        canvas.saveState();
        canvas.setColorFill(bgColor);
        canvas.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas.fill();
        canvas.restoreState();

        // Draw border
        Color borderColor = new Color(80, 80, 80);
        float borderWidth = 2f;
        canvas.saveState();
        canvas.setColorStroke(borderColor);
        canvas.setLineWidth(borderWidth);
        canvas.rectangle(borderWidth / 2, borderWidth / 2, pageSize.getWidth() - borderWidth, pageSize.getHeight() - borderWidth);
        canvas.stroke();
        canvas.restoreState();

        // Ticket-style layout on a single A6 landscape page
        // Title: EVENT TICKET (bold, centered)
        Font titleFont = new Font(Font.HELVETICA, 22, Font.BOLD, new Color(40, 60, 130));
        Paragraph title = new Paragraph("EVENT TICKET", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(6f);
        document.add(title);

        // Ticket layout: bordered rectangle with two columns
        // Table with 2 columns: left = info, right = QR code
        float[] colWidths = {2.1f, 1.3f};
        PdfPTable ticketTable = new PdfPTable(2);
        ticketTable.setWidthPercentage(96);
        ticketTable.setWidths(colWidths);
        ticketTable.setSpacingBefore(12f);
        ticketTable.setSpacingAfter(8f);
        ticketTable.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Left: Event details (compact, clear)
        PdfPTable infoTable = new PdfPTable(1);
        infoTable.setWidthPercentage(100);
        Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(50, 50, 50));
        Font valueFont = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(30, 30, 30));
        float rowPad = 5f;

        PdfPCell infoCell;
        // Event Name
        infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        Phrase eventPhrase = new Phrase();
        eventPhrase.add(new Chunk("Event: ", labelFont));
        eventPhrase.add(new Chunk(eventName, valueFont));
        infoCell.addElement(eventPhrase);
        infoCell.setPaddingBottom(rowPad);
        infoTable.addCell(infoCell);
        // User Name
        infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        Phrase userPhrase = new Phrase();
        userPhrase.add(new Chunk("User: ", labelFont));
        userPhrase.add(new Chunk(userName, valueFont));
        infoCell.addElement(userPhrase);
        infoCell.setPaddingBottom(rowPad);
        infoTable.addCell(infoCell);
        // Ticket ID
        infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        Phrase idPhrase = new Phrase();
        idPhrase.add(new Chunk("Ticket ID: ", labelFont));
        idPhrase.add(new Chunk(String.valueOf(ticketId), valueFont));
        infoCell.addElement(idPhrase);
        infoCell.setPaddingBottom(rowPad);
        infoTable.addCell(infoCell);
        // Add left column to ticketTable
        PdfPCell leftCol = new PdfPCell(infoTable);
        leftCol.setBorder(Rectangle.NO_BORDER);
        leftCol.setVerticalAlignment(Element.ALIGN_MIDDLE);
        leftCol.setPaddingLeft(8f);
        leftCol.setPaddingTop(8f);
        leftCol.setPaddingBottom(8f);
        ticketTable.addCell(leftCol);

        // Right: QR code (centered)
        PdfPCell qrCol = new PdfPCell();
        qrCol.setBorder(Rectangle.NO_BORDER);
        qrCol.setHorizontalAlignment(Element.ALIGN_CENTER);
        qrCol.setVerticalAlignment(Element.ALIGN_MIDDLE);
        qrCol.setPaddingTop(8f);
        qrCol.setPaddingBottom(8f);
        qrCol.setPaddingRight(8f);
        byte[] qrBytes = Base64.getDecoder().decode(qrCodeBase64);
        if (qrBytes.length > 0) {
            Image qrImage = Image.getInstance(qrBytes);
            // Fit QR code to column, max 70x70pt
            qrImage.scaleToFit(70, 70);
            qrImage.setAlignment(Image.MIDDLE);
            qrCol.addElement(qrImage);
        }
        ticketTable.addCell(qrCol);

        document.add(ticketTable);

        document.close();
        return baos.toByteArray();
    }
}