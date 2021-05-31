package controllers.nhanvien;

import controllers.docgia.SuaDocGiaController;
import javafx.beans.property.Property;
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
import models.NhanVien;
import services.NhanVienService;
import views.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NhanVienController implements Initializable {

    @FXML
    private TableView<NhanVien> tableNhanVien;

    @FXML
    private TableColumn<NhanVien, Integer> col_MaNhanVien;

    @FXML
    private TableColumn<NhanVien, String> col_HoTen;

    @FXML
    private TableColumn<NhanVien, String> col_NgaySinh;

    @FXML
    private TableColumn<NhanVien, String> col_SDT;

    @FXML
    private TextField textSearch;

    @FXML
    private TextField tenFile;

    @FXML
    private ComboBox<String> comboBoxNhanVien;

    private ObservableList<NhanVien> tableOblist;
    private ObservableList<String> comboBoxOblist;

    private NhanVienService nhanVienService;

    private Stage suaNVStage = new Stage();
    private Stage themNVStage = new Stage();

    private String colSelected;

    public NhanVienController(){
        nhanVienService = new NhanVienService();
        comboBoxOblist = FXCollections.observableArrayList("Mã nhân viên", "Họ tên", "Ngày sinh", "SDT");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxNhanVien.setItems(comboBoxOblist);

        col_MaNhanVien.setCellValueFactory(new PropertyValueFactory<>("MaNhanVien"));
        col_HoTen.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        col_NgaySinh.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        col_SDT.setCellValueFactory(new PropertyValueFactory<>("SDT"));

        List<NhanVien> list = nhanVienService.getListNhanVien();
        tableOblist = FXCollections.observableArrayList(list);
        tableNhanVien.setItems(tableOblist);
    }

    @FXML
    void comboBoxSelect(ActionEvent event) {
        String temp = comboBoxNhanVien.getSelectionModel().getSelectedItem().toString();
        switch (temp){
            case "Mã nhân viên": colSelected = "MaNhanVien"; break;
            case "Họ tên": colSelected = "HoTen"; break;
            case "Ngày sinh": colSelected = "NgaySinh"; break;
            case "SDT": colSelected = "SDT"; break;
        }
    }

    @FXML
    void sua(ActionEvent event) throws IOException {
        NhanVien nhanVienModel = tableNhanVien.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("nhanvien/SuaNhanVien.fxml"));
        Parent parent = loader.load();

        SuaNhanVienController suaNhanVienController = loader.getController();
        suaNhanVienController.initializeTextField(nhanVienModel);

        suaNVStage.setScene(new Scene(parent));
        suaNVStage.setTitle("Sửa nhan vien");
        suaNVStage.show();

        suaNVStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void them(ActionEvent event) throws IOException {
        themNVStage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("nhanvien/ThemNhanVien.fxml"))));
        themNVStage.setTitle("Thêm nhan vien");
        themNVStage.show();

        themNVStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void timKiem(ActionEvent event) {
        tableNhanVien.getItems().clear();
        String key = textSearch.getText();

        List<NhanVien> list = nhanVienService.searchListNhanVien(colSelected, key);
        tableOblist = FXCollections.observableArrayList(list);
        tableNhanVien.setItems(tableOblist);
    }

    @FXML
    void xoa(ActionEvent event) throws SQLException {
        NhanVien nhanVienModel = tableNhanVien.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            int maNhanVien = nhanVienModel.getMaNhanVien();
            nhanVienService.deleteListNhanVien(maNhanVien);
            tableNhanVien.getItems().removeAll(tableNhanVien.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void nhapFile(ActionEvent event) throws IOException, SQLException {
            nhanVienService.importExcelNhanVien(tenFile.getText());
            updateTable();
    }

    @FXML
    void xuatFile(ActionEvent event) throws IOException, SQLException {
        nhanVienService.exportExcelNhanVien(tenFile.getText());
    }

    private void updateTable(){
        col_MaNhanVien.setCellValueFactory(new PropertyValueFactory<>("MaNhanVien"));
        col_HoTen.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        col_NgaySinh.setCellValueFactory(new PropertyValueFactory<>("NgaySinh"));
        col_SDT.setCellValueFactory(new PropertyValueFactory<>("SDT"));

        List<NhanVien> list = nhanVienService.getListNhanVien();
        tableOblist = FXCollections.observableArrayList(list);
        tableNhanVien.setItems(tableOblist);
    }

}
