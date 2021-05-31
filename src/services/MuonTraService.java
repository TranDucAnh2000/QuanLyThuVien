package services;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import models.MuonTra;
import models.Sach;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MuonTraService {
    public List<MuonTra> getListMuonTra() {
        List<MuonTra> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from MuonTra", conn);

            while (rs.next()) {
                MuonTra muonSachModel = new MuonTra();
                muonSachModel.setMaMuonTra(rs.getInt("MaMuonTra"));
                muonSachModel.setSoThe(rs.getInt("SoThe"));
                muonSachModel.setMaNhanVien(rs.getInt("MaNhanVien"));
                list.add(muonSachModel);
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public void addListMuonTra(MuonTra muonTraModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Insert into MuonTra(MaMuonTra, SoThe, MaNhanVien) Values(?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, muonTraModel.getMaMuonTra());
        pst.setInt(2, muonTraModel.getSoThe());
        pst.setInt(3, muonTraModel.getMaNhanVien());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void deleteListMuonTra(int maMuonTra) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Delete from MuonTra where MaMuonTra = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, String.valueOf(maMuonTra));

        pst.execute();

        conn.close();
        pst.close();
    }

    public void exportExcelMuonTra(String nguoiDung, int maMuonTra) throws SQLException, IOException {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Connection conn = DBConnection.getConnection();
        String sql1 = "select * from MuonTra where MaMuonTra = "+maMuonTra;
        PreparedStatement pst1 = conn.prepareStatement(sql1);
        ResultSet rs1 = pst1.executeQuery();

        String sql2 = "select *\n" +
                "from CTMuonTra\n" +
                "where MaMuonTra = "+maMuonTra;
        PreparedStatement pst2 = conn.prepareStatement(sql2);
        ResultSet rs2 = pst2.executeQuery();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Phieu muon tra");
        XSSFRow tenBang = sheet.createRow(0);
        tenBang.createCell(0).setCellValue("Phiếu mượn trả");
        XSSFRow taiKhoanThem = sheet.createRow(1);
        taiKhoanThem.createCell(0).setCellValue("Người dùng");
        taiKhoanThem.createCell(1).setCellValue(nguoiDung);
        XSSFRow thoiGian = sheet.createRow(2);
        thoiGian.createCell(0).setCellValue("Thời gian xuất");
        thoiGian.createCell(1).setCellValue(time.format(now));

        //muontra
        XSSFRow header = sheet.createRow(4);
        header.createCell(0).setCellValue("MaMuonTra");
        header.createCell(1).setCellValue("SoThe");
        header.createCell(2).setCellValue("MaNhanVien");

        XSSFRow row = sheet.createRow(5);
        while(rs1.next()){
            row.createCell(0).setCellValue(rs1.getInt("MaMuonTra"));
            row.createCell(1).setCellValue(rs1.getInt("SoThe"));
            row.createCell(2).setCellValue(rs1.getInt("MaNhanVien"));
        }

        //ct muontra
        XSSFRow header2 = sheet.createRow(7);
        header2.createCell(0).setCellValue("MaMuonTra");
        header2.createCell(1).setCellValue("MaSach");
        header2.createCell(2).setCellValue("NgayMuon");
        header2.createCell(3).setCellValue("NgayHenTra");
        header2.createCell(4).setCellValue("NgayTra");
        header2.createCell(5).setCellValue("TienPhat");

        int index = 8;
        while(rs2.next()){
            XSSFRow row2 = sheet.createRow(index);
            row2.createCell(0).setCellValue(rs2.getInt("MaMuonTra"));
            row2.createCell(1).setCellValue(rs2.getInt("MaSach"));
            row2.createCell(2).setCellValue(rs2.getDate("NgayMuon"));
            row2.createCell(3).setCellValue(rs2.getDate("NgayHenTra"));
            row2.createCell(4).setCellValue(rs2.getDate("NgayTra"));
            row2.createCell(5).setCellValue(rs2.getInt("TienPhat"));
            index++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);

        try {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Output file Excel");
            filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLSX", "*.xlsx"), new FileChooser.ExtensionFilter("All", "*.*"));
            File fileURL = filechooser.showSaveDialog(null);

            FileOutputStream fileOut = new FileOutputStream(fileURL);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Đã xuất file Excel thành công");
            alert.setContentText(null);
            alert.showAndWait();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Xuất file thất bại!");
            alert.setContentText(null);
            alert.showAndWait();
        }

        pst1.close();
        rs1.close();
        pst2.close();
        rs2.close();

    }

    public void importExcelMuonTra(String tenFile) throws SQLException, IOException {
        String sql = "Insert into MuonTra(MaNhanVien, SoThe, MaNhanVien) values(?, ?, ?);";
        Connection conn = DBConnection.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        try {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Open file Excel");
            filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLSX", "*.xlsx"), new FileChooser.ExtensionFilter("All", "*.*"));
            File fileURL = filechooser.showOpenDialog(null);

            FileInputStream fileIn = new FileInputStream(new File(fileURL.getAbsolutePath()));

            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                pst.setInt(1, (int) row.getCell(0).getNumericCellValue());
                pst.setInt(2, (int) row.getCell(1).getNumericCellValue());
                pst.setInt(3, (int) row.getCell(2).getNumericCellValue());
                pst.execute();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Nhập dữ liệu từ file Excel thành công");
            alert.showAndWait();

            wb.close();
            pst.close();
            fileIn.close();
        }catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Nhập file thất bại!");
            alert.setContentText(null);
            alert.showAndWait();
        }
    }
}
