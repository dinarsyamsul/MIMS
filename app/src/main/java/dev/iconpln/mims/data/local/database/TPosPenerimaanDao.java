package dev.iconpln.mims.data.local.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TPOS_PENERIMAAN".
*/
public class TPosPenerimaanDao extends AbstractDao<TPosPenerimaan, Long> {

    public static final String TABLENAME = "TPOS_PENERIMAAN";

    /**
     * Properties of entity TPosPenerimaan.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Total = new Property(1, String.class, "Total", false, "TOTAL");
        public final static Property TlskNo = new Property(2, String.class, "TlskNo", false, "TLSK_NO");
        public final static Property PoSapNo = new Property(3, String.class, "PoSapNo", false, "PO_SAP_NO");
        public final static Property PoMpNo = new Property(4, String.class, "PoMpNo", false, "PO_MP_NO");
        public final static Property NoDoSmar = new Property(5, String.class, "NoDoSmar", false, "NO_DO_SMAR");
        public final static Property LeadTime = new Property(6, Integer.class, "LeadTime", false, "LEAD_TIME");
        public final static Property PoDate = new Property(7, String.class, "PoDate", false, "PO_DATE");
        public final static Property Storloc = new Property(8, String.class, "Storloc", false, "STORLOC");
        public final static Property CreatedDate = new Property(9, String.class, "CreatedDate", false, "CREATED_DATE");
        public final static Property PlanCodeNo = new Property(10, String.class, "PlanCodeNo", false, "PLAN_CODE_NO");
        public final static Property PlantName = new Property(11, String.class, "PlantName", false, "PLANT_NAME");
        public final static Property NoDoMims = new Property(12, String.class, "NoDoMims", false, "NO_DO_MIMS");
        public final static Property DoStatus = new Property(13, String.class, "DoStatus", false, "DO_STATUS");
        public final static Property Expeditions = new Property(14, String.class, "Expeditions", false, "EXPEDITIONS");
        public final static Property CourierPersonName = new Property(15, String.class, "CourierPersonName", false, "COURIER_PERSON_NAME");
        public final static Property KdPabrikan = new Property(16, String.class, "KdPabrikan", false, "KD_PABRIKAN");
        public final static Property MaterialGroup = new Property(17, String.class, "MaterialGroup", false, "MATERIAL_GROUP");
        public final static Property NamaKategoriMaterial = new Property(18, String.class, "NamaKategoriMaterial", false, "NAMA_KATEGORI_MATERIAL");
        public final static Property RatingPenerimaan = new Property(19, String.class, "RatingPenerimaan", false, "RATING_PENERIMAAN");
        public final static Property DescPenerimaan = new Property(20, String.class, "DescPenerimaan", false, "DESC_PENERIMAAN");
        public final static Property RatingQuality = new Property(21, String.class, "RatingQuality", false, "RATING_QUALITY");
        public final static Property DescQuality = new Property(22, String.class, "DescQuality", false, "DESC_QUALITY");
        public final static Property RatingWaktu = new Property(23, String.class, "RatingWaktu", false, "RATING_WAKTU");
        public final static Property DescWaktu = new Property(24, String.class, "DescWaktu", false, "DESC_WAKTU");
        public final static Property TanggalDiterima = new Property(25, String.class, "TanggalDiterima", false, "TANGGAL_DITERIMA");
        public final static Property PetugasPenerima = new Property(26, String.class, "PetugasPenerima", false, "PETUGAS_PENERIMA");
        public final static Property KodeStatusDoMims = new Property(27, String.class, "KodeStatusDoMims", false, "KODE_STATUS_DO_MIMS");
        public final static Property StatusPemeriksaan = new Property(28, String.class, "StatusPemeriksaan", false, "STATUS_PEMERIKSAAN");
        public final static Property StatusPenerimaan = new Property(29, String.class, "StatusPenerimaan", false, "STATUS_PENERIMAAN");
        public final static Property KurirPengantar = new Property(30, String.class, "KurirPengantar", false, "KURIR_PENGANTAR");
        public final static Property NilaiRatingPenerimaan = new Property(31, String.class, "NilaiRatingPenerimaan", false, "NILAI_RATING_PENERIMAAN");
        public final static Property NilaiRatingWaktu = new Property(32, String.class, "NilaiRatingWaktu", false, "NILAI_RATING_WAKTU");
        public final static Property NilaiRatingQuality = new Property(33, String.class, "NilaiRatingQuality", false, "NILAI_RATING_QUALITY");
        public final static Property DoLineItem = new Property(34, String.class, "DoLineItem", false, "DO_LINE_ITEM");
        public final static Property BisaTerima = new Property(35, Integer.class, "BisaTerima", false, "BISA_TERIMA");
        public final static Property IsRating = new Property(36, Integer.class, "IsRating", false, "IS_RATING");
        public final static Property IsDone = new Property(37, Integer.class, "isDone", false, "IS_DONE");
        public final static Property RatingDone = new Property(38, Integer.class, "ratingDone", false, "RATING_DONE");
    }


    public TPosPenerimaanDao(DaoConfig config) {
        super(config);
    }
    
    public TPosPenerimaanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TPOS_PENERIMAAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TOTAL\" TEXT," + // 1: Total
                "\"TLSK_NO\" TEXT," + // 2: TlskNo
                "\"PO_SAP_NO\" TEXT," + // 3: PoSapNo
                "\"PO_MP_NO\" TEXT," + // 4: PoMpNo
                "\"NO_DO_SMAR\" TEXT," + // 5: NoDoSmar
                "\"LEAD_TIME\" INTEGER," + // 6: LeadTime
                "\"PO_DATE\" TEXT," + // 7: PoDate
                "\"STORLOC\" TEXT," + // 8: Storloc
                "\"CREATED_DATE\" TEXT," + // 9: CreatedDate
                "\"PLAN_CODE_NO\" TEXT," + // 10: PlanCodeNo
                "\"PLANT_NAME\" TEXT," + // 11: PlantName
                "\"NO_DO_MIMS\" TEXT," + // 12: NoDoMims
                "\"DO_STATUS\" TEXT," + // 13: DoStatus
                "\"EXPEDITIONS\" TEXT," + // 14: Expeditions
                "\"COURIER_PERSON_NAME\" TEXT," + // 15: CourierPersonName
                "\"KD_PABRIKAN\" TEXT," + // 16: KdPabrikan
                "\"MATERIAL_GROUP\" TEXT," + // 17: MaterialGroup
                "\"NAMA_KATEGORI_MATERIAL\" TEXT," + // 18: NamaKategoriMaterial
                "\"RATING_PENERIMAAN\" TEXT," + // 19: RatingPenerimaan
                "\"DESC_PENERIMAAN\" TEXT," + // 20: DescPenerimaan
                "\"RATING_QUALITY\" TEXT," + // 21: RatingQuality
                "\"DESC_QUALITY\" TEXT," + // 22: DescQuality
                "\"RATING_WAKTU\" TEXT," + // 23: RatingWaktu
                "\"DESC_WAKTU\" TEXT," + // 24: DescWaktu
                "\"TANGGAL_DITERIMA\" TEXT," + // 25: TanggalDiterima
                "\"PETUGAS_PENERIMA\" TEXT," + // 26: PetugasPenerima
                "\"KODE_STATUS_DO_MIMS\" TEXT," + // 27: KodeStatusDoMims
                "\"STATUS_PEMERIKSAAN\" TEXT," + // 28: StatusPemeriksaan
                "\"STATUS_PENERIMAAN\" TEXT," + // 29: StatusPenerimaan
                "\"KURIR_PENGANTAR\" TEXT," + // 30: KurirPengantar
                "\"NILAI_RATING_PENERIMAAN\" TEXT," + // 31: NilaiRatingPenerimaan
                "\"NILAI_RATING_WAKTU\" TEXT," + // 32: NilaiRatingWaktu
                "\"NILAI_RATING_QUALITY\" TEXT," + // 33: NilaiRatingQuality
                "\"DO_LINE_ITEM\" TEXT," + // 34: DoLineItem
                "\"BISA_TERIMA\" INTEGER," + // 35: BisaTerima
                "\"IS_RATING\" INTEGER," + // 36: IsRating
                "\"IS_DONE\" INTEGER," + // 37: isDone
                "\"RATING_DONE\" INTEGER);"); // 38: ratingDone
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TPOS_PENERIMAAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TPosPenerimaan entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Total = entity.getTotal();
        if (Total != null) {
            stmt.bindString(2, Total);
        }
 
        String TlskNo = entity.getTlskNo();
        if (TlskNo != null) {
            stmt.bindString(3, TlskNo);
        }
 
        String PoSapNo = entity.getPoSapNo();
        if (PoSapNo != null) {
            stmt.bindString(4, PoSapNo);
        }
 
        String PoMpNo = entity.getPoMpNo();
        if (PoMpNo != null) {
            stmt.bindString(5, PoMpNo);
        }
 
        String NoDoSmar = entity.getNoDoSmar();
        if (NoDoSmar != null) {
            stmt.bindString(6, NoDoSmar);
        }
 
        Integer LeadTime = entity.getLeadTime();
        if (LeadTime != null) {
            stmt.bindLong(7, LeadTime);
        }
 
        String PoDate = entity.getPoDate();
        if (PoDate != null) {
            stmt.bindString(8, PoDate);
        }
 
        String Storloc = entity.getStorloc();
        if (Storloc != null) {
            stmt.bindString(9, Storloc);
        }
 
        String CreatedDate = entity.getCreatedDate();
        if (CreatedDate != null) {
            stmt.bindString(10, CreatedDate);
        }
 
        String PlanCodeNo = entity.getPlanCodeNo();
        if (PlanCodeNo != null) {
            stmt.bindString(11, PlanCodeNo);
        }
 
        String PlantName = entity.getPlantName();
        if (PlantName != null) {
            stmt.bindString(12, PlantName);
        }
 
        String NoDoMims = entity.getNoDoMims();
        if (NoDoMims != null) {
            stmt.bindString(13, NoDoMims);
        }
 
        String DoStatus = entity.getDoStatus();
        if (DoStatus != null) {
            stmt.bindString(14, DoStatus);
        }
 
        String Expeditions = entity.getExpeditions();
        if (Expeditions != null) {
            stmt.bindString(15, Expeditions);
        }
 
        String CourierPersonName = entity.getCourierPersonName();
        if (CourierPersonName != null) {
            stmt.bindString(16, CourierPersonName);
        }
 
        String KdPabrikan = entity.getKdPabrikan();
        if (KdPabrikan != null) {
            stmt.bindString(17, KdPabrikan);
        }
 
        String MaterialGroup = entity.getMaterialGroup();
        if (MaterialGroup != null) {
            stmt.bindString(18, MaterialGroup);
        }
 
        String NamaKategoriMaterial = entity.getNamaKategoriMaterial();
        if (NamaKategoriMaterial != null) {
            stmt.bindString(19, NamaKategoriMaterial);
        }
 
        String RatingPenerimaan = entity.getRatingPenerimaan();
        if (RatingPenerimaan != null) {
            stmt.bindString(20, RatingPenerimaan);
        }
 
        String DescPenerimaan = entity.getDescPenerimaan();
        if (DescPenerimaan != null) {
            stmt.bindString(21, DescPenerimaan);
        }
 
        String RatingQuality = entity.getRatingQuality();
        if (RatingQuality != null) {
            stmt.bindString(22, RatingQuality);
        }
 
        String DescQuality = entity.getDescQuality();
        if (DescQuality != null) {
            stmt.bindString(23, DescQuality);
        }
 
        String RatingWaktu = entity.getRatingWaktu();
        if (RatingWaktu != null) {
            stmt.bindString(24, RatingWaktu);
        }
 
        String DescWaktu = entity.getDescWaktu();
        if (DescWaktu != null) {
            stmt.bindString(25, DescWaktu);
        }
 
        String TanggalDiterima = entity.getTanggalDiterima();
        if (TanggalDiterima != null) {
            stmt.bindString(26, TanggalDiterima);
        }
 
        String PetugasPenerima = entity.getPetugasPenerima();
        if (PetugasPenerima != null) {
            stmt.bindString(27, PetugasPenerima);
        }
 
        String KodeStatusDoMims = entity.getKodeStatusDoMims();
        if (KodeStatusDoMims != null) {
            stmt.bindString(28, KodeStatusDoMims);
        }
 
        String StatusPemeriksaan = entity.getStatusPemeriksaan();
        if (StatusPemeriksaan != null) {
            stmt.bindString(29, StatusPemeriksaan);
        }
 
        String StatusPenerimaan = entity.getStatusPenerimaan();
        if (StatusPenerimaan != null) {
            stmt.bindString(30, StatusPenerimaan);
        }
 
        String KurirPengantar = entity.getKurirPengantar();
        if (KurirPengantar != null) {
            stmt.bindString(31, KurirPengantar);
        }
 
        String NilaiRatingPenerimaan = entity.getNilaiRatingPenerimaan();
        if (NilaiRatingPenerimaan != null) {
            stmt.bindString(32, NilaiRatingPenerimaan);
        }
 
        String NilaiRatingWaktu = entity.getNilaiRatingWaktu();
        if (NilaiRatingWaktu != null) {
            stmt.bindString(33, NilaiRatingWaktu);
        }
 
        String NilaiRatingQuality = entity.getNilaiRatingQuality();
        if (NilaiRatingQuality != null) {
            stmt.bindString(34, NilaiRatingQuality);
        }
 
        String DoLineItem = entity.getDoLineItem();
        if (DoLineItem != null) {
            stmt.bindString(35, DoLineItem);
        }
 
        Integer BisaTerima = entity.getBisaTerima();
        if (BisaTerima != null) {
            stmt.bindLong(36, BisaTerima);
        }
 
        Integer IsRating = entity.getIsRating();
        if (IsRating != null) {
            stmt.bindLong(37, IsRating);
        }
 
        Integer isDone = entity.getIsDone();
        if (isDone != null) {
            stmt.bindLong(38, isDone);
        }
 
        Integer ratingDone = entity.getRatingDone();
        if (ratingDone != null) {
            stmt.bindLong(39, ratingDone);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TPosPenerimaan entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Total = entity.getTotal();
        if (Total != null) {
            stmt.bindString(2, Total);
        }
 
        String TlskNo = entity.getTlskNo();
        if (TlskNo != null) {
            stmt.bindString(3, TlskNo);
        }
 
        String PoSapNo = entity.getPoSapNo();
        if (PoSapNo != null) {
            stmt.bindString(4, PoSapNo);
        }
 
        String PoMpNo = entity.getPoMpNo();
        if (PoMpNo != null) {
            stmt.bindString(5, PoMpNo);
        }
 
        String NoDoSmar = entity.getNoDoSmar();
        if (NoDoSmar != null) {
            stmt.bindString(6, NoDoSmar);
        }
 
        Integer LeadTime = entity.getLeadTime();
        if (LeadTime != null) {
            stmt.bindLong(7, LeadTime);
        }
 
        String PoDate = entity.getPoDate();
        if (PoDate != null) {
            stmt.bindString(8, PoDate);
        }
 
        String Storloc = entity.getStorloc();
        if (Storloc != null) {
            stmt.bindString(9, Storloc);
        }
 
        String CreatedDate = entity.getCreatedDate();
        if (CreatedDate != null) {
            stmt.bindString(10, CreatedDate);
        }
 
        String PlanCodeNo = entity.getPlanCodeNo();
        if (PlanCodeNo != null) {
            stmt.bindString(11, PlanCodeNo);
        }
 
        String PlantName = entity.getPlantName();
        if (PlantName != null) {
            stmt.bindString(12, PlantName);
        }
 
        String NoDoMims = entity.getNoDoMims();
        if (NoDoMims != null) {
            stmt.bindString(13, NoDoMims);
        }
 
        String DoStatus = entity.getDoStatus();
        if (DoStatus != null) {
            stmt.bindString(14, DoStatus);
        }
 
        String Expeditions = entity.getExpeditions();
        if (Expeditions != null) {
            stmt.bindString(15, Expeditions);
        }
 
        String CourierPersonName = entity.getCourierPersonName();
        if (CourierPersonName != null) {
            stmt.bindString(16, CourierPersonName);
        }
 
        String KdPabrikan = entity.getKdPabrikan();
        if (KdPabrikan != null) {
            stmt.bindString(17, KdPabrikan);
        }
 
        String MaterialGroup = entity.getMaterialGroup();
        if (MaterialGroup != null) {
            stmt.bindString(18, MaterialGroup);
        }
 
        String NamaKategoriMaterial = entity.getNamaKategoriMaterial();
        if (NamaKategoriMaterial != null) {
            stmt.bindString(19, NamaKategoriMaterial);
        }
 
        String RatingPenerimaan = entity.getRatingPenerimaan();
        if (RatingPenerimaan != null) {
            stmt.bindString(20, RatingPenerimaan);
        }
 
        String DescPenerimaan = entity.getDescPenerimaan();
        if (DescPenerimaan != null) {
            stmt.bindString(21, DescPenerimaan);
        }
 
        String RatingQuality = entity.getRatingQuality();
        if (RatingQuality != null) {
            stmt.bindString(22, RatingQuality);
        }
 
        String DescQuality = entity.getDescQuality();
        if (DescQuality != null) {
            stmt.bindString(23, DescQuality);
        }
 
        String RatingWaktu = entity.getRatingWaktu();
        if (RatingWaktu != null) {
            stmt.bindString(24, RatingWaktu);
        }
 
        String DescWaktu = entity.getDescWaktu();
        if (DescWaktu != null) {
            stmt.bindString(25, DescWaktu);
        }
 
        String TanggalDiterima = entity.getTanggalDiterima();
        if (TanggalDiterima != null) {
            stmt.bindString(26, TanggalDiterima);
        }
 
        String PetugasPenerima = entity.getPetugasPenerima();
        if (PetugasPenerima != null) {
            stmt.bindString(27, PetugasPenerima);
        }
 
        String KodeStatusDoMims = entity.getKodeStatusDoMims();
        if (KodeStatusDoMims != null) {
            stmt.bindString(28, KodeStatusDoMims);
        }
 
        String StatusPemeriksaan = entity.getStatusPemeriksaan();
        if (StatusPemeriksaan != null) {
            stmt.bindString(29, StatusPemeriksaan);
        }
 
        String StatusPenerimaan = entity.getStatusPenerimaan();
        if (StatusPenerimaan != null) {
            stmt.bindString(30, StatusPenerimaan);
        }
 
        String KurirPengantar = entity.getKurirPengantar();
        if (KurirPengantar != null) {
            stmt.bindString(31, KurirPengantar);
        }
 
        String NilaiRatingPenerimaan = entity.getNilaiRatingPenerimaan();
        if (NilaiRatingPenerimaan != null) {
            stmt.bindString(32, NilaiRatingPenerimaan);
        }
 
        String NilaiRatingWaktu = entity.getNilaiRatingWaktu();
        if (NilaiRatingWaktu != null) {
            stmt.bindString(33, NilaiRatingWaktu);
        }
 
        String NilaiRatingQuality = entity.getNilaiRatingQuality();
        if (NilaiRatingQuality != null) {
            stmt.bindString(34, NilaiRatingQuality);
        }
 
        String DoLineItem = entity.getDoLineItem();
        if (DoLineItem != null) {
            stmt.bindString(35, DoLineItem);
        }
 
        Integer BisaTerima = entity.getBisaTerima();
        if (BisaTerima != null) {
            stmt.bindLong(36, BisaTerima);
        }
 
        Integer IsRating = entity.getIsRating();
        if (IsRating != null) {
            stmt.bindLong(37, IsRating);
        }
 
        Integer isDone = entity.getIsDone();
        if (isDone != null) {
            stmt.bindLong(38, isDone);
        }
 
        Integer ratingDone = entity.getRatingDone();
        if (ratingDone != null) {
            stmt.bindLong(39, ratingDone);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TPosPenerimaan readEntity(Cursor cursor, int offset) {
        TPosPenerimaan entity = new TPosPenerimaan( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Total
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // TlskNo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // PoSapNo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // PoMpNo
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // NoDoSmar
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // LeadTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // PoDate
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Storloc
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // CreatedDate
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // PlanCodeNo
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // PlantName
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // NoDoMims
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // DoStatus
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // Expeditions
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // CourierPersonName
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // KdPabrikan
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // MaterialGroup
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // NamaKategoriMaterial
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // RatingPenerimaan
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // DescPenerimaan
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // RatingQuality
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // DescQuality
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // RatingWaktu
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // DescWaktu
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // TanggalDiterima
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // PetugasPenerima
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // KodeStatusDoMims
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // StatusPemeriksaan
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // StatusPenerimaan
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // KurirPengantar
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // NilaiRatingPenerimaan
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // NilaiRatingWaktu
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // NilaiRatingQuality
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34), // DoLineItem
            cursor.isNull(offset + 35) ? null : cursor.getInt(offset + 35), // BisaTerima
            cursor.isNull(offset + 36) ? null : cursor.getInt(offset + 36), // IsRating
            cursor.isNull(offset + 37) ? null : cursor.getInt(offset + 37), // isDone
            cursor.isNull(offset + 38) ? null : cursor.getInt(offset + 38) // ratingDone
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TPosPenerimaan entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTotal(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTlskNo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPoSapNo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPoMpNo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNoDoSmar(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLeadTime(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setPoDate(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setStorloc(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCreatedDate(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPlanCodeNo(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPlantName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setNoDoMims(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setDoStatus(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setExpeditions(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setCourierPersonName(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setKdPabrikan(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setMaterialGroup(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setNamaKategoriMaterial(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setRatingPenerimaan(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setDescPenerimaan(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setRatingQuality(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setDescQuality(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setRatingWaktu(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setDescWaktu(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setTanggalDiterima(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setPetugasPenerima(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setKodeStatusDoMims(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setStatusPemeriksaan(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setStatusPenerimaan(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setKurirPengantar(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setNilaiRatingPenerimaan(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setNilaiRatingWaktu(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setNilaiRatingQuality(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setDoLineItem(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
        entity.setBisaTerima(cursor.isNull(offset + 35) ? null : cursor.getInt(offset + 35));
        entity.setIsRating(cursor.isNull(offset + 36) ? null : cursor.getInt(offset + 36));
        entity.setIsDone(cursor.isNull(offset + 37) ? null : cursor.getInt(offset + 37));
        entity.setRatingDone(cursor.isNull(offset + 38) ? null : cursor.getInt(offset + 38));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TPosPenerimaan entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TPosPenerimaan entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TPosPenerimaan entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
