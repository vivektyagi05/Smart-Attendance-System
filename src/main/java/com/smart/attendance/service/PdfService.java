package com.smart.attendance.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import org.springframework.stereotype.Service;
import com.smart.attendance.dto.SalaryResponse;
import java.io.ByteArrayOutputStream;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

@Service
public class PdfService {

    public byte[] generatePayslip(String name, Long userId, SalaryResponse salary) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 🔷 TITLE
            Paragraph title = new Paragraph("SALARY PAYSLIP")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // 🔷 USER INFO
            document.add(new Paragraph("Employee Name: " + name));
            document.add(new Paragraph("Employee ID: " + userId));

            document.add(new Paragraph("\n"));

            // 🔷 TABLE
            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));

            // Header
            table.addHeaderCell(new Cell().add(new Paragraph("Component")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            // Earnings
            table.addCell("Basic Salary");
            table.addCell("$" + salary.getBasic());

            table.addCell("HRA");
            table.addCell("$" + salary.getHra());

            table.addCell("Transport");
            table.addCell("$" + salary.getTransport());

            table.addCell("Special Allowance");
            table.addCell("$" + salary.getSpecial());

            table.addCell("Total Earnings");
            table.addCell("$" + salary.getTotalEarnings());

            // Deductions
            table.addCell("TDS");
            table.addCell("$" + salary.getTds());

            table.addCell("PF");
            table.addCell("$" + salary.getPf());

            table.addCell("Tax");
            table.addCell("$" + salary.getTax());

            table.addCell("Other");
            table.addCell("$" + salary.getOthers());

            table.addCell("Total Deductions");
            table.addCell("$" + salary.getTotalDeductions());

            document.add(table);

            document.add(new Paragraph("\n"));

            // 🔷 NET SALARY HIGHLIGHT
            Paragraph net = new Paragraph("Net Salary: $" + salary.getNetSalary())
                    .setBold()
                    .setFontSize(16)
                    .setFontColor(ColorConstants.BLUE)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(net);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}