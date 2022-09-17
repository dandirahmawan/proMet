package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.TblHadirReservasiOnline;
import com.indonesiapowe.proMET.Model.TblReservasiOnline;
import com.indonesiapowe.proMET.Repository.TblHadirReservasiOnlineRepository;
import com.indonesiapowe.proMET.Repository.TblReservasiOnlineRepository;
import com.indonesiapowe.proMET.StoreFile;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PdfServices {

    @Autowired
    TblHadirReservasiOnlineRepository repo;

    @Autowired
    TblReservasiOnlineRepository reservRepo;

    @Autowired
    StoreFile sf;

    public void exportPdf(HttpServletRequest request, String reservasiId, HttpServletResponse response) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String stringdate = dateFormat.format(new Date());

            File dir = new File("../report");
            if(!dir.exists()){
                dir.mkdir();
            }

            PdfWriter writer = new PdfWriter("../report/REPORT-"+stringdate+".pdf");
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            Optional<TblReservasiOnline> dataOpt = reservRepo.findById(reservasiId);
            String ids = dataOpt.map(TblReservasiOnline::getId).orElse("");
            if(!ids.equals("")){
                this.headerReport(doc, dataOpt.get());
                this.listTable(doc, reservasiId);
            }

            doc.close();
            writer.close();

            sf.download(response,"../report/REPORT-"+stringdate+".pdf", "REPORT-"+stringdate+".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void headerReport(Document doc, TblReservasiOnline reservasi) throws Exception {
        /*creating table*/
        float [] pointColumnWidths = {80, 400};
        Table table = new Table(pointColumnWidths);
        table.setWidthPercent(100);

        Table tblWrapper = new Table(1);
        table.setWidthPercent(100);

        String path = ResourceUtils.getFile("classpath:static/image/logo.png").getPath();
        ImageData data = ImageDataFactory.create(path);
        Image img = new Image(data);
        img.setWidth(50);

        Cell cell1 = new Cell();
        cell1.add(img);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Cell cell2 = new Cell();
        cell2.setBorder(Border.NO_BORDER);
        cell2.add("PT. INDONESIA POWER");
        cell2.setFont(font);
        cell2.setFontSize(10);
        table.addCell(cell2);
        tblWrapper.addCell(table);

        Table table0 = new Table(1);
        table0.setWidthPercent(100);
        Cell cell3 = new Cell();
        cell3.setFontSize(14);
        cell3.setFont(font);
        cell3.setBorder(Border.NO_BORDER);
        cell3.add("DAFTAR HADIR");
        cell3.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table0.setTextAlignment(TextAlignment.CENTER);
        table0.addCell(cell3);
        tblWrapper.addCell(table0);

        float [] pointColumnWidths1 = {90, 10, 400};
        Table table1 = new Table(pointColumnWidths1);
        table1.setWidthPercent(100);

        Date date = reservasi.getScheduleMeet();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);

        SimpleDateFormat time1 = new SimpleDateFormat("HH:mm");
        String startTime = time1.format(date);
        String endTime = time1.format(reservasi.getScheduleMeetEnd());

        table1.addCell(this.cellHeader("ACARA"));
        table1.addCell(this.cellHeader(":"));
        table1.addCell(this.cellHeader(reservasi.getTitle()));

        Table table2 = new Table(pointColumnWidths1);
        table2.setWidthPercent(100);
        table2.addCell(this.cellHeader("TANGGAL / HARI"));
        table2.addCell(this.cellHeader(":"));
        table2.addCell(this.cellHeader(dateString));

        Table table3 = new Table(pointColumnWidths1);
        table3.setWidthPercent(100);
        table3.addCell(this.cellHeader("JAM"));
        table3.addCell(this.cellHeader(":"));
        table3.addCell(this.cellHeader(startTime+" - "+endTime));

        Table table4 = new Table(pointColumnWidths1);
        table4.setWidthPercent(100);
        table4.addCell(this.cellHeader("TEMPAT"));
        table4.addCell(this.cellHeader(":"));
        table4.addCell(this.cellHeader(reservasi.getLokasi()));

        Table table5 = new Table(pointColumnWidths1);
        table5.setWidthPercent(100);
        table5.addCell(this.cellHeader("PIMPINAN RAPAT"));
        table5.addCell(this.cellHeader(":"));
        table5.addCell(this.cellHeader(reservasi.getPic()));

        tblWrapper.addCell(table1);
        tblWrapper.addCell(table2);
        tblWrapper.addCell(table3);
        tblWrapper.addCell(table4);
        tblWrapper.addCell(table5);
        doc.add(tblWrapper);
    }

    public Cell cellHeader(String text){
        Cell cell = new Cell();
        cell.add(text);
        cell.setBorder(Border.NO_BORDER);
        cell.setFontSize(8);
        return cell;
    }

    public Cell cell(String text, float fontSize, HorizontalAlignment alignment, PdfFont font){
        Cell cell = new Cell();
        cell.add(text);
        cell.setHorizontalAlignment(alignment);
        cell.setFontSize(fontSize);
        cell.setFont(font);
        return cell;
    }

    public void listTable(Document doc, String reservasiId) throws Exception{
        float [] pointColumnWidths1 = {15, 90, 90, 90, 90, 90};
        Table table = new Table(pointColumnWidths1);
        table.setWidthPercent(100);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        PdfFont font1 = PdfFontFactory.createFont(FontConstants.HELVETICA);

        table.addCell(this.cell("No", 8, HorizontalAlignment.CENTER, font));
        table.addCell(this.cell("Nama", 8, HorizontalAlignment.CENTER, font));
        table.addCell(this.cell("NID", 8, HorizontalAlignment.CENTER, font));
        table.addCell(this.cell("Instansi", 8, HorizontalAlignment.CENTER, font));
        table.addCell(this.cell("Divisi / Unit", 8, HorizontalAlignment.CENTER, font));
        table.addCell(this.cell("Jabatan", 8, HorizontalAlignment.CENTER, font));

        /*get list data*/
        List<TblHadirReservasiOnline> data = repo.findByReservasiId(reservasiId);
        for(int i = 0;i<data.size();i++){
            TblHadirReservasiOnline item = data.get(i);
            String name = item.getNama();
            String nid = item.getNid();
            String instansi = item.getInstansi();
            String div = item.getDivisiUnit();
            String jabatan = item.getJabatan();
            String no = String.valueOf(i + 1);

            table.addCell(this.cell(no, 8, HorizontalAlignment.CENTER, font1));
            table.addCell(this.cell(name, 8, HorizontalAlignment.CENTER, font1));
            table.addCell(this.cell(nid, 8, HorizontalAlignment.CENTER, font1));
            table.addCell(this.cell(instansi, 8, HorizontalAlignment.CENTER, font1));
            table.addCell(this.cell(div, 8, HorizontalAlignment.CENTER, font1));
            table.addCell(this.cell(jabatan, 8, HorizontalAlignment.CENTER, font1));
        }

        doc.add(table);
    }
}
