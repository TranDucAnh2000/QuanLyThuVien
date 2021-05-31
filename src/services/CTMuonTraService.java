package services;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import models.CTMuonTra;
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

public class CTMuonTraService {
    public static int maMuonTra;

    public List<CTMuonTra> getListCTMuonTra() {
        List<CTMuonTra> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from CTMuonTra\n"+
                                                        "where MaMuonTra = "+maMuonTra, conn);

            while (rs.next()) {
                CTMuonTra ctMuonTraModel = new CTMuonTra();
                ctMuonTraModel.setMaMuonTra(rs.getInt("MaMuonTra"));
                ctMuonTraModel.setMaSach(rs.getInt("MaSach"));
                ctMuonTraModel.setNgayMuon(rs.getDate("NgayMuon"));
                ctMuonTraModel.setNgayHenTra(rs.getDate("NgayHenTra"));
                ctMuonTraModel.setNgayTra(rs.getDate("NgayTra"));
                ctMuonTraModel.setTienPhat(rs.getInt("TienPhat"));
                list.add(ctMuonTraModel);
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public void tinhTienPhat(CTMuonTra ctMuonTraModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Update CTMuonTra set TienPhat = 1000 * DATEDIFF(day, ?, ?)\n"+
                        "where MaMuonTra = ? and MaSach = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1,String.valueOf(ctMuonTraModel.getNgayHenTra()));
        pst.setString(2,String.valueOf(ctMuonTraModel.getNgayTra()));
        pst.setString(3,String.valueOf(ctMuonTraModel.getMaMuonTra()));
        pst.setString(4,String.valueOf(ctMuonTraModel.getMaSach()));

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void deleteListCTMuonTra(int maSach) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Delete from CTMuonTra where MaSach = ? and MaMuonTra = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, String.valueOf(maSach));
        pst.setString(2, String.valueOf(maMuonTra));

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void addListCTMuonTra(CTMuonTra ctMuonTraModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Insert into CTMuonTra(MaMuonTra, MaSach, NgayMuon, NgayHenTra, NgayTra) Values(?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, ctMuonTraModel.getMaMuonTra());
        pst.setInt(2, ctMuonTraModel.getMaSach());
        pst.setDate(3, ctMuonTraModel.getNgayMuon());
        pst.setDate(4, ctMuonTraModel.getNgayHenTra());
        pst.setDate(5, ctMuonTraModel.getNgayTra());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void editListCTMuonTra(CTMuonTra ctMuonTraModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Update CTMuonTra set NgayMuon = ?, NgayHenTra = ?, NgayTra = ?\n" +
                "where MaSach = ? and MaMuonTra = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setDate(1, ctMuonTraModel.getNgayMuon());
        pst.setDate(2, ctMuonTraModel.getNgayHenTra());
        pst.setDate(3, ctMuonTraModel.getNgayTra());
        pst.setInt(4, ctMuonTraModel.getMaSach());
        pst.setInt(5, ctMuonTraModel.getMaMuonTra());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void exportExcelCTMuonTra(String nguoiDung) throws SQLException, IOException {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Connection conn = DBConnection.getConnection();
        String sql = "select * from CTMuonTra";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("CTMuonTra details");
        XSSFRow tenBang = sheet.createRow(0);
        tenBang.createCell(0).setCellValue("Tên bảng");
        tenBang.createCell(1).setCellValue("Chi tiết Mượn trả");
        XSSFRow taiKhoanThem = sheet.createRow(1);
        taiKhoanThem.createCell(0).setCellValue("Người dùng");
        taiKhoanThem.createCell(1).setCellValue(nguoiDung);
        XSSFRow thoiGian = sheet.createRow(2);
        thoiGian.createCell(0).setCellValue("Thời gian xuất");
        thoiGian.createCell(1).setCellValue(time.format(now));

        XSSFRow header = sheet.createRow(4);
        header.createCell(0).setCellValue("MaMuonTra");
        header.createCell(1).setCellValue("MaSach");
        header.createCell(2).setCellValue("NgayMuon");
        header.createCell(3).setCellValue("NgayHenTra");
        header.createCell(4).setCellValue("NgayTra");
        header.createCell(5).setCellValue("TienPhat");

        int index = 5;
        while(rs.next()){
            XSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(rs.getInt("MaMuonTra"));
            row.createCell(1).setCellValue(rs.getInt("MaSach"));
            row.createCell(2).setCellValue(rs.getDate("NgayMuon"));
            row.createCell(3).setCellValue(rs.getDate("NgayHenTra"));
            row.createCell(4).setCellValue(rs.getDate("NgayTra"));
            row.createCell(5).setCellValue(rs.getInt("TienPhat"));
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

    public void importExcelCTMuonTra(String tenFile) throws SQLException, IOException {
        String sql = "Insert into CTMuonTra(MaNhanVien, MaSach, NgayMuon, NgayHenTra, NgayTra) values(?, ?, ?, ?, ?);";
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
                pst.setDate(3, (Date) row.getCell(2).getDateCellValue());
                pst.setDate(4, (Date) row.getCell(3).getDateCellValue());
                pst.setDate(5, (Date) row.getCell(4).getDateCellValue());
                pst.setInt(6, (int) row.getCell(5).getNumericCellValue());
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
