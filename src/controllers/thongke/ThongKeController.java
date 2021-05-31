package controllers.thongke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.DocGia;
import services.DocGiaService;
import services.ThongKeService;

import javax.print.Doc;
import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ThongKeController implements Initializable {

    @FXML
    private ComboBox<String> comboBox1Sach;

    @FXML
    private ComboBox<String> comboBox2Sach;

    @FXML
    private TextField soSach;

    @FXML
    private TextField soDocGia;

    @FXML
    private TextField soNhanVien;

    @FXML
    private TableView<DocGia> tableDocGia;

    @FXML
    private TableColumn<DocGia, Integer> col_SoThe;

    @FXML
    private TableColumn<DocGia, String> col_TenDocGia;

    @FXML
    private TextField textSoSachMuon;

    @FXML
    private TextField textTongTienPhat;

    String tieuChiSach;

    private ObservableList<String> comboBox1SachOblist = FXCollections.observableArrayList("TenSach", "TacGia", "TheLoai", "NXB");
    private ObservableList<String> comboBox2SachOblist;
    private ObservableList<DocGia> docGiaOblist;

    private ThongKeService thongKeService = new ThongKeService();
    private DocGiaService docGiaService = new DocGiaService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox1Sach.setItems(comboBox1SachOblist);
        try {
            soDocGia.setText(String.valueOf(thongKeService.count("SoThe", "", "DocGia")));
            soDocGia.setEditable(false);
            soNhanVien.setText(String.valueOf(thongKeService.count("MaNhanVien", "","NhanVien")));
            soNhanVien.setEditable(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        col_SoThe.setCellValueFactory(new PropertyValueFactory<>("SoThe"));
        col_TenDocGia.setCellValueFactory(new PropertyValueFactory<>("TenDocGia"));
        List<DocGia> list1 = docGiaService.getListDocGia();
        docGiaOblist = FXCollections.observableArrayList(list1);
        tableDocGia.setItems(docGiaOblist);
    }

    @FXML
    void comboBox1Sach(ActionEvent event) throws SQLException {
        tieuChiSach = comboBox1Sach.getSelectionModel().getSelectedItem();
        comboBox2SachOblist = FXCollections.observableArrayList(thongKeService.getListComboBox(tieuChiSach));
        comboBox2SachOblist.add("Tất cả");
        comboBox2Sach.setItems(comboBox2SachOblist);
    }

    @FXML
    void comboBox2Sach(ActionEvent event) throws SQLException {
        String chiTiet;
        if(comboBox2Sach.getSelectionModel().getSelectedItem() == "Tất cả"){
            chiTiet = "";
        }else {
            chiTiet = comboBox2Sach.getSelectionModel().getSelectedItem();
        }
        int result = thongKeService.count(tieuChiSach, chiTiet, "Sach");
        soSach.setText(String.valueOf(result));
        soSach.setEditable(false);
    }

    @FXML
    void tinh(ActionEvent event) throws SQLException {
        int soThe = tableDocGia.getSelectionModel().getSelectedItem().getSoThe();
        textSoSachMuon.setText(String.valueOf(thongKeService.countSachDaMuon(soThe)));
        textSoSachMuon.setEditable(false);
        textTongTienPhat.setText(String.valueOf(thongKeService.countTongTienPhat(soThe)));
        textTongTienPhat.setEditable(false);
    }

}
