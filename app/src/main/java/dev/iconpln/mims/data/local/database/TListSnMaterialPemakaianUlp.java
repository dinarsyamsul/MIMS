package dev.iconpln.mims.data.local.database;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "TLIST_SN_MATERIAL_PEMAKAIAN_ULP".
 */
@Entity
public class TListSnMaterialPemakaianUlp {

    @Id
    private Long id;
    private String NoTransaksi;
    private String NoMaterial;
    private String NoSerialNumber;
    private Integer IsScanned;

    @Generated
    public TListSnMaterialPemakaianUlp() {
    }

    public TListSnMaterialPemakaianUlp(Long id) {
        this.id = id;
    }

    @Generated
    public TListSnMaterialPemakaianUlp(Long id, String NoTransaksi, String NoMaterial, String NoSerialNumber, Integer IsScanned) {
        this.id = id;
        this.NoTransaksi = NoTransaksi;
        this.NoMaterial = NoMaterial;
        this.NoSerialNumber = NoSerialNumber;
        this.IsScanned = IsScanned;
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

    public String getNoMaterial() {
        return NoMaterial;
    }

    public void setNoMaterial(String NoMaterial) {
        this.NoMaterial = NoMaterial;
    }

    public String getNoSerialNumber() {
        return NoSerialNumber;
    }

    public void setNoSerialNumber(String NoSerialNumber) {
        this.NoSerialNumber = NoSerialNumber;
    }

    public Integer getIsScanned() {
        return IsScanned;
    }

    public void setIsScanned(Integer IsScanned) {
        this.IsScanned = IsScanned;
    }

}