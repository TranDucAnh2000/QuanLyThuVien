package controllers.muontra;

import com.microsoft.sqlserver.jdbc.SQLServerException;
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
import models.CTMuonTra;
import services.CTMuonTraService;
import views.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CTMuonTraController implements Initializable {

    @FXML
    private TableView<CTMuonTra> tableCTMuonTra;

    @FXML
    private TableColumn<CTMuonTra, Integer> col_MaMuonTra;

    @FXML
    private TableColumn<CTMuonTra, Integer> col_MaSach;

    @FXML
    private TableColumn<CTMuonTra, Date> col_NgayMuon;

    @FXML
    private TableColumn<CTMuonTra, Date> col_NgayHenTra;

    @FXML
    private TableColumn<CTMuonTra, Date> col_NgayTra;

    @FXML
    private TableColumn<CTMuonTra, Integer> col_TienPhat;

    private CTMuonTraService ctMuonTraService = new CTMuonTraService();

    private ObservableList<CTMuonTra> tableOblist;

    private Stage themCTMTStage = new Stage();
    private Stage suaCTMTStage = new Stage();

    @FXML
    private TextField tenFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_MaMuonTra.setCellValueFactory(new PropertyValueFactory<>("MaMuonTra"));
        col_MaSach.setCellValueFactory(new PropertyValueFactory<>("MaSach"));
        col_NgayMuon.setCellValueFactory(new PropertyValueFactory<>("NgayMuon"));
        col_NgayHenTra.setCellValueFactory(new PropertyValueFactory<>("NgayHenTra"));
        col_NgayTra.setCellValueFactory(new PropertyValueFactory<>("NgayTra"));
        col_TienPhat.setCellValueFactory(new PropertyValueFactory<>("TienPhat"));

        List<CTMuonTra> list = ctMuonTraService.getListCTMuonTra();
        tableOblist = FXCollections.observableArrayList(list);
        tableCTMuonTra.setItems(tableOblist);
    }

    @FXML
    void them(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("muontra/ThemCTMuonTra.fxml"));
        Parent parent = loader.load();

        ThemCTMuonTraController themCTMuonTraController = loader.getController();
        themCTMuonTraController.setTextMaMuonTra(String.valueOf(ctMuonTraService.maMuonTra));

        themCTMTStage.setScene(new Scene(parent));
        themCTMTStage.setTitle("Thêm sách cho 1 phiếu mượn");
        themCTMTStage.show();

        themCTMTStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void sua(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("muontra/SuaCTMuonTra.fxml"));
        Parent parent = loader.load();

        SuaCTMuonTraController suaCTMuonTraController = loader.getController();
        suaCTMuonTraController.initializeTextField(tableCTMuonTra.getSelectionModel().getSelectedItem());

        suaCTMTStage.setScene(new Scene(parent));
        suaCTMTStage.setTitle("Sửa sách cho 1 phiếu mượn");
        suaCTMTStage.show();

        suaCTMTStage.setOnCloseRequest((e)->{
            updateTable();
        });
    }

    @FXML
    void xoa(ActionEvent event) throws SQLException {
        int maSach = tableCTMuonTra.getSelectionModel().getSelectedItem().getMaSach();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            ctMuonTraService.deleteListCTMuonTra(maSach);
            updateTable();
        }
    }

    @FXML
    void tinhTienPhat(ActionEvent event){
        CTMuonTra ctMuonTraModel = tableCTMuonTra.getSelectionModel().getSelectedItem();
        try {
            ctMuonTraService.tinhTienPhat(ctMuonTraModel);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Thiếu ngày phát hoặc ngày hẹn trả");
            alert.setContentText(null);
            alert.showAndWait();
        }
        updateTable();
    }

    @FXML
    void xuatFile(ActionEvent event) throws IOException, SQLException {
        ctMuonTraService.exportExcelCTMuonTra(tenFile.getText());
    }

    @FXML
    void nhapFile(ActionEvent event) throws IOException, SQLException {
        ctMuonTraService.importExcelCTMuonTra(tenFile.getText());
        updateTable();
    }

    private void updateTable(){
        col_MaMuonTra.setCellValueFactory(new PropertyValueFactory<>("MaMuonTra"));
        col_MaSach.setCellValueFactory(new PropertyValueFactory<>("MaSach"));
        col_NgayMuon.setCellValueFactory(new PropertyValueFactory<>("NgayMuon"));
        col_NgayHenTra.setCellValueFactory(new PropertyValueFactory<>("NgayHenTra"));
        col_NgayTra.setCellValueFactory(new PropertyValueFactory<>("NgayTra"));
        col_TienPhat.setCellValueFactory(new PropertyValueFactory<>("TienPhat"));

        List<CTMuonTra> list = ctMuonTraService.getListCTMuonTra();
        tableOblist = FXCollections.observableArrayList(list);
        tableCTMuonTra.setItems(tableOblist);
    }

}
