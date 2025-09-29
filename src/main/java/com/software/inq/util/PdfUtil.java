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
        Color bgColor = new Color(245, 245, 240);
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

        // Title: EVENT TICKET
        Font titleFont = new Font(Font.HELVETICA, 28, Font.BOLD, new Color(40, 60, 130));
        Paragraph title = new Paragraph("EVENT TICKET", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(8f);
        document.add(title);

        // Colored line separator
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(80);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setMinimumHeight(5f);
        lineCell.setBackgroundColor(new Color(40, 60, 130));
        lineCell.setBorder(Rectangle.NO_BORDER);
        lineTable.addCell(lineCell);
        lineTable.setSpacingAfter(18f);
        lineTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        document.add(lineTable);

        // Details section
        Font labelFont = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(50, 50, 50));
        Font valueFont = new Font(Font.HELVETICA, 14, Font.NORMAL, new Color(30, 30, 30));

        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(70);
        detailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        detailsTable.setSpacingAfter(8f);
        detailsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        detailsTable.setWidths(new float[]{1.3f, 2.5f});

        PdfPCell cell;
        // Event
        cell = new PdfPCell(new Phrase("Event:", labelFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(7f);
        detailsTable.addCell(cell);
        cell = new PdfPCell(new Phrase(eventName, valueFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(7f);
        detailsTable.addCell(cell);
        // User
        cell = new PdfPCell(new Phrase("User:", labelFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(7f);
        detailsTable.addCell(cell);
        cell = new PdfPCell(new Phrase(userName, valueFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(7f);
        detailsTable.addCell(cell);
        // Ticket ID
        cell = new PdfPCell(new Phrase("Ticket ID:", labelFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(7f);
        detailsTable.addCell(cell);
        cell = new PdfPCell(new Phrase(String.valueOf(ticketId), valueFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(7f);
        detailsTable.addCell(cell);

        document.add(detailsTable);

        // QR code image
        byte[] qrBytes = Base64.getDecoder().decode(qrCodeBase64);
        Image qrImage = Image.getInstance(qrBytes);
        qrImage.scaleToFit(80, 80);
        qrImage.setAlignment(Element.ALIGN_CENTER);
        document.add(qrImage);

        document.close();
        return baos.toByteArray();
    }
}