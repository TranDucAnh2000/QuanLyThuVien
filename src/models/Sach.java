package models;

public class Sach {
    private int maSach;
    private String tenSach;
    private String tacGia;
    private String theLoai;
    private String NXB;
    private String phongMuon;

    public Sach(){

    }

    public Sach(int maSach, String tenSach, String tacGia, String theLoai, String NXB, String phongMuon){
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.theLoai = theLoai;
        this.NXB = NXB;
        this.phongMuon = phongMuon;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getNXB() {
        return NXB;
    }

    public void setNXB(String NXB) {
        this.NXB = NXB;
    }

    public String getPhongMuon() {
        return phongMuon;
    }

    public void setPhongMuon(String phongMuon) {
        this.phongMuon = phongMuon;
    }
}
