package com.indonesiapowe.proMET.Service;

import com.indonesiapowe.proMET.Model.*;
import com.indonesiapowe.proMET.Model.ModelView.TblDetailReservasiLayoutView;
import com.indonesiapowe.proMET.Repository.TblMasterUnitRepository;
import com.indonesiapowe.proMET.Repository.TblRealisasiBiayaKonsumsiRepository;
import com.indonesiapowe.proMET.Repository.ViewTblRealisasiBiayaKonsumsiRepository;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RealisasiBiayaKonsumsiService {

    @Autowired
    TblRealisasiBiayaKonsumsiRepository trbkr;

    @Autowired
    ViewTblRealisasiBiayaKonsumsiRepository vtrbkr;

    @Autowired
    ViewTblUsersService vtus;

    @Autowired
    TblMasterUnitRepository tmur;

    @Value("${role.id.superadmin}")
    String roleSuperAdmin;

    @Value("${role.id.adminpromet}")
    String roleAdminPromet;

    @Value("${role.id.adminfasilitas}")
    String roleAdminFasilitas;

    public List<ViewTblRealisasiBiayaKonsumsi> getAll(String username, Date startDate, Date endDate){
        if(username != null){
            ViewTblUsers vtu = vtus.getByEmail(username);
            String idUnit = vtu.getUnitId();
            String site = vtu.getSitegroup();
            String roleId = vtu.getRoleId();

            if(site.equals("IP")){
                Optional<TblMasterUnit> masterUnit = tmur.findByNamaUnitIgnoreCase("kantor pusat");
                idUnit = masterUnit.map(TblMasterUnit::getId).orElse("");
            }

            if(roleId.equals(roleAdminFasilitas)){
                if(startDate == null || endDate == null){
                    return vtrbkr.findByIdUnitOrderByCreatedDateDesc(idUnit);
                }else{
                    return vtrbkr.findByIdUnitAndTanggalAcaraBetweenOrderByCreatedDateDesc(idUnit, startDate, endDate);
                }
            }else if(roleId.equals(roleSuperAdmin) || roleId.equals(roleAdminPromet)){
                if(startDate == null || endDate == null) {
                    return vtrbkr.findAllByOrderByCreatedDateDesc();
                }else{
                    return vtrbkr.findAllByTanggalAcaraBetweenOrderByCreatedDateDesc(startDate, endDate);
                }
            }else{
                if(startDate == null || endDate == null) {
                    return vtrbkr.findByCreatedByOrderByCreatedDateDesc(username);
                }else{
                    return vtrbkr.findByCreatedByAndTanggalAcaraBetweenOrderByCreatedDateDesc(username, startDate, endDate);
                }
            }

//            String roleId = vtu.getRoleId();
//            if(roleId.equals(roleSuperAdmin) || roleId.equals(roleAdminPromet)){
//                return vtrbkr.findAllByOrderByCreatedDateDesc();
//            }else{
//                return vtrbkr.findByIdUnitOrderByCreatedDateDesc(idUnit);
//            }
//            return vtrbkr.findByIdUnitOrderByCreatedDateDesc(idUnit);
        }else{
            if(startDate == null || endDate == null) {
                return vtrbkr.findAllByOrderByCreatedDateDesc();
            }else{
                return vtrbkr.findAllByTanggalAcaraBetweenOrderByCreatedDateDesc(startDate, endDate);
            }
        }
    }

    public Object post(TblRealisasiBiayaKonsumsi tblRealisasiBiayaKonsumsi){
        String id = tblRealisasiBiayaKonsumsi.getId();
        Date now = new Date();
        Map<String, Object> map = new HashMap<>();

        if(id == null){
            tblRealisasiBiayaKonsumsi.setCreatedDate(now);
            trbkr.save(tblRealisasiBiayaKonsumsi);
            map.put("code", 1);
            map.put("message", "Simpan realisasi biaya konsumsi berhasil");
        }else{
            Optional<TblRealisasiBiayaKonsumsi> data = trbkr.findById(id);
            String ids = data.map(TblRealisasiBiayaKonsumsi::getId).orElse("");

            if(!ids.equals("")){
                /*set data created date*/
                tblRealisasiBiayaKonsumsi.setCreatedDate(data.get().getCreatedDate());
                tblRealisasiBiayaKonsumsi.setLastModifiedDate(now);
                trbkr.save(tblRealisasiBiayaKonsumsi);
                map.put("code", 1);
                map.put("message", "Update realisasi biaya konsumsi berhasil");
            }else{
                map.put("code", -1);
                map.put("message", "Data realisasi biaya konsumsi tidak ditemukan");
            }
        }

        return map;
    }

    public Object getDataById(String id){
        return vtrbkr.findById(id);
    }

    public Object delete(String id){
        trbkr.deleteById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("message", "Delete data berghasil");
        return map;
    }

    public void export(HttpServletResponse response, String type, String username, String sd, String ed){
        List<ViewTblRealisasiBiayaKonsumsi> data = new ArrayList<>();

        if(sd != null){
            try{
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(sd);
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(ed);
                data = this.getAll(username, startDate, endDate);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            data =  this.getAll(username, null, null);
        }

        if(type.equalsIgnoreCase("excel")){
            try {
                this.exportExcel(data, response);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

//    public void exportExcel(List<ViewTblRealisasiBiayaKonsumsi> data, HttpServletResponse response) throws Exception{
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Sheet1");
//
//        /*set header column*/
//        String[] columnHeader = {"No", "Tanggal Permohonan", "Tanggal Acara", "Waktu Mulai", "Waktu Selesai",
//                "Divisi / Department / Bidang", "Nama Rapat", "Ruang Rapat", "Gedung", "Lantai",
//                "Jumlah Peserta Internal", "Jumlah Peserta External", "Keterangan Peserta External",
//                "Jumlah Konsumsi yang Disetujui", "Snack Pagi (Nama Vendor)", "Snack Siang (Nama Vendor)",
//                "Snack Sore (Nama Vendor)", "Peralatan Bantu", "Rupiah Konsumsi"};
//
//
//        HSSFFont fontHeader = workbook.createFont();
//        fontHeader.setBold(true);
//
//        HSSFCellStyle styleHeader = workbook.createCellStyle();
//        styleHeader.setBorderBottom(BorderStyle.THIN);
//        styleHeader.setBorderTop(BorderStyle.THIN);
//        styleHeader.setBorderLeft(BorderStyle.THIN);
//        styleHeader.setBorderRight(BorderStyle.THIN);
//        styleHeader.setFont(fontHeader);
//
//        HSSFCellStyle style = workbook.createCellStyle();
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        /*create header row*/
//        Row rowHeader = sheet.createRow(0);
//        for(int ic = 0;ic < columnHeader.length;ic++){
//            Cell cellHeader = rowHeader.createCell(ic);
//            cellHeader.setCellValue(columnHeader[ic]);
//            cellHeader.setCellStyle(styleHeader);
//        }
//
//
//        int i = 0;
//        int no = 0;
//        for(ViewTblRealisasiBiayaKonsumsi item : data){
////            System.out.println(item.getId());
//            i++;
//            no++;
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
//
//            Date tglPemrmohonan = item.getTanggalPermohonan();
//            Date tglAcara = item.getTanggalAcara();
//            Date waktuMulai = item.getReservasi().getJadwalMulai();
//            Date waktuSelesai = item.getReservasi().getJadwalSelesai();
//            String tglPermohonanFmt = dateFormat.format(tglPemrmohonan);
//            String tglAcaraFmt = dateFormat.format(tglAcara);
//            String waktuMulaiFmt = timeFormat.format(waktuMulai);
//            String waktuSelesaiFmt = timeFormat.format(waktuSelesai);
//            String namaRapat = item.getReservasi().getNamaRapat();
//            Integer jumlahInternal = item.getReservasi().getJumlahInternal();
//            Integer jumlahExternal = item.getReservasi().getJumlahExternal();
//            String ketPeserta = item.getReservasi().getKeteranganPeserta();
//            Integer jumlahKonsumsi = item.getJumlahKonsumsiDisetujui();
//            String snackPagi = (item.getSnackPagi() != null) ? item.getSnackPagi().getNamaVendor() : "";
//            String snackSore = (item.getSnackSore() != null) ? item.getSnackSore().getNamaVendor() : "";
//            String snackSiang = (item.getSnackSiang() != null) ? item.getSnackSiang().getNamaVendor() : "";
//            BigDecimal rupiahKonsumsi = item.getRupiahKonsumsi();
//
//            String department = item.getReservasi().getCreatedByData().getDepartment();
//            String bidang = item.getReservasi().getCreatedByData().getOrganization();
//            String bidDep = (bidang != null) ? bidang : (department != null) ? department : "";
//
//            /*get data layouts*/
//            List<TblDetailReservasiLayoutView> layouts = item.getReservasi().getLayouts();
//            int sizeLayouts = layouts.size();
//            int mergeRowTo = i + (layouts.size() - 1);
//
//
//            String fas = "";
//            List<TblDetailReservasiFasilitas> fasilitas = item.getReservasi().getFasilitas();
//                for(int ifas = 0;ifas < fasilitas.size();ifas++){
//                    TblDetailReservasiFasilitas itemFas = fasilitas.get(ifas);
//                    String namaFas = itemFas.getDataFasilitas().getNama();
//                    String countFas = String.valueOf(itemFas.getJumlah());
//                    String keteranganFas = namaFas+"("+countFas+")";
//                    keteranganFas = (ifas + 1 == fasilitas.size()) ? keteranganFas : keteranganFas+", ";
//                    fas += keteranganFas;
//            }
//
//            Object[] cellData = {no, tglPermohonanFmt, tglAcaraFmt, waktuMulaiFmt, waktuSelesaiFmt, bidDep, namaRapat, layouts,
//                                layouts, layouts, jumlahInternal, jumlahExternal, ketPeserta, jumlahKonsumsi, snackPagi, snackSiang,
//                                snackSore, fas, rupiahKonsumsi};
//
//
//            /*create data row*/
//            Row rowData = sheet.createRow(i);
//            Row rowDataMerge = sheet.createRow(mergeRowTo); /*for merge to next row needs*/
//
//            /*create row for ruangan rapat, gedung needs*/
//            int rowUnmerged = i;
//            List<Row> rowsUnmerged = new ArrayList<>();
//            if(layouts.size() > 1){
//                for(int ir = 0;ir<layouts.size();ir++){
//                    Row rowRapat = sheet.createRow(rowUnmerged);
//                    rowsUnmerged.add(rowRapat);
//                    rowUnmerged++;
//                }
//            }
//
//            for(int ii = 0;ii<cellData.length;ii++){
//                Cell cell =  rowData.createCell(ii);
//                cell.setCellStyle(style); /*set border data cell*/
//                String columnName = columnHeader[ii];
//
//                /*merge cell to next row*/
//                if(sizeLayouts > 1){
//                    if(columnName.equals("Ruang Rapat") || columnName.equals("Gedung") || columnName.equals("Lantai")){
//                        /*set data ruang rapat*/
//                        for(int ir = 0;ir<rowsUnmerged.size();ir++){
//                            Row r = rowsUnmerged.get(ir);
//                            Cell cellRapat = r.createCell(ii);
//                            String ruangRapat = layouts.get(ir).getRuangan().getNamaRuangan();
//                            String gedung = layouts.get(ir).getRuangan().getNamaGedung();
//                            Integer lantai = Integer.parseInt(layouts.get(ir).getRuangan().getLantai());
//
//                            if(columnName.equals("Ruang Rapat")) cellRapat.setCellValue(ruangRapat);
//                            if(columnName.equals("Gedung")) cellRapat.setCellValue(gedung);
//                            if(columnName.equals("Lantai")) cellRapat.setCellValue(lantai);
//                            cellRapat.setCellStyle(style);
//                        }
//                    }else {
//                        Cell cellMergedRow = rowDataMerge.createCell(ii);
//                        cellMergedRow.setCellStyle(style);
//                        sheet.addMergedRegion(new CellRangeAddress(i, mergeRowTo, ii, ii));
//                    }
//
//
//                    /*overlap next row sequence*/
//                    if (ii + 1 == cellData.length) {
//                        i = mergeRowTo;
//                    }
//                }else{
//                    String ruangRapat = layouts.get(0).getRuangan().getNamaRuangan();
//                    String gedung = layouts.get(0).getRuangan().getNamaGedung();
//                    Integer lantai = Integer.parseInt(layouts.get(0).getRuangan().getLantai());
//
//                    if(columnName.equals("Ruang Rapat")) cell.setCellValue(ruangRapat);
//                    if(columnName.equals("Gedung")) cell.setCellValue(gedung);
//                    if(columnName.equals("Lantai")) cell.setCellValue(lantai);
//                }
//
//                if(cellData[ii] instanceof String){
//                    cell.setCellValue(cellData[ii].toString());
//                }else if(cellData[ii] instanceof Integer){
//                    cell.setCellValue((Integer)cellData[ii]);
//                }else if(cellData[ii] instanceof BigDecimal){
//                    BigDecimal bd = (BigDecimal) cellData[ii];
//                    Double db = bd.doubleValue();
//                    cell.setCellValue(db);
//                }
//            }
//        }
//
//        File file = new File("./report");
//        if(!file.exists()) file.mkdir();
//        FileOutputStream fos = new FileOutputStream("./report/realisasi-biaya-konsumsi.xls");
////        response.addHeader("Content-Disposition", "attachment; filename=Report Realisais Biaya Konsumsi.xls");
//        workbook.write(fos);
//        workbook.close();
//        fos.close();
//    }

    public void exportExcel(List<ViewTblRealisasiBiayaKonsumsi> data, HttpServletResponse response) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        /*set header column*/
        String[] columnHeader = {"No", "Tanggal Permohonan", "Tanggal Acara", "Waktu Mulai", "Waktu Selesai",
                "Divisi / Department / Bidang", "Nama Rapat", "Ruang Rapat", "Gedung", "Lantai",
                "Jumlah Peserta Internal", "Jumlah Peserta External", "Keterangan Peserta External",
                "Jumlah Konsumsi yang Disetujui", "Snack Pagi (Nama Vendor)", "Rupiah Konsumsi Pagi" ,"Snack Siang (Nama Vendor)",
                "Rupiah Konsumsi Siang", "Snack Sore (Nama Vendor)", "Rupiah Konsumsi Sore", "Peralatan Bantu", "Jumlah Rupiah Konsumsi"};


        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setBold(true);

        XSSFCellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setFont(fontHeader);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        DataFormat datafrmt = workbook.createDataFormat();

        XSSFCellStyle styleCurrency = workbook.createCellStyle();
        styleCurrency.setBorderBottom(BorderStyle.THIN);
        styleCurrency.setBorderTop(BorderStyle.THIN);
        styleCurrency.setBorderLeft(BorderStyle.THIN);
        styleCurrency.setBorderRight(BorderStyle.THIN);
        styleCurrency.setVerticalAlignment(VerticalAlignment.CENTER);
        styleCurrency.setDataFormat(datafrmt.getFormat("Rp #,##0.00"));

        /*create header row*/
        Row rowHeader = sheet.createRow(0);
        for(int ic = 0;ic < columnHeader.length;ic++){
            Cell cellHeader = rowHeader.createCell(ic);
            cellHeader.setCellValue(columnHeader[ic]);
            cellHeader.setCellStyle(styleHeader);
        }


        int i = 0;
        int no = 0;
        for(ViewTblRealisasiBiayaKonsumsi item : data){
//            System.out.println(item.getId());
            i++;
            no++;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

            Date tglPemrmohonan = item.getTanggalPermohonan();
            Date tglAcara = item.getTanggalAcara();
            Date waktuMulai = item.getReservasi().getJadwalMulai();
            Date waktuSelesai = item.getReservasi().getJadwalSelesai();
            String tglPermohonanFmt = dateFormat.format(tglPemrmohonan);
            String tglAcaraFmt = dateFormat.format(tglAcara);
            String waktuMulaiFmt = timeFormat.format(waktuMulai);
            String waktuSelesaiFmt = timeFormat.format(waktuSelesai);
            String namaRapat = item.getReservasi().getNamaRapat();
            Integer jumlahInternal = item.getReservasi().getJumlahInternal();
            Integer jumlahExternal = item.getReservasi().getJumlahExternal();
            String ketPeserta = item.getReservasi().getKeteranganPeserta();
            Integer jumlahKonsumsi = item.getJumlahKonsumsiDisetujui();
            String snackPagi = (item.getSnackPagi() != null) ? item.getSnackPagi().getNamaVendor() : "";
            String snackSore = (item.getSnackSore() != null) ? item.getSnackSore().getNamaVendor() : "";
            String snackSiang = (item.getSnackSiang() != null) ? item.getSnackSiang().getNamaVendor() : "";
            BigDecimal sumRupiahKonsumsi = item.getSumRupiahKonsumsi();
            BigDecimal rupiahKonsumsiPagi = item.getRupiahKonsumsiPagi();
            BigDecimal rupiahKonsumsiSiang = item.getRupiahKonsumsiSiang();
            BigDecimal rupiahKonsumsiSore = item.getRupiahKonsumsiSore();

            String department = item.getReservasi().getCreatedByData().getDepartment();
            String bidang = item.getReservasi().getCreatedByData().getOrganization();
            String bidDep = (bidang != null) ? bidang : (department != null) ? department : "";

            /*get data layouts*/
            List<TblDetailReservasiLayoutView> layouts = item.getReservasi().getLayouts();
            int sizeLayouts = layouts.size();
            int mergeRowTo = i + (layouts.size() - 1);


            String fas = "";
            List<TblDetailReservasiFasilitas> fasilitas = item.getReservasi().getFasilitas();
            for(int ifas = 0;ifas < fasilitas.size();ifas++){
                TblDetailReservasiFasilitas itemFas = fasilitas.get(ifas);
                String namaFas = itemFas.getDataFasilitas().getNama();
                String countFas = String.valueOf(itemFas.getJumlah());
                String keteranganFas = namaFas+"("+countFas+")";
                keteranganFas = (ifas + 1 == fasilitas.size()) ? keteranganFas : keteranganFas+", ";
                fas += keteranganFas;
            }

            Object[] cellData = {no, tglPermohonanFmt, tglAcaraFmt, waktuMulaiFmt, waktuSelesaiFmt, bidDep, namaRapat, layouts,
                    layouts, layouts, jumlahInternal, jumlahExternal, ketPeserta, jumlahKonsumsi, snackPagi, rupiahKonsumsiPagi,
                    snackSiang, rupiahKonsumsiSiang, snackSore, rupiahKonsumsiSore, fas, sumRupiahKonsumsi};


            /*create data row*/
            Row rowData = sheet.createRow(i);

            /*create row for ruangan rapat, gedung needs*/
            int rowUnmerged = i;
            List<Row> rowsUnmerged = new ArrayList<>();
            if(layouts.size() > 1){
                for(int ir = 1;ir<layouts.size();ir++){
                    i++; /*append new row for data gedung, ruangan dan lantai*/
                    Row rowRapat = sheet.createRow(i);
                    rowsUnmerged.add(rowRapat);
                }
            }

            for(int ii = 0;ii<cellData.length;ii++){
                Cell cell =  rowData.createCell(ii);
                cell.setCellStyle(style); /*set border data cell*/
                String columnName = columnHeader[ii];

                if(columnName.equals("Rupiah Konsumsi")){
                    cell.setCellStyle(styleCurrency);
                }

                if(layouts.size() > 1){
                    for(int ir = 0;ir<rowsUnmerged.size();ir++) {
                        Row r = rowsUnmerged.get(ir);
                        Cell cellRapat = r.createCell(ii);
                        cellRapat.setCellStyle(style);

                        if (columnName.equals("Ruang Rapat") || columnName.equals("Gedung") || columnName.equals("Lantai")) {
                            int seqDataLayouts = ir + 1;
                            String ruangRapat = layouts.get(seqDataLayouts).getRuangan().getNamaRuangan();
                            String gedung = layouts.get(seqDataLayouts).getRuangan().getNamaGedung();
                            Integer lantai = Integer.parseInt(layouts.get(seqDataLayouts).getRuangan().getLantai());

                            if (columnName.equals("Ruang Rapat")) cellRapat.setCellValue(ruangRapat);
                            if (columnName.equals("Gedung")) cellRapat.setCellValue(gedung);
                            if (columnName.equals("Lantai")) cellRapat.setCellValue(lantai);
                        }else {
                            sheet.addMergedRegion(new CellRangeAddress(rowUnmerged, mergeRowTo, ii, ii));
                        }
                    }
                }

                String ruangRapat = layouts.get(0).getRuangan().getNamaRuangan();
                String gedung = layouts.get(0).getRuangan().getNamaGedung();
                Integer lantai = Integer.parseInt(layouts.get(0).getRuangan().getLantai());

                if(columnName.equals("Ruang Rapat")) cell.setCellValue(ruangRapat);
                if(columnName.equals("Gedung")) cell.setCellValue(gedung);
                if(columnName.equals("Lantai")) cell.setCellValue(lantai);

                if(cellData[ii] instanceof String){
                    cell.setCellValue(cellData[ii].toString());
                }else if(cellData[ii] instanceof Integer){
                    cell.setCellValue((Integer)cellData[ii]);
                }else if(cellData[ii] instanceof BigDecimal){
                    BigDecimal bd = (BigDecimal) cellData[ii];
                    Double db = bd.doubleValue();
                    cell.setCellValue(db);
                }
            }
        }

        File file = new File("./report");
        if(!file.exists()) file.mkdir();
        response.addHeader("Content-Disposition", "attachment; filename=Report Realisais Biaya Konsumsi.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
