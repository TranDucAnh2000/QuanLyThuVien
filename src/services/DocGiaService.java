package services;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import models.DocGia;
import models.Sach;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import views.Main;

import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DocGiaService {
    public List<DocGia> getListDocGia() {
        List<DocGia> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from DocGia", conn);

            while (rs.next()) {
                DocGia docGiaModel = new DocGia();
                docGiaModel.setSoThe(rs.getInt("SoThe"));
                docGiaModel.setTenDocGia(rs.getString("TenDocGia"));
                docGiaModel.setSDT(rs.getString("SDT"));
                docGiaModel.setDiaChi(rs.getString("DiaChi"));
                list.add(docGiaModel);
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    public List<DocGia> searchListDocGia(String colSelected, String key) {
        List<DocGia> list = new ArrayList<>();
        try {

            Connection conn = DBConnection.getConnection();
            ResultSet rs = DBConnection.getData("select * from DocGia where " + colSelected + " LIKE " + "N'%" + key + "%'", conn);

            while(rs.next()){
                DocGia docGiaModel = new DocGia();
                docGiaModel.setTenDocGia(rs.getString("TenDocGia"));
                docGiaModel.setSDT(rs.getString("SDT"));
                docGiaModel.setDiaChi(rs.getString("DiaChi"));
                docGiaModel.setSoThe(rs.getInt("SoThe"));
                list.add(docGiaModel);
            }

            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public void addListDocGia(DocGia docGiaModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Insert into DocGia(TenDocGia, SDT, DiaChi, SoThe) Values(?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, docGiaModel.getTenDocGia());
        pst.setString(2, docGiaModel.getSDT());
        pst.setString(3, docGiaModel.getDiaChi());
        pst.setInt(4, docGiaModel.getSoThe());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void deleteListDocGia(int soThe) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Delete from DocGia where SoThe = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, String.valueOf(soThe));

        pst.execute();

        conn.close();
        pst.close();
    }

    public void editListDocGia(DocGia docGiaModel) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "Update DocGia set TenDocGia = ?, SDT = ?, DiaChi = ?\n" +
                "where SoThe = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, docGiaModel.getTenDocGia());
        pst.setString(2, docGiaModel.getSDT());
        pst.setString(3, docGiaModel.getDiaChi());
        pst.setInt(4, docGiaModel.getSoThe());

        pst.executeUpdate();

        conn.close();
        pst.close();
    }

    public void exportExcelDocGia(String nguoiDung) throws SQLException, IOException {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Connection conn = DBConnection.getConnection();
        String sql = "select * from DocGia";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("DocGia details");
        XSSFRow tenBang = sheet.createRow(0);
        tenBang.createCell(0).setCellValue("Tên bảng");
        tenBang.createCell(1).setCellValue("Độc giả");
        XSSFRow taiKhoanThem = sheet.createRow(1);
        taiKhoanThem.createCell(0).setCellValue("Người dùng");
        taiKhoanThem.createCell(1).setCellValue(nguoiDung);
        XSSFRow thoiGian = sheet.createRow(2);
        thoiGian.createCell(0).setCellValue("Thời gian xuất");
        thoiGian.createCell(1).setCellValue(time.format(now));

        XSSFRow header = sheet.createRow(5);
        header.createCell(0).setCellValue("TenDocGia");
        header.createCell(1).setCellValue("SDT");
        header.createCell(2).setCellValue("DiaChi");
        header.createCell(3).setCellValue("SoThe");

        int index = 6;
        while(rs.next()){
            XSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(rs.getString("TenDocGia"));
            row.createCell(1).setCellValue(rs.getString("SDT"));
            row.createCell(2).setCellValue(rs.getString("DiaChi"));
            row.createCell(3).setCellValue(rs.getInt("SoThe"));
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

    public void importExcelDocGia() throws SQLException, IOException {
        String sql = "Insert into DocGia(TenDocGia, SDT, DiaChi, SoThe) values(?, ?, ?, ?);";
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
                pst.setString(1, row.getCell(0).getStringCellValue());
                pst.setString(2, row.getCell(1).getStringCellValue());
                pst.setString(3, row.getCell(2).getStringCellValue());
                pst.setInt(4, (int) row.getCell(3).getNumericCellValue());
                pst.execute();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Nhập dữ liệu từ file Excel thành công");
            alert.showAndWait();

            wb.close();
            pst.close();
            fileIn.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Nhập file thất bại!");
            alert.setContentText(null);
            alert.showAndWait();
        }
    }
}
