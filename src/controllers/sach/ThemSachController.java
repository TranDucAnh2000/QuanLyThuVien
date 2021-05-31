package controllers.sach;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Sach;
import services.SachService;

import java.sql.SQLException;

public class ThemSachController {

    @FXML
    private TextField textMaSach;

    @FXML
    private TextField textTenSach;

    @FXML
    private TextField textTacGia;

    @FXML
    private TextField textTheLoai;

    @FXML
    private TextField textNXB;

    @FXML
    private TextField textPhongMuon;

    @FXML
    void them(ActionEvent event) throws SQLException {
        Sach sachModel = new Sach(Integer.valueOf(textMaSach.getText()), textTenSach.getText(), textTacGia.getText(),
                                    textTheLoai.getText(), textNXB.getText(),textPhongMuon.getText());
        SachService sachService = new SachService();
        sachService.addListSach(sachModel);
    }

    @FXML
    void xoaText(ActionEvent event) {
        textMaSach.clear();
        textTenSach.clear();
        textTacGia.clear();
        textTheLoai.clear();
        textNXB.clear();
        textPhongMuon.clear();
    }

}
