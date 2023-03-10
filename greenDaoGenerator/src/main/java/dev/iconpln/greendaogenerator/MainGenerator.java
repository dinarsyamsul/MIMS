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
        tPengujian.addStringProperty("QtyLolos");
        tPengujian.addStringProperty("QtyTdkLolos");
        tPengujian.addStringProperty("QtyRusak");
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
        tPos.addStringProperty("KodeStatusDoMims");
        tPos.addStringProperty("CourierPersonName");
        tPos.addStringProperty("KdPabrikan");
        tPos.addStringProperty("MaterialGroup");
        tPos.addStringProperty("NamaKategoriMaterial");
        tPos.addStringProperty("PetugasPenerima");
        tPos.addStringProperty("TglDiterima");
        tPos.addStringProperty("KurirPengantar");
        tPos.addStringProperty("DoLineItem");
        tPos.addStringProperty("RatingResponse");
        tPos.addStringProperty("RatingQuality");
        tPos.addStringProperty("RatingDelivery");

        // endregion

        // region posPenerimaan
        Entity tPosPenerimaan = schema.addEntity("TPosPenerimaan");
        tPosPenerimaan.addIdProperty();
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
        tPosPenerimaan.addStringProperty("RatingPenerimaan");
        tPosPenerimaan.addStringProperty("DescPenerimaan");
        tPosPenerimaan.addStringProperty("RatingQuality");
        tPosPenerimaan.addStringProperty("DescQuality");
        tPosPenerimaan.addStringProperty("RatingWaktu");
        tPosPenerimaan.addStringProperty("DescWaktu");
        tPosPenerimaan.addStringProperty("TanggalDiterima");
        tPosPenerimaan.addStringProperty("PetugasPenerima");
        tPosPenerimaan.addStringProperty("KodeStatusDoMims");
        tPosPenerimaan.addStringProperty("StatusPemeriksaan");
        tPosPenerimaan.addStringProperty("KurirPengantar");
        tPosPenerimaan.addStringProperty("NilaiRatingPenerimaan");
        tPosPenerimaan.addStringProperty("NilaiRatingWaktu");
        tPosPenerimaan.addStringProperty("NilaiRatingQuality");
        tPosPenerimaan.addStringProperty("DoLineItem");
        tPosPenerimaan.addIntProperty("IsRating");
        tPosPenerimaan.addIntProperty("isDone");

        // endregion

        //region Pos Detail Penerimaan
        Entity tPosDetailPenerimaan = schema.addEntity("TPosDetailPenerimaan");
        tPosDetailPenerimaan.addIdProperty();
        tPosDetailPenerimaan.addStringProperty("NoDoSmar");
        tPosDetailPenerimaan.addStringProperty("Qty");
        tPosDetailPenerimaan.addStringProperty("KdPabrikan");
        tPosDetailPenerimaan.addStringProperty("DoStatus");
        tPosDetailPenerimaan.addStringProperty("NoPackaging");
        tPosDetailPenerimaan.addStringProperty("SerialNumber");
        tPosDetailPenerimaan.addStringProperty("NoMaterial");
        tPosDetailPenerimaan.addStringProperty("NamaKategoriMaterial");
        tPosDetailPenerimaan.addStringProperty("StorLoc");
        tPosDetailPenerimaan.addStringProperty("StatusPenerimaan");
        tPosDetailPenerimaan.addStringProperty("StatusPemeriksaan");
        tPosDetailPenerimaan.addIntProperty("IsComplaint");
        tPosDetailPenerimaan.addIntProperty("IsChecked");
        tPosDetailPenerimaan.addStringProperty("DoLineItem");
        tPosDetailPenerimaan.addIntProperty("IsDone");

        //endregion

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
        tPosSns.addStringProperty("NoPackaging");
        tPosSns.addStringProperty("DoLineItem");
        tPosSns.addStringProperty("StatusPenerimaan");
        tPosSns.addStringProperty("StatusPemeriksaan");

        // region SnPermaterial
        Entity tSnPermaterial = schema.addEntity("TSnPermaterial");
        tSnPermaterial.addIdProperty();
        tSnPermaterial.addStringProperty("noMatSap");
        tSnPermaterial.addStringProperty("Mmc");
        tSnPermaterial.addStringProperty("MaterialGroup");
        tSnPermaterial.addStringProperty("TglProduksi");
        tSnPermaterial.addStringProperty("KdPabrikan");
        tSnPermaterial.addStringProperty("NoSertMeterologi");
        tSnPermaterial.addStringProperty("Spln");
        tSnPermaterial.addStringProperty("NoProduksi");
        tSnPermaterial.addStringProperty("StorLoc");
        tSnPermaterial.addStringProperty("NamaKategoriMaterial");
        tSnPermaterial.addStringProperty("NoSerial");
        tSnPermaterial.addStringProperty("NoDoSmar");
        tSnPermaterial.addStringProperty("Spesifikasi");
        tSnPermaterial.addStringProperty("Plant");
        tSnPermaterial.addStringProperty("MaterialId");
        tSnPermaterial.addStringProperty("MasaGaransi");
        tSnPermaterial.addStringProperty("DoStatus");
        tSnPermaterial.addStringProperty("NoPackaging");
        tSnPermaterial.addStringProperty("DoLineItem");
        tSnPermaterial.addStringProperty("Status");

        // region pemeriksaan
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
        tPemeriksaan.addStringProperty("CreatedDate");
        tPemeriksaan.addStringProperty("PlanCodeNo");
        tPemeriksaan.addStringProperty("PlantName");
        tPemeriksaan.addStringProperty("NoDoMims");
        tPemeriksaan.addStringProperty("DoStatus");
        tPemeriksaan.addStringProperty("StatusPemeriksaan");
        tPemeriksaan.addStringProperty("Expeditions");
        tPemeriksaan.addStringProperty("CourierPersonName");
        tPemeriksaan.addStringProperty("KdPabrikan");
        tPemeriksaan.addStringProperty("MaterialGroup");
        tPemeriksaan.addStringProperty("NamaKategoriMaterial");
        tPemeriksaan.addStringProperty("TanggalDiterima");
        tPemeriksaan.addStringProperty("PetugasPenerima");
        tPemeriksaan.addStringProperty("NamaKurir");
        tPemeriksaan.addStringProperty("NamaEkspedisi");
        tPemeriksaan.addStringProperty("DoLineItem");

        tPemeriksaan.addStringProperty("NamaManager");
        tPemeriksaan.addStringProperty("NamaKetua");
        tPemeriksaan.addStringProperty("NamaSekretaris");
        tPemeriksaan.addStringProperty("NamaAnggota");
        tPemeriksaan.addStringProperty("NamaAnggotaBaru");

        tPemeriksaan.addIntProperty("isDone");//untuk centangan


        // region pemeriksaan detail
        Entity tPemeriksaanDetail = schema.addEntity("TPemeriksaanDetail");
        tPemeriksaanDetail.addIdProperty();
        tPemeriksaanDetail.addStringProperty("NoPemeriksaan");
        tPemeriksaanDetail.addStringProperty("Sn");
        tPemeriksaanDetail.addStringProperty("NoDoSmar");
        tPemeriksaanDetail.addStringProperty("NoMaterail");
        tPemeriksaanDetail.addStringProperty("NoPackaging");
        tPemeriksaanDetail.addStringProperty("Kategori");
        tPemeriksaanDetail.addStringProperty("StatusPenerimaan");
        tPemeriksaanDetail.addStringProperty("StatusPemeriksaan");
        tPemeriksaanDetail.addIntProperty("isPeriksa");
        tPemeriksaanDetail.addIntProperty("IsChecked");
        tPemeriksaanDetail.addIntProperty("IsComplaint");
        tPemeriksaanDetail.addIntProperty("IsDone");

        // endregion

        // region photo
        Entity tPhoto = schema.addEntity("TPhoto");
        tPhoto.addIdProperty();
        tPhoto.addIntProperty("PhotoNumber");
        tPhoto.addStringProperty("NoDo");
        tPhoto.addStringProperty("Path");
        tPhoto.addStringProperty("Type");

        // endregion

        Entity tRating = schema.addEntity("TRating");
        tRating.addIdProperty();
        tRating.addStringProperty("KdRating");
        tRating.addStringProperty("Keterangan");
        tRating.addIntProperty("Nilai");
        tRating.addStringProperty("Type");
        tRating.addIntProperty("IsActive");

        Entity tMonitoringPermintaan = schema.addEntity("TMonitoringPermintaan");
        tMonitoringPermintaan.addIdProperty();
        tMonitoringPermintaan.addStringProperty("NoPermintaan");
        tMonitoringPermintaan.addStringProperty("NoTransaksi");
        tMonitoringPermintaan.addStringProperty("StorLocTujuanName");
        tMonitoringPermintaan.addStringProperty("KodePengeluaran");
        tMonitoringPermintaan.addStringProperty("StorLocTujuan");
        tMonitoringPermintaan.addStringProperty("CreatedBy");
        tMonitoringPermintaan.addStringProperty("NoRepackaging");
        tMonitoringPermintaan.addStringProperty("Plant");
        tMonitoringPermintaan.addStringProperty("UpdatedBy");
        tMonitoringPermintaan.addStringProperty("CreatedDate");
        tMonitoringPermintaan.addStringProperty("UpdatedDate");
        tMonitoringPermintaan.addIntProperty("JumlahKardus");
        tMonitoringPermintaan.addStringProperty("StorLocAsalName");
        tMonitoringPermintaan.addStringProperty("TanggalPermintaan");
        tMonitoringPermintaan.addStringProperty("TanggalPengeluaran");
        tMonitoringPermintaan.addStringProperty("PlantName");
        tMonitoringPermintaan.addStringProperty("StorLocAsal");

        Entity tMonitoringPermintaanDetail = schema.addEntity("TMonitoringPermintaanDetail");
        tMonitoringPermintaanDetail.addIdProperty();
        tMonitoringPermintaanDetail.addStringProperty("NoPermintaan");
        tMonitoringPermintaanDetail.addStringProperty("NoTransaksi");
        tMonitoringPermintaanDetail.addStringProperty("NoRepackaging");
        tMonitoringPermintaanDetail.addStringProperty("NomorMaterial");
        tMonitoringPermintaanDetail.addStringProperty("Unit");
        tMonitoringPermintaanDetail.addIntProperty("QtyPermintaan");
        tMonitoringPermintaanDetail.addStringProperty("MaterialDesc");
        tMonitoringPermintaanDetail.addStringProperty("QtyScan");
        tMonitoringPermintaanDetail.addStringProperty("Kategori");
        tMonitoringPermintaanDetail.addStringProperty("QtyPengeluaran");

        Entity tTransMonitoringPermintaan = schema.addEntity("TTransMonitoringPermintaan");
        tTransMonitoringPermintaan.addIdProperty();
        tTransMonitoringPermintaan.addStringProperty("NoPermintaan");
        tTransMonitoringPermintaan.addStringProperty("NoTransaksi");
        tTransMonitoringPermintaan.addStringProperty("StorLocTujuanName");
        tTransMonitoringPermintaan.addStringProperty("KodePengeluaran");
        tTransMonitoringPermintaan.addStringProperty("StorLocTujuan");
        tTransMonitoringPermintaan.addStringProperty("CreatedBy");
        tTransMonitoringPermintaan.addStringProperty("NoRepackaging");
        tTransMonitoringPermintaan.addStringProperty("Plant");
        tTransMonitoringPermintaan.addStringProperty("UpdatedBy");
        tTransMonitoringPermintaan.addStringProperty("CreatedDate");
        tTransMonitoringPermintaan.addStringProperty("UpdatedDate");
        tTransMonitoringPermintaan.addIntProperty("JumlahKardus");
        tTransMonitoringPermintaan.addStringProperty("StorLocAsalName");
        tTransMonitoringPermintaan.addStringProperty("TanggalPermintaan");
        tTransMonitoringPermintaan.addStringProperty("TanggalPengeluaran");
        tTransMonitoringPermintaan.addStringProperty("PlantName");
        tTransMonitoringPermintaan.addStringProperty("StorLocAsal");
        tTransMonitoringPermintaan.addIntProperty("IsDone");

        Entity tTransMonitoringPermintaanDetail = schema.addEntity("TTransMonitoringPermintaanDetail");
        tTransMonitoringPermintaanDetail.addIdProperty();
        tTransMonitoringPermintaanDetail.addStringProperty("NoPermintaan");
        tTransMonitoringPermintaanDetail.addStringProperty("NoTransaksi");
        tTransMonitoringPermintaanDetail.addStringProperty("NoRepackaging");
        tTransMonitoringPermintaanDetail.addStringProperty("NomorMaterial");
        tTransMonitoringPermintaanDetail.addStringProperty("Unit");
        tTransMonitoringPermintaanDetail.addIntProperty("QtyPermintaan");
        tTransMonitoringPermintaanDetail.addStringProperty("MaterialDesc");
        tTransMonitoringPermintaanDetail.addStringProperty("QtyScan");
        tTransMonitoringPermintaanDetail.addStringProperty("Kategori");
        tTransMonitoringPermintaanDetail.addStringProperty("QtyPengeluaran");
        tTransMonitoringPermintaanDetail.addIntProperty("IsScannedSn");
        tTransMonitoringPermintaanDetail.addIntProperty("IsDone");

        Entity tSnMonitoringPermintaan = schema.addEntity("TSnMonitoringPermintaan");
        tSnMonitoringPermintaan.addIdProperty();
        tSnMonitoringPermintaan.addStringProperty("NoRepackaging");
        tSnMonitoringPermintaan.addStringProperty("NomorMaterial");
        tSnMonitoringPermintaan.addStringProperty("SerialNumber");
        tSnMonitoringPermintaan.addStringProperty("Status");

        Entity tMonitoringSnMaterial = schema.addEntity("TMonitoringSnMaterial");
        tMonitoringSnMaterial.addIdProperty();
        tMonitoringSnMaterial.addStringProperty("NoRepackaging");
        tMonitoringSnMaterial.addStringProperty("NomorMaterial");
        tMonitoringSnMaterial.addStringProperty("SerialNumber");
        tMonitoringSnMaterial.addStringProperty("Status");
        tMonitoringSnMaterial.addIntProperty("IsScanned");

        Entity tPenerimaanUlp = schema.addEntity("TPenerimaanUlp");
        tPenerimaanUlp.addIdProperty();
        tPenerimaanUlp.addStringProperty("NoPengiriman");
        tPenerimaanUlp.addStringProperty("NoPermintaan");
        tPenerimaanUlp.addStringProperty("StatusPemeriksaan");
        tPenerimaanUlp.addStringProperty("DeliveryDate");
        tPenerimaanUlp.addStringProperty("StatusPenerimaan");
        tPenerimaanUlp.addIntProperty("JumlahKardus");
        tPenerimaanUlp.addStringProperty("GudangAsal");
        tPenerimaanUlp.addStringProperty("NoRepackaging");
        tPenerimaanUlp.addStringProperty("GudangTujuan");
        tPenerimaanUlp.addStringProperty("TanggalPemeriksaan");
        tPenerimaanUlp.addStringProperty("TanggalPenerimaan");
        tPenerimaanUlp.addStringProperty("KepalaGudangPemeriksa");
        tPenerimaanUlp.addStringProperty("PejabatPemeriksa");
        tPenerimaanUlp.addStringProperty("JabatanPemeriksa");
        tPenerimaanUlp.addStringProperty("NamaPetugasPemeriksa");
        tPenerimaanUlp.addStringProperty("JabatanPetugasPemeriksa");
        tPenerimaanUlp.addStringProperty("KepalaGudangPenerima");
        tPenerimaanUlp.addStringProperty("NoPk");
        tPenerimaanUlp.addStringProperty("TanggalDokumen");
        tPenerimaanUlp.addStringProperty("PejabatPenerima");
        tPenerimaanUlp.addStringProperty("Kurir");
        tPenerimaanUlp.addStringProperty("NoNota");
        tPenerimaanUlp.addStringProperty("NoMaterial");
        tPenerimaanUlp.addStringProperty("Spesifikasi");
        tPenerimaanUlp.addIntProperty("KuantitasPeriksa");
        tPenerimaanUlp.addIntProperty("Kuantitas");

        Entity tPenerimaanDetailUlp = schema.addEntity("TPenerimaanDetailUlp");
        tPenerimaanDetailUlp.addIdProperty();
        tPenerimaanDetailUlp.addStringProperty("NoTransaksi");
        tPenerimaanDetailUlp.addStringProperty("NoRepackaging");
        tPenerimaanDetailUlp.addStringProperty("NoMaterial");
        tPenerimaanDetailUlp.addStringProperty("MaterialDesc");
        tPenerimaanDetailUlp.addIntProperty("QtyPermintaan");
        tPenerimaanDetailUlp.addIntProperty("QtyPengiriman");
        tPenerimaanDetailUlp.addIntProperty("QtyPemeriksaan");
        tPenerimaanDetailUlp.addIntProperty("QtyPenerimaan");
        tPenerimaanDetailUlp.addIntProperty("QtySesuai");

        Entity tTransPenerimaanUlp = schema.addEntity("TTransPenerimaanUlp");
        tTransPenerimaanUlp.addIdProperty();
        tTransPenerimaanUlp.addStringProperty("NoPengiriman");
        tTransPenerimaanUlp.addStringProperty("NoPermintaan");
        tTransPenerimaanUlp.addStringProperty("StatusPemeriksaan");
        tTransPenerimaanUlp.addStringProperty("TempStatusPemeriksaan");
        tTransPenerimaanUlp.addStringProperty("DeliveryDate");
        tTransPenerimaanUlp.addStringProperty("StatusPenerimaan");
        tTransPenerimaanUlp.addStringProperty("TempStatusPenerimaan");
        tTransPenerimaanUlp.addIntProperty("JumlahKardus");
        tTransPenerimaanUlp.addStringProperty("GudangAsal");
        tTransPenerimaanUlp.addStringProperty("NoRepackaging");
        tTransPenerimaanUlp.addStringProperty("GudangTujuan");
        tTransPenerimaanUlp.addStringProperty("TanggalPemeriksaan");
        tTransPenerimaanUlp.addStringProperty("TanggalPenerimaan");
        tTransPenerimaanUlp.addStringProperty("KepalaGudangPemeriksa");
        tTransPenerimaanUlp.addStringProperty("PejabatPemeriksa");
        tTransPenerimaanUlp.addStringProperty("JabatanPemeriksa");
        tTransPenerimaanUlp.addStringProperty("NamaPetugasPemeriksa");
        tTransPenerimaanUlp.addStringProperty("JabatanPetugasPemeriksa");
        tTransPenerimaanUlp.addStringProperty("KepalaGudangPenerima");
        tTransPenerimaanUlp.addStringProperty("NoPk");
        tTransPenerimaanUlp.addStringProperty("TanggalDokumen");
        tTransPenerimaanUlp.addStringProperty("PejabatPenerima");
        tTransPenerimaanUlp.addStringProperty("Kurir");
        tTransPenerimaanUlp.addStringProperty("NoNota");
        tTransPenerimaanUlp.addStringProperty("NoMaterial");
        tTransPenerimaanUlp.addStringProperty("Spesifikasi");
        tTransPenerimaanUlp.addIntProperty("KuantitasPeriksa");
        tTransPenerimaanUlp.addIntProperty("Kuantitas");
        tTransPenerimaanUlp.addIntProperty("IsDonePemeriksaan");
        tTransPenerimaanUlp.addIntProperty("IsDone");

        Entity tTransPenerimaanDetailUlp = schema.addEntity("TTransPenerimaanDetailUlp");
        tTransPenerimaanDetailUlp.addIdProperty();
        tTransPenerimaanDetailUlp.addStringProperty("NoTransaksi");
        tTransPenerimaanDetailUlp.addStringProperty("NoRepackaging");
        tTransPenerimaanDetailUlp.addStringProperty("NoMaterial");
        tTransPenerimaanDetailUlp.addStringProperty("MaterialDesc");
        tTransPenerimaanDetailUlp.addIntProperty("QtyPermintaan");
        tTransPenerimaanDetailUlp.addIntProperty("QtyPengiriman");
        tTransPenerimaanDetailUlp.addIntProperty("QtyPemeriksaan");
        tTransPenerimaanDetailUlp.addIntProperty("QtyPenerimaan");
        tTransPenerimaanDetailUlp.addIntProperty("QtySesuai");
        tTransPenerimaanDetailUlp.addIntProperty("IsDone");

        Entity tListSnMaterialPenerimaanUlp = schema.addEntity("TListSnMaterialPenerimaanUlp");
        tListSnMaterialPenerimaanUlp.addIdProperty();
        tListSnMaterialPenerimaanUlp.addStringProperty("NoRepackaging");
        tListSnMaterialPenerimaanUlp.addStringProperty("NoMaterial");
        tListSnMaterialPenerimaanUlp.addStringProperty("NoSerialNumber");
        tListSnMaterialPenerimaanUlp.addStringProperty("Status");
        tListSnMaterialPenerimaanUlp.addIntProperty("IsScanned");

        Entity tPemakaian = schema.addEntity("TPemakaian");
        tPemakaian.addIdProperty();
        tPemakaian.addStringProperty("NoTransaksi");
        tPemakaian.addStringProperty("NoReservasi");
        tPemakaian.addStringProperty("NoPemesanan");
        tPemakaian.addStringProperty("KodeIntegrasi");
        tPemakaian.addStringProperty("Plant");
        tPemakaian.addStringProperty("StorLoc");
        tPemakaian.addStringProperty("StatusPemakaian");
        tPemakaian.addStringProperty("TanggalReservasi");
        tPemakaian.addStringProperty("TanggalPemakaian");
        tPemakaian.addStringProperty("TanggalPengeluaran");
        tPemakaian.addStringProperty("TanggalDokumen");
        tPemakaian.addStringProperty("JenisPekerjaan");
        tPemakaian.addStringProperty("Sumber");
        tPemakaian.addStringProperty("StatusKirimAgo");
        tPemakaian.addStringProperty("StatusSap");
        tPemakaian.addStringProperty("NoAgenda");
        tPemakaian.addStringProperty("IdPelanggan");
        tPemakaian.addStringProperty("NamaPelanggan");
        tPemakaian.addStringProperty("AlamatPelanggan");
        tPemakaian.addStringProperty("Tarif");
        tPemakaian.addStringProperty("Daya");
        tPemakaian.addStringProperty("TanggalBayar");

        Entity tPemakaianDetail = schema.addEntity("TPemakaianDetail");
        tPemakaianDetail.addIdProperty();
        tPemakaianDetail.addStringProperty("NoTransaksi");
        tPemakaianDetail.addStringProperty("NomorMaterial");
        tPemakaianDetail.addStringProperty("NamaMaterial");
        tPemakaianDetail.addStringProperty("QtyReservasi");
        tPemakaianDetail.addStringProperty("QtyPemakaian");
        tPemakaianDetail.addStringProperty("QtyPengeluaran");
        tPemakaianDetail.addStringProperty("Unit");
        tPemakaianDetail.addStringProperty("NoMeter");
        tPemakaianDetail.addStringProperty("ValuationType");
        tPemakaianDetail.addStringProperty("Keterangan");

        Entity tTransPemakaianDetail = schema.addEntity("TTransPemakaianDetail");
        tTransPemakaianDetail.addIdProperty();
        tTransPemakaianDetail.addStringProperty("NoTransaksi");
        tTransPemakaianDetail.addStringProperty("NomorMaterial");
        tTransPemakaianDetail.addStringProperty("NamaMaterial");
        tTransPemakaianDetail.addStringProperty("QtyReservasi");
        tTransPemakaianDetail.addStringProperty("QtyPemakaian");
        tTransPemakaianDetail.addStringProperty("QtyPengeluaran");
        tTransPemakaianDetail.addStringProperty("Unit");
        tTransPemakaianDetail.addStringProperty("NoMeter");
        tTransPemakaianDetail.addStringProperty("ValuationType");
        tTransPemakaianDetail.addStringProperty("Keterangan");
        tTransPemakaianDetail.addStringProperty("SnScanned");
        tTransPemakaianDetail.addIntProperty("IsDone");

        Entity tListSnMaterialPemakaianUlp = schema.addEntity("TListSnMaterialPemakaianUlp");
        tListSnMaterialPemakaianUlp.addIdProperty();
        tListSnMaterialPemakaianUlp.addStringProperty("NoTransaksi");
        tListSnMaterialPemakaianUlp.addStringProperty("NoMaterial");
        tListSnMaterialPemakaianUlp.addStringProperty("NoSerialNumber");
        tListSnMaterialPemakaianUlp.addIntProperty("IsScanned");

        new DaoGenerator().generateAll(schema, "../MIMS-Master/app/src/main/java");

    }
}