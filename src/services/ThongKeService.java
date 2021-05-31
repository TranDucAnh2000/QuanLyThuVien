package services;

import models.Sach;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThongKeService {

    public List<String> getListComboBox(String tieuChi) throws SQLException {
        List<String> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "Select distinct "+tieuChi +" from Sach";
        ResultSet rs = DBConnection.getData(sql, conn);
        while(rs.next()){
            String temp = rs.getString(tieuChi);
            list.add(temp);
        }
        conn.close();
        return list;
    }

    public int count(String tieuChi, String chiTiet, String bang) throws SQLException {
        int result = 0;
        Connection conn = DBConnection.getConnection();
        String sql = "Select count("+tieuChi+") as SoLuong\n"+
                        "from "+bang+"\n"+
                        "where "+tieuChi+" like N'%"+chiTiet+"%'";
        ResultSet rs = DBConnection.getData(sql, conn);
        while(rs.next()){
            result = rs.getInt("SoLuong");
        }

        conn.close();
        return  result;
    }

    public int countSachDaMuon(int soThe) throws SQLException {
        int result = 0;
        Connection conn = DBConnection.getConnection();
        String sql = "select count(MaSach) as SoLuong\n" +
                "from DocGia, MuonTra, CTMuonTra\n" +
                "where DocGia.SoThe = MuonTra.SoThe and MuonTra.MaMuonTra = CTMuonTra.MaMuonTra\n" +
                "\tand DocGia.SoThe = "+soThe;
        ResultSet rs = DBConnection.getData(sql, conn);
        while(rs.next()){
            result = rs.getInt("SoLuong");
        }

        conn.close();
        return  result;
    }

    public int countTongTienPhat(int soThe) throws SQLException {
        int result = 0;
        Connection conn = DBConnection.getConnection();
        String sql = "select sum(TienPhat) as SoLuong\n" +
                "from DocGia, MuonTra, CTMuonTra\n" +
                "where DocGia.SoThe = MuonTra.SoThe and MuonTra.MaMuonTra = CTMuonTra.MaMuonTra\n" +
                "\tand DocGia.SoThe = "+soThe;
        ResultSet rs = DBConnection.getData(sql, conn);
        while(rs.next()){
            result = rs.getInt("SoLuong");
        }

        conn.close();
        return  result;
    }

}
