package dev.iconpln.mims.data.local.database;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "TPEMERIKSAAN_DETAIL".
 */
@Entity
public class TPemeriksaanDetail {

    @Id
    private Long id;
    private String NoPemeriksaan;
    private String Sn;
    private String NoDoSmar;
    private String NoMaterail;
    private String NoPackaging;
    private String Kategori;
    private String StatusSn;
    private Integer IsChecked;
    private Integer IsDone;

    @Generated
    public TPemeriksaanDetail() {
    }

    public TPemeriksaanDetail(Long id) {
        this.id = id;
    }

    @Generated
    public TPemeriksaanDetail(Long id, String NoPemeriksaan, String Sn, String NoDoSmar, String NoMaterail, String NoPackaging, String Kategori, String StatusSn, Integer IsChecked, Integer IsDone) {
        this.id = id;
        this.NoPemeriksaan = NoPemeriksaan;
        this.Sn = Sn;
        this.NoDoSmar = NoDoSmar;
        this.NoMaterail = NoMaterail;
        this.NoPackaging = NoPackaging;
        this.Kategori = Kategori;
        this.StatusSn = StatusSn;
        this.IsChecked = IsChecked;
        this.IsDone = IsDone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoPemeriksaan() {
        return NoPemeriksaan;
    }

    public void setNoPemeriksaan(String NoPemeriksaan) {
        this.NoPemeriksaan = NoPemeriksaan;
    }

    public String getSn() {
        return Sn;
    }

    public void setSn(String Sn) {
        this.Sn = Sn;
    }

    public String getNoDoSmar() {
        return NoDoSmar;
    }

    public void setNoDoSmar(String NoDoSmar) {
        this.NoDoSmar = NoDoSmar;
    }

    public String getNoMaterail() {
        return NoMaterail;
    }

    public void setNoMaterail(String NoMaterail) {
        this.NoMaterail = NoMaterail;
    }

    public String getNoPackaging() {
        return NoPackaging;
    }

    public void setNoPackaging(String NoPackaging) {
        this.NoPackaging = NoPackaging;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String Kategori) {
        this.Kategori = Kategori;
    }

    public String getStatusSn() {
        return StatusSn;
    }

    public void setStatusSn(String StatusSn) {
        this.StatusSn = StatusSn;
    }

    public Integer getIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(Integer IsChecked) {
        this.IsChecked = IsChecked;
    }

    public Integer getIsDone() {
        return IsDone;
    }

    public void setIsDone(Integer IsDone) {
        this.IsDone = IsDone;
    }

}