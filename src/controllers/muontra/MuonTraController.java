package controllers.muontra;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.DocGia;
import models.MuonTra;
import models.NhanVien;
import services.CTMuonTraService;
import services.DocGiaService;
import services.MuonTraService;
import services.NhanVienService;
import views.Main;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MuonTraController implements Initializable {

    @FXML
    private TableView<DocGia> tableDocGia;

    @FXML
    private TableColumn<DocGia, Integer> col_SoThe;

    @FXML
    private TableColumn<DocGia, String> col_TenDocGia;

    @FXML
    private TableView<NhanVien> tableNhanVien;

    @FXML
    private TableColumn<NhanVien, Integer> col_MaNhanVien;

    @FXML
    private TableColumn<NhanVien, String> col_TenNhanVien;

    @FXML
    private TextField textMuonTra;

    @FXML
    private TextField tenFile;

    @FXML
    private TableView<MuonTra> tableMuonTra;

    @FXML
    private TableColumn<MuonTra, Integer> col_MaMuonTra;

    @FXML
    private TableColumn<MuonTra, Integer> col_SoTheMT;

    @FXML
    private TableColumn<MuonTra, Integer> col_MaNhanVienMT;

    private DocGiaService docGiaService = new DocGiaService();
    private NhanVienService nhanVienService = new NhanVienService();
    private MuonTraService muonTraService = new MuonTraService();
    private CTMuonTraService ctMuonTraService = new CTMuonTraService();

    private ObservableList<DocGia> tableDocGiaOblist;
    private ObservableList<NhanVien> tableNhanVienOblist;
    private ObservableList<MuonTra> tableMuonTraOblist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_SoThe.setCellValueFactory(new PropertyValueFactory<>("SoThe"));
        col_TenDocGia.setCellValueFactory(new PropertyValueFactory<>("TenDocGia"));
        List<DocGia> list1 = docGiaService.getListDocGia();
        tableDocGiaOblist = FXCollections.observableArrayList(list1);
        tableDocGia.setItems(tableDocGiaOblist);

        col_MaNhanVien.setCellValueFactory(new PropertyValueFactory<>("MaNhanVien"));
        col_TenNhanVien.setCellValueFactory(new PropertyValueFactory<>("HoTen"));
        List<NhanVien> list2 = nhanVienService.getListNhanVien();
        tableNhanVienOblist = FXCollections.observableArrayList(list2);
        tableNhanVien.setItems(tableNhanVienOblist);

        col_MaMuonTra.setCellValueFactory(new PropertyValueFactory<>("MaMuonTra"));
        col_SoTheMT.setCellValueFactory(new PropertyValueFactory<>("SoThe"));
        col_MaNhanVienMT.setCellValueFactory(new PropertyValueFactory<>("MaNhanVien"));
        List<MuonTra> list3 = muonTraService.getListMuonTra();
        tableMuonTraOblist = FXCollections.observableArrayList(list3);
        tableMuonTra.setItems(tableMuonTraOblist);
    }

    @FXML
    void them(ActionEvent event) throws SQLException {
        int soThe = tableDocGia.getSelectionModel().getSelectedItem().getSoThe();
        int maNhanVien = tableNhanVien.getSelectionModel().getSelectedItem().getMaNhanVien();
        MuonTra muonTraModel = new MuonTra(Integer.valueOf(textMuonTra.getText()), soThe, maNhanVien);
        try {
            muonTraService.addListMuonTra(muonTraModel);
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Chọn 1 độc giả, 1 nhân viên và điền mã mượn trả");
            alert.setContentText(null);
            alert.showAndWait();
        }
        updateTable();
    }

    @FXML
    void xoa(ActionEvent event) throws SQLException {
        int maMuonTra = tableMuonTra.getSelectionModel().getSelectedItem().getMaMuonTra();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn chắc muốn xóa?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            muonTraService.deleteListMuonTra(maMuonTra);
            updateTable();
        }
    }

    @FXML
    void handleRow(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2 && tableMuonTra.getSelectionModel().getSelectedItem() != null) {
            int maMuonTra = tableMuonTra.getSelectionModel().getSelectedItem().getMaMuonTra();
            ctMuonTraService.maMuonTra = maMuonTra;
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("muontra/CTMuonTra.fxml"))));
            stage.setTitle("Chi tiết mượn trả");
            stage.show();
        }
    }

    @FXML
    void nhapFile(ActionEvent event) throws IOException, SQLException {
            muonTraService.importExcelMuonTra(tenFile.getText());
            updateTable();
    }

    @FXML
    void xuatFile(ActionEvent event) throws IOException, SQLException {
        muonTraService.exportExcelMuonTra(tenFile.getText(), tableMuonTra.getSelectionModel().getSelectedItem().getMaMuonTra());
    }

    private void updateTable(){
        col_MaMuonTra.setCellValueFactory(new PropertyValueFactory<>("MaMuonTra"));
        col_SoTheMT.setCellValueFactory(new PropertyValueFactory<>("SoThe"));
        col_MaNhanVienMT.setCellValueFactory(new PropertyValueFactory<>("MaNhanVien"));
        List<MuonTra> list3 = muonTraService.getListMuonTra();
        tableMuonTraOblist = FXCollections.observableArrayList(list3);
        tableMuonTra.setItems(tableMuonTraOblist);
    }

}
