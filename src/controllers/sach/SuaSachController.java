package controllers.sach;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Sach;
import services.SachService;

import java.sql.SQLException;

public class SuaSachController {

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
    void sua(ActionEvent event) throws SQLException {
        Sach sachModel = new Sach(Integer.valueOf(textMaSach.getText()), textTenSach.getText(), textTacGia.getText(),
                                    textTheLoai.getText(), textNXB.getText(),textPhongMuon.getText());
        SachService sachService = new SachService();
        sachService.editListSach(sachModel);
    }

    public void initializeTextField(Sach sachModel){
        textMaSach.setText(String.valueOf(sachModel.getMaSach()));
        textTenSach.setText(sachModel.getTenSach());
        textTacGia.setText(sachModel.getTacGia());
        textTheLoai.setText(sachModel.getTheLoai());
        textNXB.setText(sachModel.getNXB());
        textPhongMuon.setText(sachModel.getPhongMuon());

        textMaSach.setEditable(false);
    }
}