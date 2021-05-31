package controllers.docgia;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.DocGia;
import services.DocGiaService;

import java.sql.SQLException;

public class ThemDocGiaController {

    @FXML
    private TextField textTenDocGia;

    @FXML
    private TextField textSDT;

    @FXML
    private TextField textDiaChi;

    @FXML
    private TextField textSoThe;

    @FXML
    void them(ActionEvent event) throws SQLException {
        DocGia docGiaModel = new DocGia(Integer.valueOf(textSoThe.getText()), textSDT.getText(), textDiaChi.getText(), textTenDocGia.getText());
        DocGiaService docGiaService = new DocGiaService();
        docGiaService.addListDocGia(docGiaModel);
    }

    @FXML
    void xoaText(ActionEvent event) {
        textTenDocGia.clear();
        textSDT.clear();
        textSDT.clear();
        textDiaChi.clear();
        textSoThe.clear();
    }

}
