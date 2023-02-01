package dev.iconpln.greendaogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MainGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "dev.iconpln.mims.data.local.database");

        //region Province
        Entity tMonitoring = schema.addEntity("TMonitoring");
        tMonitoring.addIdProperty();
        tMonitoring.addStringProperty("NomorMaterial");
        tMonitoring.addStringProperty("KdPabrikan");
        tMonitoring.addStringProperty("MatSapNo");
        tMonitoring.addStringProperty("StoreLoc");
        tMonitoring.addStringProperty("Uom");
        tMonitoring.addStringProperty("Unit");
        tMonitoring.addStringProperty("MaterialDesc");
        tMonitoring.addStringProperty("TlskNo");
        tMonitoring.addStringProperty("PoSapNo");
        tMonitoring.addStringProperty("PoMpNo");
        tMonitoring.addStringProperty("Qty");
        tMonitoring.addStringProperty("LeadTime");
        tMonitoring.addStringProperty("CreatedDate");


        //endregion

        //region Pengujian
        Entity tPengujian = schema.addEntity("TPengujian");
        tPengujian.addIdProperty();
        tPengujian.addStringProperty("TanggalUji");
        tPengujian.addStringProperty("NoPengujian");
        tPengujian.addStringProperty("NamaKategori");
        tPengujian.addStringProperty("QtyMaterial");
        tPengujian.addStringProperty("QtySiap");
        tPengujian.addStringProperty("Unit");
        tPengujian.addStringProperty("StatusUji");
        tPengujian.addStringProperty("KdPabrikan");

        //endregion

        //region Pengujian Detail
        Entity tPengujianDetails = schema.addEntity("TPengujianDetails");
        tPengujianDetails.addIdProperty();
        tPengujianDetails.addStringProperty("SerialNumber");
        tPengujianDetails.addStringProperty("KeteranganMaterial");
        tPengujianDetails.addStringProperty("noPengujian");
        tPengujianDetails.addStringProperty("NamaKategori");
        tPengujianDetails.addStringProperty("StatusUji");

        //endregion

        //region lokasi
        Entity tLokasi = schema.addEntity("TLokasi");
        tLokasi.addIdProperty();
        tLokasi.addStringProperty("NoDoSns");
        tLokasi.addStringProperty("Ket");
        tLokasi.addStringProperty("UpdateDate");

        //endregion

        //region Pos Detail
        Entity tPosDetail = schema.addEntity("TPosDetail");
        tPosDetail.addIdProperty();
        tPosDetail.addStringProperty("NoMatSap");
        tPosDetail.addStringProperty("NoDoSmar");
        tPosDetail.addStringProperty("Qty");
        tPosDetail.addStringProperty("KdPabrikan");
        tPosDetail.addStringProperty("DoStatus");
        tPosDetail.addStringProperty("PoSapNo");
        tPosDetail.addStringProperty("PoMpNo");
        tPosDetail.addStringProperty("NoDoMims");
        tPosDetail.addStringProperty("NoPackaging");
        tPosDetail.addStringProperty("PlantCodeNo");
        tPosDetail.addStringProperty("PlantName");
        tPosDetail.addStringProperty("StorLoc");
        tPosDetail.addStringProperty("LeadTime");
        tPosDetail.addStringProperty("CreatedDate");
        tPosDetail.addStringProperty("Uom");
        tPosDetail.addStringProperty("NoPemeriksaan");

        //endregion

        //region Pos Detail Penerimaan
        Entity tPosDetailPenerimaan = schema.addEntity("TPosDetailPenerimaan");
        tPosDetailPenerimaan.addIdProperty();
        tPosDetailPenerimaan.addStringProperty("NoMatSap");
        tPosDetailPenerimaan.addStringProperty("NoDoSmar");
        tPosDetailPenerimaan.addStringProperty("Qty");
        tPosDetailPenerimaan.addStringProperty("KdPabrikan");
        tPosDetailPenerimaan.addStringProperty("DoStatus");
        tPosDetailPenerimaan.addStringProperty("PoSapNo");
        tPosDetailPenerimaan.addStringProperty("PoMpNo");
        tPosDetailPenerimaan.addStringProperty("NoDoMims");
        tPosDetailPenerimaan.addStringProperty("NoPackaging");
        tPosDetailPenerimaan.addStringProperty("PlantCodeNo");
        tPosDetailPenerimaan.addStringProperty("PlantName");
        tPosDetailPenerimaan.addStringProperty("StorLoc");
        tPosDetailPenerimaan.addStringProperty("LeadTime");
        tPosDetailPenerimaan.addStringProperty("CreatedDate");
        tPosDetailPenerimaan.addStringProperty("Uom");
        tPosDetailPenerimaan.addStringProperty("NoPemeriksaan");
        tPosDetailPenerimaan.addIntProperty("isDone");

        //endregion

        //region Material Detail
        Entity tMaterialDetail = schema.addEntity("TMaterialDetail");
        tMaterialDetail.addIdProperty();
        tMaterialDetail.addStringProperty("NomorMaterial");
        tMaterialDetail.addStringProperty("Mmc");
        tMaterialDetail.addStringProperty("MaterialGroup");
        tMaterialDetail.addStringProperty("TglProduksi");
        tMaterialDetail.addStringProperty("KdPabrikan");
        tMaterialDetail.addStringProperty("serialNumber");
        tMaterialDetail.addStringProperty("NomorSertMaterologi");
        tMaterialDetail.addStringProperty("Spln");
        tMaterialDetail.addStringProperty("NoProduksi");
        tMaterialDetail.addStringProperty("Storloc");
        tMaterialDetail.addStringProperty("NoPackaging");
        tMaterialDetail.addStringProperty("Spesifikasi");
        tMaterialDetail.addStringProperty("Plant");
        tMaterialDetail.addStringProperty("MaterialId");
        tMaterialDetail.addStringProperty("MasaGaransi");
        tMaterialDetail.addStringProperty("Tahun");
        tMaterialDetail.addStringProperty("NamaKategoriMaterial");

        //endregion

        //region Material Groups
        Entity tMaterialGroups = schema.addEntity("TMaterialGroups");
        tMaterialGroups.addIdProperty();
        tMaterialGroups.addStringProperty("MaterialGroup");
        tMaterialGroups.addStringProperty("NamaKategoriMaterial");

        //endregion

        // region privilege
        Entity tPrivelege = schema.addEntity("TPrivilege");
        tPrivelege.addIdProperty();
        tPrivelege.addStringProperty("ModuleId");
        tPrivelege.addStringProperty("IsActive");
        tPrivelege.addStringProperty("RoleId");
        tPrivelege.addStringProperty("MethodId");
        tPrivelege.addStringProperty("MethodValue");
        // endregion

        // region material
        Entity tMaterial = schema.addEntity("TMaterial");
        tMaterial.addIdProperty();
        tMaterial.addStringProperty("NomorMaterial");
        tMaterial.addStringProperty("Mmc");
        tMaterial.addStringProperty("MaterialGroup");
        tMaterial.addStringProperty("TglProduksi");
        tMaterial.addStringProperty("NomorSertMaterologi");
        tMaterial.addStringProperty("Storloc");
        tMaterial.addStringProperty("Plant");
        tMaterial.addStringProperty("MaterialId");
        tMaterial.addStringProperty("MasaGaransi");
        tMaterial.addStringProperty("NoProduksi");
        tMaterial.addStringProperty("Tahun");
        tMaterial.addStringProperty("NamaKategoriMaterial");
        tMaterial.addStringProperty("KdPabrikan");

        // endregion

        // region pos
        Entity tPos = schema.addEntity("TPos");
        tPos.addIdProperty();
        tPos.addStringProperty("StorLoc");
        tPos.addStringProperty("Total");
        tPos.addStringProperty("TlskNo");
        tPos.addStringProperty("PoSapNo");
        tPos.addStringProperty("PoMpNo");
        tPos.addStringProperty("NoDoSmar");
        tPos.addIntProperty("LeadTime");
        tPos.addStringProperty("Storloc");
        tPos.addStringProperty("CreatedDate");
        tPos.addStringProperty("PlanCodeNo");
        tPos.addStringProperty("PlantName");
        tPos.addStringProperty("NoDoMims");
        tPos.addStringProperty("DoStatus");
        tPos.addStringProperty("Expeditions");
        tPos.addStringProperty("CourierPersonName");
        tPos.addStringProperty("KdPabrikan");
        tPos.addStringProperty("MaterialGroup");
        tPos.addStringProperty("NamaKategoriMaterial");

        // endregion

        // region posPenerimaan
        Entity tPosPenerimaan = schema.addEntity("TPosPenerimaan");
        tPosPenerimaan.addIdProperty();
        tPosPenerimaan.addStringProperty("StorLoc");
        tPosPenerimaan.addStringProperty("Total");
        tPosPenerimaan.addStringProperty("TlskNo");
        tPosPenerimaan.addStringProperty("PoSapNo");
        tPosPenerimaan.addStringProperty("PoMpNo");
        tPosPenerimaan.addStringProperty("NoDoSmar");
        tPosPenerimaan.addIntProperty("LeadTime");
        tPosPenerimaan.addStringProperty("Storloc");
        tPosPenerimaan.addStringProperty("CreatedDate");
        tPosPenerimaan.addStringProperty("PlanCodeNo");
        tPosPenerimaan.addStringProperty("PlantName");
        tPosPenerimaan.addStringProperty("NoDoMims");
        tPosPenerimaan.addStringProperty("DoStatus");
        tPosPenerimaan.addStringProperty("Expeditions");
        tPosPenerimaan.addStringProperty("CourierPersonName");
        tPosPenerimaan.addStringProperty("KdPabrikan");
        tPosPenerimaan.addStringProperty("MaterialGroup");
        tPosPenerimaan.addStringProperty("NamaKategoriMaterial");
        tPosPenerimaan.addStringProperty("PhotoSuratBarang");
        tPosPenerimaan.addStringProperty("PhotoBarang");
        tPosPenerimaan.addStringProperty("TanggalDiterima");
        tPosPenerimaan.addStringProperty("PetugasPenerima");
        tPosPenerimaan.addStringProperty("NamaKurir");
        tPosPenerimaan.addStringProperty("NamaEkspedisi");
        tPosPenerimaan.addIntProperty("isChecked");

        // endregion

        // region possns
        Entity tPosSns = schema.addEntity("TPosSns");
        tPosSns.addIdProperty();
        tPosSns.addStringProperty("noMatSap");
        tPosSns.addStringProperty("Mmc");
        tPosSns.addStringProperty("MaterialGroup");
        tPosSns.addStringProperty("TglProduksi");
        tPosSns.addStringProperty("KdPabrikan");
        tPosSns.addStringProperty("NoSertMeterologi");
        tPosSns.addStringProperty("Spln");
        tPosSns.addStringProperty("NoProduksi");
        tPosSns.addStringProperty("StorLoc");
        tPosSns.addStringProperty("NamaKategoriMaterial");
        tPosSns.addStringProperty("NoSerial");
        tPosSns.addStringProperty("NoDoSmar");
        tPosSns.addStringProperty("Spesifikasi");
        tPosSns.addStringProperty("Plant");
        tPosSns.addStringProperty("MaterialId");
        tPosSns.addStringProperty("MasaGaransi");
        tPosSns.addStringProperty("DoStatus");
        tPosSns.addStringProperty("Status");

        // region posPenerimaan
        Entity tPemeriksaan = schema.addEntity("TPemeriksaan");
        tPemeriksaan.addIdProperty();
        tPemeriksaan.addStringProperty("NoPemeriksaan");
        tPemeriksaan.addStringProperty("StorLoc");
        tPemeriksaan.addStringProperty("Total");
        tPemeriksaan.addStringProperty("TlskNo");
        tPemeriksaan.addStringProperty("PoSapNo");
        tPemeriksaan.addStringProperty("PoMpNo");
        tPemeriksaan.addStringProperty("NoDoSmar");
        tPemeriksaan.addIntProperty("LeadTime");
        tPemeriksaan.addStringProperty("Storloc");
        tPemeriksaan.addStringProperty("CreatedDate");
        tPemeriksaan.addStringProperty("PlanCodeNo");
        tPemeriksaan.addStringProperty("PlantName");
        tPemeriksaan.addStringProperty("NoDoMims");
        tPemeriksaan.addStringProperty("DoStatus");
        tPemeriksaan.addStringProperty("Expeditions");
        tPemeriksaan.addStringProperty("CourierPersonName");
        tPemeriksaan.addStringProperty("KdPabrikan");
        tPemeriksaan.addStringProperty("MaterialGroup");
        tPemeriksaan.addStringProperty("NamaKategoriMaterial");
        tPemeriksaan.addStringProperty("TanggalDiterima");
        tPemeriksaan.addStringProperty("PetugasPenerima");
        tPemeriksaan.addStringProperty("NamaKurir");
        tPemeriksaan.addStringProperty("NamaEkspedisi");
        //baru
        tPemeriksaan.addStringProperty("NamaKetua");
        tPemeriksaan.addStringProperty("NamaManager");
        tPemeriksaan.addStringProperty("NamaSekretaris");
        tPemeriksaan.addStringProperty("Anggota");
        tPemeriksaan.addStringProperty("RatingPenerimaan");
        tPemeriksaan.addStringProperty("DescPenerimaan");
        tPemeriksaan.addStringProperty("RatingQuality");
        tPemeriksaan.addStringProperty("DescQuality");
        tPemeriksaan.addStringProperty("RatingWaktu");
        tPemeriksaan.addStringProperty("DescWaktu");
        tPemeriksaan.addStringProperty("RatingPath");
        tPemeriksaan.addStringProperty("Packangings");
        tPemeriksaan.addIntProperty("State");//1 pemeriksaan 2 rating
        tPemeriksaan.addIntProperty("isDone");//untuk centangan


        // region penerimaan detail
        Entity tPemeriksaanDetail = schema.addEntity("TPemeriksaanDetail");
        tPemeriksaanDetail.addIdProperty();
        tPemeriksaanDetail.addStringProperty("NoPemeriksaan");
        tPemeriksaanDetail.addStringProperty("Sn");
        tPemeriksaanDetail.addStringProperty("NoDoSmar");
        tPemeriksaanDetail.addStringProperty("NoMaterail");
        tPemeriksaanDetail.addStringProperty("NoPackaging");
        tPemeriksaanDetail.addStringProperty("Status");//REJECTED || APPROVED
        tPemeriksaanDetail.addIntProperty("IsDone");


        // endregion

        new DaoGenerator().generateAll(schema, "../MIMS-Master/app/src/main/java");

    }
}