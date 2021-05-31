package controllers.nhanvien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.NhanVien;
import services.NhanVienService;

import java.sql.Date;
import java.sql.SQLException;

public class SuaNhanVienController {

    @FXML
    private TextField textMaNhanVien;

    @FXML
    private TextField textHoTen;

    @FXML
    private TextField textNgaySinh;

    @FXML
    private TextField textSDT;

    @FXML
    void sua(ActionEvent event) throws SQLException {
        NhanVien nhanVien = new NhanVien(Integer.valueOf(textMaNhanVien.getText()), textHoTen.getText(), Date.valueOf(textNgaySinh.getText()), textSDT.getText());
        NhanVienService nhanVienService = new NhanVienService();
        nhanVienService.editListNhanVien(nhanVien);
    }

    public void initializeTextField(NhanVien nhanVien){
        textMaNhanVien.setText(String.valueOf(nhanVien.getMaNhanVien()));
        textHoTen.setText(nhanVien.getHoTen());
        textNgaySinh.setText(String.valueOf(nhanVien.getNgaySinh()));
        textSDT.setText(nhanVien.getSDT());

        textMaNhanVien.setEditable(false);
    }

}
