package services;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import models.DocGia;
import models.NhanVien;
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

public class NhanVienService {
    public List<NhanVien> getListNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from NhanVien", conn);

            while (rs.next()) {
                NhanVien nhanVienModel = new NhanVien();
                nhanVienModel.setMaNhanVien(rs.getInt("MaNhanVien"));
                nhanVienModel.setHoTen(rs.getString("HoTen"));
                nhanVienModel.setNgaySinh(rs.getDate("NgaySinh"));
                nhanVienModel.setSDT(rs.getString("SDT"));
                list.add(nhanVienModel);
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public List<NhanVien> searchListNhanVien(String colSelected, String key) {
        List<NhanVien> list = new ArrayList<>();
        try {

            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from NhanVien where " + colSelected + " LIKE " + "N'%" + key + "%'", conn);

            while(rs.next()){
                NhanVien nhanVienModel = new NhanVien();
                nhanVienModel.setMaNhanVien(rs.getInt("MaNhanVien"));
                nhanVienModel.setHoTen(rs.getString("HoTen"));
                nhanVienModel.setNgaySinh(rs.getDate("NgaySinh"));
                nhanVienModel.setSDT(rs.getString("SDT"));
                list.add(nhanVienModel);
            }

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public void addListNhanVien(NhanVien nhanVienModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Insert into NhanVien(MaNhanVien, HoTen, NgaySinh, SDT) Values(?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, nhanVienModel.getMaNhanVien());
        pst.setString(2, nhanVienModel.getHoTen());
        pst.setDate(3, nhanVienModel.getNgaySinh());
        pst.setString(4, nhanVienModel.getSDT());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void deleteListNhanVien(int maNhanVien) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Delete from NhanVien where MaNhanVien = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, maNhanVien);

        pst.execute();

        conn.close();
        pst.close();
    }

    public void editListNhanVien(NhanVien nhanVienModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Update NhanVien set HoTen = ?, NgaySinh = ?, SDT = ?\n" +
                "where MaNhanVien = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, nhanVienModel.getMaNhanVien());
        pst.setString(2, nhanVienModel.getHoTen());
        pst.setDate(3, nhanVienModel.getNgaySinh());
        pst.setString(4, nhanVienModel.getSDT());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void exportExcelNhanVien(String nguoiDung) throws SQLException, IOException {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Connection conn = DBConnection.getConnection();
        String sql = "select * from NhanVien";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("NhanVien details");
        XSSFRow tenBang = sheet.createRow(0);
        tenBang.createCell(0).setCellValue("Tên bảng");
        tenBang.createCell(1).setCellValue("Nhân viên");
        XSSFRow taiKhoanThem = sheet.createRow(1);
        taiKhoanThem.createCell(0).setCellValue("Người dùng");
        taiKhoanThem.createCell(1).setCellValue(nguoiDung);
        XSSFRow thoiGian = sheet.createRow(2);
        thoiGian.createCell(0).setCellValue("Thời gian xuất");
        thoiGian.createCell(1).setCellValue(time.format(now));

        XSSFRow header = sheet.createRow(4);
        header.createCell(0).setCellValue("MaNhanVien");
        header.createCell(1).setCellValue("HoTen");
        header.createCell(2).setCellValue("NgaySinh");
        header.createCell(3).setCellValue("SDT");

        int index = 5;
        while(rs.next()){
            XSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(rs.getInt("MaNhanVien"));
            row.createCell(1).setCellValue(rs.getString("HoTen"));
            row.createCell(2).setCellValue(rs.getDate("NgaySinh"));
            row.createCell(3).setCellValue(rs.getString("SDT"));
            index++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

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

    public void importExcelNhanVien(String tenFile) throws SQLException, IOException {
        String sql = "Insert into NhanVien(MaNhanVien, HoTen, NgaySinh, SDT) values(?, ?, ?, ?);";
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
                pst.setDate(3, (Date) row.getCell(2).getDateCellValue());
                pst.setString(4, row.getCell(3).getStringCellValue());
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
