package models;

public class DocGia {
    private int soThe;
    private String tenDocGia;
    private String SDT;
    private String diaChi;

    public DocGia(){}

    public DocGia(int soThe, String tenDocGia, String SDT, String diaChi){
        this.soThe = soThe;
        this.tenDocGia = tenDocGia;
        this.SDT = SDT;
        this.diaChi = diaChi;
    }

    public int getSoThe() {
        return soThe;
    }

    public void setSoThe(int soThe) {
        this.soThe = soThe;
    }

    public String getTenDocGia() {
        return tenDocGia;
    }

    public void setTenDocGia(String tenDocGia) {
        this.tenDocGia = tenDocGia;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
