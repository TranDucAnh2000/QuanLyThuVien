package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import views.Main;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    void handleSach(ActionEvent event) {
        Pane view = getPage("sach/Sach");
        mainBorderPane.setCenter(view);
    }

    @FXML
    void handleDocGia(ActionEvent event) {
        Pane view = getPage("docgia/DocGia");
        mainBorderPane.setCenter(view);
    }

    @FXML
    void handleNhanVien(ActionEvent event) {
        Pane view = getPage("nhanvien/NhanVien");
        mainBorderPane.setCenter(view);
    }

    @FXML
    void handleMuonTra(ActionEvent event) {
        Pane view = getPage("muontra/MuonTra");
        mainBorderPane.setCenter(view);
    }

    @FXML
    void handleThongKe(ActionEvent event) {
        Pane view = getPage("thongke/ThongKe");
        mainBorderPane.setCenter(view);
    }

    public Pane getPage(String fileName){
        Pane view = new Pane();
        try {
            URL fileUrl = Main.class.getResource("/views/"+ fileName + ".fxml");
            if(fileName == null){
                throw new java.io.FileNotFoundException("FXML file can't be found");
            }
            view = new FXMLLoader().load(fileUrl);
        } catch (Exception e) {
            System.out.println("No page " +fileName+ " please check controllers files");
        }
        return view;
    }
}
