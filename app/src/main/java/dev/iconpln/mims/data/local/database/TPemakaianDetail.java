package dev.iconpln.mims.data.local.database;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "TPEMAKAIAN_DETAIL".
 */
@Entity
public class TPemakaianDetail {

    @Id
    private Long id;
    private String NoTransaksi;
    private String NomorMaterial;
    private String NamaMaterial;
    private String QtyReservasi;
    private String QtyPemakaian;
    private String QtyPengeluaran;
    private String Unit;
    private String NoMeter;
    private String ValuationType;
    private String Keterangan;

    @Generated
    public TPemakaianDetail() {
    }

    public TPemakaianDetail(Long id) {
        this.id = id;
    }

    @Generated
    public TPemakaianDetail(Long id, String NoTransaksi, String NomorMaterial, String NamaMaterial, String QtyReservasi, String QtyPemakaian, String QtyPengeluaran, String Unit, String NoMeter, String ValuationType, String Keterangan) {
        this.id = id;
        this.NoTransaksi = NoTransaksi;
        this.NomorMaterial = NomorMaterial;
        this.NamaMaterial = NamaMaterial;
        this.QtyReservasi = QtyReservasi;
        this.QtyPemakaian = QtyPemakaian;
        this.QtyPengeluaran = QtyPengeluaran;
        this.Unit = Unit;
        this.NoMeter = NoMeter;
        this.ValuationType = ValuationType;
        this.Keterangan = Keterangan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoTransaksi() {
        return NoTransaksi;
    }

    public void setNoTransaksi(String NoTransaksi) {
        this.NoTransaksi = NoTransaksi;
    }

    public String getNomorMaterial() {
        return NomorMaterial;
    }

    public void setNomorMaterial(String NomorMaterial) {
        this.NomorMaterial = NomorMaterial;
    }

    public String getNamaMaterial() {
        return NamaMaterial;
    }

    public void setNamaMaterial(String NamaMaterial) {
        this.NamaMaterial = NamaMaterial;
    }

    public String getQtyReservasi() {
        return QtyReservasi;
    }

    public void setQtyReservasi(String QtyReservasi) {
        this.QtyReservasi = QtyReservasi;
    }

    public String getQtyPemakaian() {
        return QtyPemakaian;
    }

    public void setQtyPemakaian(String QtyPemakaian) {
        this.QtyPemakaian = QtyPemakaian;
    }

    public String getQtyPengeluaran() {
        return QtyPengeluaran;
    }

    public void setQtyPengeluaran(String QtyPengeluaran) {
        this.QtyPengeluaran = QtyPengeluaran;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public String getNoMeter() {
        return NoMeter;
    }

    public void setNoMeter(String NoMeter) {
        this.NoMeter = NoMeter;
    }

    public String getValuationType() {
        return ValuationType;
    }

    public void setValuationType(String ValuationType) {
        this.ValuationType = ValuationType;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String Keterangan) {
        this.Keterangan = Keterangan;
    }

}