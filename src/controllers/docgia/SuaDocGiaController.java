package controllers.docgia;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.DocGia;
import services.DocGiaService;

import javax.print.Doc;
import java.sql.SQLException;

public class SuaDocGiaController {

    @FXML
    private TextField textTenDocGia;

    @FXML
    private TextField textSDT;

    @FXML
    private TextField textDiaChi;

    @FXML
    private TextField textSoThe;

    @FXML
    void sua(ActionEvent event) throws SQLException {
        DocGia docGiaModel = new DocGia(Integer.valueOf(textSoThe.getText()), textTenDocGia.getText(), textSDT.getText(), textDiaChi.getText());
        DocGiaService docGiaService = new DocGiaService();
        docGiaService.editListDocGia(docGiaModel);
    }

    public void initializeTextField(DocGia docGiaModel){
        textTenDocGia.setText(docGiaModel.getTenDocGia());
        textSDT.setText(docGiaModel.getSDT());
        textDiaChi.setText(docGiaModel.getDiaChi());
        textSoThe.setText(String.valueOf(docGiaModel.getSoThe()));

        textSoThe.setEditable(false);
    }

}
