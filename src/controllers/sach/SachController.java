package controllers.sach;

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
import models.Sach;
import services.SachService;
import views.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SachController implements Initializable {

    @FXML
    private TableView<Sach> tableSach;

    @FXML
    private TableColumn<Sach, Integer> col_MaSach;

    @FXML
    private TableColumn<Sach, String> col_TenSach;

    @FXML
    private TableColumn<Sach, String> col_TacGia;

    @FXML
    private TableColumn<Sach, String> col_TheLoai;

    @FXML
    private TableColumn<Sach, String> col_NXB;

    @FXML
    private TableColumn<Sach, String> col_Phong;

    @FXML
    private TextField textSearch;

    @FXML
    private TextField tenFile;

    @FXML
    private ComboBox<String> comboBoxSach;

    private SachService sachService;

    private ObservableList<String> comboBoxOblist;
    private ObservableList<Sach> tableOblist;

    private Stage themSachStage = new Stage();
    private Stage suaSachStage = new Stage();

    private String colSelected;

    public SachController(){
        sachService = new SachService();
        comboBoxOblist = FXCollections.observableArrayList("Mã sách", "Tên sách", "Tác giả", "Thể loại", "NXB", "Phòng mượn");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxSach.setItems(comboBoxOblist);

        col_MaSach.setCellValueFactory(new PropertyValueFactory<>("MaSach"));
        col_TenSach.setCellValueFactory(new PropertyValueFactory<>("TenSach"));
        col_TacGia.setCellValueFactory(new PropertyValueFactory<>("TacGia"));
        col_TheLoai.setCellValueFactory(new PropertyValueFactory<>("TheLoai"));
        col_NXB.setCellValueFactory(new PropertyValueFactory<>("NXB"));
        col_Phong.setCellValueFactory(new PropertyValueFactory<>("PhongMuon"));

        List<Sach> list = sachService.getListSach();
        tableOblist = FXCollections.observableArrayList(list);
        tableSach.setItems(tableOblist);
    }

    @FXML
    void comboBoxSelect(ActionEvent event){
        String temp = comboBoxSach.getSelectionModel().getSelectedItem().toString();
        switch (temp){
            case "Mã sách": colSelected = "MaSach"; break;
            case "Tên sách": colSelected = "TenSach"; break;
            case "Tác giả": colSelected = "TacGia"; break;
            case "Thể loại": colSelected = "TheLoai"; break;
            case "NXB": colSelected = "NXB"; break;
            case "Phòng mượn": colSelected = "PhongMuon"; break;
        }
    }

    @FXML
    void sua(ActionEvent event) throws IOException {
        Sach sachModel = tableSach.getSelectionModel().getSelectedItem();
        //canh bao
        if(sachModel == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Chọn 1 quyển sách trước đã!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("sach/SuaSach.fxml"));
        Parent parent = loader.load();

        SuaSachController suaSachController = loader.getController();
        suaSachController.initializeTextField(sachModel);

        suaSachStage.setScene(new Scene(parent));
        suaSachStage.setTitle("Sửa sách");
        suaSachStage.show();

        suaSachStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void them(ActionEvent event) throws IOException {
        themSachStage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("sach/ThemSach.fxml"))));
        themSachStage.setTitle("Thêm sách");
        themSachStage.show();

        themSachStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void timKiem(ActionEvent event) {
        tableSach.getItems().clear();
        String key = textSearch.getText();

        List<Sach> list = sachService.searchListSach(colSelected, key);
        tableOblist = FXCollections.observableArrayList(list);
        tableSach.setItems(tableOblist);
    }

    @FXML
    void xoa(ActionEvent event) throws SQLException {
        Sach sachModel = tableSach.getSelectionModel().getSelectedItem();
        //canh bao
        if(sachModel == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Chọn 1 quyển sách đi bạn!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            int maSach = sachModel.getMaSach();
            sachService.deleteListSach(maSach);
            tableSach.getItems().removeAll(tableSach.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void xuatFileExcel(ActionEvent event) throws IOException, SQLException {
        sachService.exportExcelSach(tenFile.getText());
    }

    @FXML
    void nhapFileExcel(ActionEvent event) throws IOException, SQLException {
            sachService.importExcelSach(tenFile.getText());
            updateTable();
    }

    private void updateTable(){
        col_MaSach.setCellValueFactory(new PropertyValueFactory<>("MaSach"));
        col_TenSach.setCellValueFactory(new PropertyValueFactory<>("TenSach"));
        col_TacGia.setCellValueFactory(new PropertyValueFactory<>("TacGia"));
        col_TheLoai.setCellValueFactory(new PropertyValueFactory<>("TheLoai"));
        col_NXB.setCellValueFactory(new PropertyValueFactory<>("NXB"));
        col_Phong.setCellValueFactory(new PropertyValueFactory<>("PhongMuon"));

        List<Sach> list = sachService.getListSach();
        tableOblist = FXCollections.observableArrayList(list);
        tableSach.setItems(tableOblist);
    }
}
