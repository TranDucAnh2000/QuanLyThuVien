package models;

import java.sql.Date;

public class CTMuonTra {
    private int maMuonTra;
    private int maSach;
    private Date ngayMuon;
    private Date ngayHenTra;
    private Date ngayTra;
    private int tienPhat;

    public CTMuonTra(){}

    public CTMuonTra(int maMuonTra, int maSach, Date ngayMuon, Date ngayHenTra, Date ngayTra, int tienPhat){
        this.maMuonTra = maMuonTra;
        this.maSach = maSach;
        this.ngayMuon = ngayMuon;
        this.ngayHenTra = ngayHenTra;
        this.ngayTra = ngayTra;
        this.tienPhat = tienPhat;
    }

    public int getMaMuonTra() {
        return maMuonTra;
    }

    public void setMaMuonTra(int maMuonTra) {
        this.maMuonTra = maMuonTra;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public Date getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(Date ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public Date getNgayHenTra() {
        return ngayHenTra;
    }

    public void setNgayHenTra(Date ngayHenTra) {
        this.ngayHenTra = ngayHenTra;
    }

    public Date getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(Date ngayTra) {
        this.ngayTra = ngayTra;
    }

    public int getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(int tienPhat) {
        this.tienPhat = tienPhat;
    }
}
