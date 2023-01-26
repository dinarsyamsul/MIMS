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

        // endregion

        new DaoGenerator().generateAll(schema, "../MIMS/app/src/main/java");

    }
}