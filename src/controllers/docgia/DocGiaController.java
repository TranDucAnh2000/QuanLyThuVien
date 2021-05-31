package controllers.docgia;

import controllers.sach.SuaSachController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.DocGia;
import models.Sach;
import services.DocGiaService;
import views.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DocGiaController implements Initializable {

    @FXML
    private TableView<DocGia> tableDocGia;

    @FXML
    private TableColumn<DocGia, Integer> col_SoThe;

    @FXML
    private TableColumn<DocGia, String> col_TenDocGia;

    @FXML
    private TableColumn<DocGia, String> col_SDT;

    @FXML
    private TableColumn<DocGia, String> col_DiaChi;

    @FXML
    private TextField textSearch;

    @FXML
    private TextField tenNguoiDung;

    @FXML
    private ComboBox<String> comboBoxDocGia;

    private ObservableList<DocGia> tableOblist;
    private ObservableList<String> comboBoxOblist;

    private DocGiaService docGiaService;

    private Stage suaDGStage = new Stage();
    private Stage themDGStage = new Stage();

    private String colSelected;

    public DocGiaController(){
        docGiaService = new DocGiaService();
        comboBoxOblist = FXCollections.observableArrayList("Số thẻ", "Tên độc giả", "SDT", "Địa chỉ");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxDocGia.setItems(comboBoxOblist);

        col_SoThe.setCellValueFactory(new PropertyValueFactory<>("SoThe"));
        col_TenDocGia.setCellValueFactory(new PropertyValueFactory<>("TenDocGia"));
        col_SDT.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        col_DiaChi.setCellValueFactory(new PropertyValueFactory<>("DiaChi"));

        List<DocGia> list = docGiaService.getListDocGia();
        tableOblist = FXCollections.observableArrayList(list);
        tableDocGia.setItems(tableOblist);
    }

    @FXML
    void comboBoxSelect(ActionEvent event) {
        String temp = comboBoxDocGia.getSelectionModel().getSelectedItem().toString();
        switch (temp){
            case "Số thẻ": colSelected = "SoThe"; break;
            case "Tên độc giả": colSelected = "TenDocGia"; break;
            case "SDT": colSelected = "SDT"; break;
            case "Địa chỉ": colSelected = "DiaChi"; break;
        }

    }

    @FXML
    void sua(ActionEvent event) throws IOException {
        DocGia docGiaModel = tableDocGia.getSelectionModel().getSelectedItem();
        //canh bao
        if(docGiaModel == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Chọn 1 độc giả trước đã!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("docgia/SuaDocGia.fxml"));
        Parent parent = loader.load();

        SuaDocGiaController suaDocGiaController = loader.getController();
        suaDocGiaController.initializeTextField(docGiaModel);

        suaDGStage.setScene(new Scene(parent));
        suaDGStage.setTitle("Sửa độc giả");
        suaDGStage.show();

        suaDGStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void them(ActionEvent event) throws IOException {
        themDGStage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("docgia/ThemDocGia.fxml"))));
        themDGStage.setTitle("Thêm doc gia");
        themDGStage.show();

        themDGStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void timKiem(ActionEvent event) {
        tableDocGia.getItems().clear();
        String key = textSearch.getText();

        List<DocGia> list = docGiaService.searchListDocGia(colSelected, key);
        tableOblist = FXCollections.observableArrayList(list);
        tableDocGia.setItems(tableOblist);
    }

    @FXML
    void xoa(ActionEvent event) throws SQLException {
        DocGia docGiaModel = tableDocGia.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            int soThe = docGiaModel.getSoThe();
            docGiaService.deleteListDocGia(soThe);
            tableDocGia.getItems().removeAll(tableDocGia.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void nhapFileExcel(ActionEvent event) throws IOException, SQLException {
            docGiaService.importExcelDocGia();
            updateTable();
    }

    @FXML
    void xuatFileExcel(ActionEvent event) throws IOException, SQLException {
        docGiaService.exportExcelDocGia(tenNguoiDung.getText());
    }

    private void updateTable(){
        col_SoThe.setCellValueFactory(new PropertyValueFactory<>("SoThe"));
        col_TenDocGia.setCellValueFactory(new PropertyValueFactory<>("TenDocGia"));
        col_SDT.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        col_DiaChi.setCellValueFactory(new PropertyValueFactory<>("DiaChi"));

        List<DocGia> list = docGiaService.getListDocGia();
        tableOblist = FXCollections.observableArrayList(list);
        tableDocGia.setItems(tableOblist);
    }

}
