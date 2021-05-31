package models;

public class MuonTra {
    private int maMuonTra;
    private int soThe;
    private int maNhanVien;

    public MuonTra(){}

    public MuonTra(int maMuonTra, int soThe, int maNhanVien){
        this.maMuonTra = maMuonTra;
        this.soThe = soThe;
        this.maNhanVien = maNhanVien;
    }

    public int getMaMuonTra() {
        return maMuonTra;
    }

    public void setMaMuonTra(int maMuonTra) {
        this.maMuonTra = maMuonTra;
    }

    public int getSoThe() {
        return soThe;
    }

    public void setSoThe(int soThe) {
        this.soThe = soThe;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
}
