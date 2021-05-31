package services;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import models.Sach;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SachService {

    public List<Sach> getListSach() {
        List<Sach> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from Sach", conn);

            while (rs.next()) {
                Sach sachModel = new Sach();
                sachModel.setMaSach(rs.getInt("MaSach"));
                sachModel.setTenSach(rs.getString("TenSach"));
                sachModel.setTacGia(rs.getString("TacGia"));
                sachModel.setTheLoai(rs.getString("TheLoai"));
                sachModel.setNXB(rs.getString("NXB"));
                sachModel.setPhongMuon(rs.getString("PhongMuon"));
                list.add(sachModel);
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public List<Sach> searchListSach(String colSelected, String key) {
        List<Sach> list = new ArrayList<>();
        try {

            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from Sach where " + colSelected + " LIKE " + "N'%" + key + "%'", conn);

            while(rs.next()){
                Sach sachModel = new Sach();
                sachModel.setMaSach(rs.getInt("MaSach"));
                sachModel.setTenSach(rs.getString("TenSach"));
                sachModel.setTacGia(rs.getString("TacGia"));
                sachModel.setTheLoai(rs.getString("TheLoai"));
                sachModel.setNXB(rs.getString("NXB"));
                sachModel.setPhongMuon(rs.getString("PhongMuon"));
                list.add(sachModel);
            }

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public void addListSach(Sach sachModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Insert into Sach(MaSach, TenSach, TacGia,TheLoai, NXB, PhongMuon) Values(?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, sachModel.getMaSach());
        pst.setString(2, sachModel.getTenSach());
        pst.setString(3, sachModel.getTacGia());
        pst.setString(4, sachModel.getTheLoai());
        pst.setString(5, sachModel.getNXB());
        pst.setString(6, sachModel.getPhongMuon());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void deleteListSach(int maSach) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Delete from Sach where MaSach = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, String.valueOf(maSach));

        pst.execute();

        conn.close();
        pst.close();
    }

    public void editListSach(Sach sachModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Update Sach set TenSach = ?, TacGia = ?, TheLoai = ?, NXB = ?, PhongMuon = ?\n" +
                        "where MaSach = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, sachModel.getTenSach());
        pst.setString(2, sachModel.getTacGia());
        pst.setString(3, sachModel.getTheLoai());
        pst.setString(4, sachModel.getNXB());
        pst.setString(5, sachModel.getPhongMuon());
        pst.setString(6, String.valueOf(sachModel.getMaSach()));

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void exportExcelSach(String nguoiDung) throws SQLException, IOException {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Connection conn = DBConnection.getConnection();
        String sql = "select * from Sach";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Sach details");
        XSSFRow tenBang = sheet.createRow(0);
        tenBang.createCell(0).setCellValue("Tên bảng");
        tenBang.createCell(1).setCellValue("Sách");
        XSSFRow taiKhoanThem = sheet.createRow(1);
        taiKhoanThem.createCell(0).setCellValue("Người dùng");
        taiKhoanThem.createCell(1).setCellValue(nguoiDung);
        XSSFRow thoiGian = sheet.createRow(2);
        thoiGian.createCell(0).setCellValue("Thời gian xuất");
        thoiGian.createCell(1).setCellValue(time.format(now));

        XSSFRow header = sheet.createRow(4);
        header.createCell(0).setCellValue("MaSach");
        header.createCell(1).setCellValue("TenSach");
        header.createCell(2).setCellValue("TacGia");
        header.createCell(3).setCellValue("TheLoai");
        header.createCell(4).setCellValue("NXB");
        header.createCell(5).setCellValue("PhongMuon");

        int index = 5;
        while(rs.next()){
            XSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(rs.getInt("MaSach"));
            row.createCell(1).setCellValue(rs.getString("TenSach"));
            row.createCell(2).setCellValue(rs.getString("TacGia"));
            row.createCell(3).setCellValue(rs.getString("TheLoai"));
            row.createCell(4).setCellValue(rs.getString("NXB"));
            row.createCell(5).setCellValue(rs.getString("PhongMuon"));
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

        pst.close();
        rs.close();

    }

    public void importExcelSach(String tenFile) throws SQLException, IOException {
        String sql = "Insert into Sach(MaSach, TenSach, TacGia, TheLoai, NXB, PhongMuon) values(?, ?, ?, ?, ?, ?);";
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
                pst.setString(2, row.getCell(1).getStringCellValue());
                pst.setString(3, row.getCell(2).getStringCellValue());
                pst.setString(4, row.getCell(3).getStringCellValue());
                pst.setString(5, row.getCell(4).getStringCellValue());
                pst.setString(6, row.getCell(5).getStringCellValue());
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
