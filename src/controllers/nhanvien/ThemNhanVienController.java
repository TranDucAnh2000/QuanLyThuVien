package controllers.nhanvien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.NhanVien;
import services.NhanVienService;

import java.sql.Date;
import java.sql.SQLException;

public class ThemNhanVienController {

    @FXML
    private TextField textMaNhanVien;

    @FXML
    private TextField textHoTen;

    @FXML
    private TextField textNgaySinh;

    @FXML
    private TextField textSDT;

    @FXML
    void them(ActionEvent event) throws SQLException {
        NhanVien nhanVien = new NhanVien(Integer.valueOf(textMaNhanVien.getText()), textHoTen.getText(), Date.valueOf(textNgaySinh.getText()), textSDT.getText());
        NhanVienService nhanVienService = new NhanVienService();
        nhanVienService.addListNhanVien(nhanVien);
    }

    @FXML
    void xoaText(ActionEvent event) {
        textHoTen.clear();
        textNgaySinh.clear();
        textMaNhanVien.clear();
        textSDT.clear();
    }

}
