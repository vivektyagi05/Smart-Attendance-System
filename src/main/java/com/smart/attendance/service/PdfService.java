package com.smart.attendance.service;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.smart.attendance.model.Attendance;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

    public byte[] generateAttendancePdf(
            List<Attendance> list,
            String employeeCode) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();

            doc.add(new Paragraph("Attendance Report"));
            doc.add(new Paragraph("Employee Code: " + employeeCode));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell("Date");
            table.addCell("Check In");
            table.addCell("Check Out");
            table.addCell("Status");
            table.addCell("Working Hours");
            

            for (Attendance a : list) {

                table.addCell(a.getDate().toString());
                table.addCell(a.getCheckIn() == null ? "-" : a.getCheckIn().toString());
                table.addCell(a.getCheckOut() == null ? "-" : a.getCheckOut().toString());
                table.addCell(a.getStatus());

                Integer min = a.getWorkingMinutes();
                table.addCell(min == null ? "-" :
                        (min / 60) + ":" + String.format("%02d", min % 60));
            }

            doc.add(table);
            doc.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}
