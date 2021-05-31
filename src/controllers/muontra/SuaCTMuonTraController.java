package controllers.muontra;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.CTMuonTra;
import services.CTMuonTraService;

import java.sql.Date;
import java.sql.SQLException;

public class SuaCTMuonTraController {

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

    CTMuonTraService ctMuonTraService = new CTMuonTraService();

    @FXML
    void sua(ActionEvent event) throws SQLException {
        CTMuonTra ctMuonTra = new CTMuonTra();
        ctMuonTra.setNgayMuon(Date.valueOf(textNgayMuon.getText()));
        ctMuonTra.setNgayHenTra(Date.valueOf(textNgayHenTra.getText()));
        ctMuonTra.setNgayTra(Date.valueOf(textNgayTra.getText()));
        ctMuonTra.setMaMuonTra(Integer.valueOf(textMaMuonTra.getText()));
        ctMuonTra.setMaSach(Integer.valueOf(textMaSach.getText()));

        ctMuonTraService.editListCTMuonTra(ctMuonTra);
    }

    public void initializeTextField(CTMuonTra ctMuonTra){
        textMaMuonTra.setText(String.valueOf(ctMuonTra.getMaMuonTra()));
        textMaSach.setText(String.valueOf(ctMuonTra.getMaSach()));
        textNgayMuon.setText(String.valueOf(ctMuonTra.getNgayMuon()));
        textNgayHenTra.setText(String.valueOf(ctMuonTra.getNgayHenTra()));
        textNgayTra.setText(String.valueOf(ctMuonTra.getNgayTra()));

        textMaMuonTra.setEditable(false);
        textMaSach.setEditable(false);
    }

}
