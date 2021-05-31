package controllers.muontra;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.CTMuonTra;
import services.CTMuonTraService;

import java.sql.Date;
import java.sql.SQLException;

public class ThemCTMuonTraController {

    @FXML
    private TextField textMaMuonTra;

    @FXML
    private TextField textMaSach;

    @FXML
    private TextField textNgayMuon;

    @FXML
    private TextField textNgayHenTra;

    @FXML
    private TextField textNgayTra;

    private CTMuonTraService ctMuonTraService = new CTMuonTraService();

    @FXML
    void them(ActionEvent event) {
        try{
            CTMuonTra ctMuonTraModel = new CTMuonTra();
            ctMuonTraModel.setMaMuonTra(Integer.valueOf(textMaMuonTra.getText()));
            ctMuonTraModel.setMaSach(Integer.valueOf(textMaSach.getText()));
            ctMuonTraModel.setNgayMuon(Date.valueOf(textNgayMuon.getText()));
            ctMuonTraModel.setNgayHenTra(Date.valueOf(textNgayHenTra.getText()));
            ctMuonTraModel.setNgayTra(Date.valueOf(textNgayTra.getText()));

            ctMuonTraService.addListCTMuonTra(ctMuonTraModel);
        }catch (IllegalArgumentException | SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Sai định dạng thông tin");
            alert.setContentText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void xoaText(ActionEvent event) {
        textMaSach.clear();
        textNgayHenTra.clear();
        textNgayMuon.clear();
        textNgayTra.clear();
    }

    public void setTextMaMuonTra(String maMuonTra){
        textMaMuonTra.setText(maMuonTra);
        textMaMuonTra.setEditable(false);
    }

}
