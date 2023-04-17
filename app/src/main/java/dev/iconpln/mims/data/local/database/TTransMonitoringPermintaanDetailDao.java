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
 * DAO for table "TTRANS_MONITORING_PERMINTAAN_DETAIL".
*/
public class TTransMonitoringPermintaanDetailDao extends AbstractDao<TTransMonitoringPermintaanDetail, Long> {

    public static final String TABLENAME = "TTRANS_MONITORING_PERMINTAAN_DETAIL";

    /**
     * Properties of entity TTransMonitoringPermintaanDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NoPermintaan = new Property(1, String.class, "NoPermintaan", false, "NO_PERMINTAAN");
        public final static Property NoTransaksi = new Property(2, String.class, "NoTransaksi", false, "NO_TRANSAKSI");
        public final static Property NoRepackaging = new Property(3, String.class, "NoRepackaging", false, "NO_REPACKAGING");
        public final static Property NomorMaterial = new Property(4, String.class, "NomorMaterial", false, "NOMOR_MATERIAL");
        public final static Property Unit = new Property(5, String.class, "Unit", false, "UNIT");
        public final static Property QtyPermintaan = new Property(6, Integer.class, "QtyPermintaan", false, "QTY_PERMINTAAN");
        public final static Property MaterialDesc = new Property(7, String.class, "MaterialDesc", false, "MATERIAL_DESC");
        public final static Property QtyScan = new Property(8, String.class, "QtyScan", false, "QTY_SCAN");
        public final static Property Kategori = new Property(9, String.class, "Kategori", false, "KATEGORI");
        public final static Property QtyPengeluaran = new Property(10, String.class, "QtyPengeluaran", false, "QTY_PENGELUARAN");
        public final static Property QtyAkanDiScan = new Property(11, Integer.class, "QtyAkanDiScan", false, "QTY_AKAN_DI_SCAN");
        public final static Property IsScannedSn = new Property(12, Integer.class, "IsScannedSn", false, "IS_SCANNED_SN");
        public final static Property IsDone = new Property(13, Integer.class, "IsDone", false, "IS_DONE");
    }


    public TTransMonitoringPermintaanDetailDao(DaoConfig config) {
        super(config);
    }
    
    public TTransMonitoringPermintaanDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TTRANS_MONITORING_PERMINTAAN_DETAIL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NO_PERMINTAAN\" TEXT," + // 1: NoPermintaan
                "\"NO_TRANSAKSI\" TEXT," + // 2: NoTransaksi
                "\"NO_REPACKAGING\" TEXT," + // 3: NoRepackaging
                "\"NOMOR_MATERIAL\" TEXT," + // 4: NomorMaterial
                "\"UNIT\" TEXT," + // 5: Unit
                "\"QTY_PERMINTAAN\" INTEGER," + // 6: QtyPermintaan
                "\"MATERIAL_DESC\" TEXT," + // 7: MaterialDesc
                "\"QTY_SCAN\" TEXT," + // 8: QtyScan
                "\"KATEGORI\" TEXT," + // 9: Kategori
                "\"QTY_PENGELUARAN\" TEXT," + // 10: QtyPengeluaran
                "\"QTY_AKAN_DI_SCAN\" INTEGER," + // 11: QtyAkanDiScan
                "\"IS_SCANNED_SN\" INTEGER," + // 12: IsScannedSn
                "\"IS_DONE\" INTEGER);"); // 13: IsDone
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TTRANS_MONITORING_PERMINTAAN_DETAIL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TTransMonitoringPermintaanDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String NoPermintaan = entity.getNoPermintaan();
        if (NoPermintaan != null) {
            stmt.bindString(2, NoPermintaan);
        }
 
        String NoTransaksi = entity.getNoTransaksi();
        if (NoTransaksi != null) {
            stmt.bindString(3, NoTransaksi);
        }
 
        String NoRepackaging = entity.getNoRepackaging();
        if (NoRepackaging != null) {
            stmt.bindString(4, NoRepackaging);
        }
 
        String NomorMaterial = entity.getNomorMaterial();
        if (NomorMaterial != null) {
            stmt.bindString(5, NomorMaterial);
        }
 
        String Unit = entity.getUnit();
        if (Unit != null) {
            stmt.bindString(6, Unit);
        }
 
        Integer QtyPermintaan = entity.getQtyPermintaan();
        if (QtyPermintaan != null) {
            stmt.bindLong(7, QtyPermintaan);
        }
 
        String MaterialDesc = entity.getMaterialDesc();
        if (MaterialDesc != null) {
            stmt.bindString(8, MaterialDesc);
        }
 
        String QtyScan = entity.getQtyScan();
        if (QtyScan != null) {
            stmt.bindString(9, QtyScan);
        }
 
        String Kategori = entity.getKategori();
        if (Kategori != null) {
            stmt.bindString(10, Kategori);
        }
 
        String QtyPengeluaran = entity.getQtyPengeluaran();
        if (QtyPengeluaran != null) {
            stmt.bindString(11, QtyPengeluaran);
        }
 
        Integer QtyAkanDiScan = entity.getQtyAkanDiScan();
        if (QtyAkanDiScan != null) {
            stmt.bindLong(12, QtyAkanDiScan);
        }
 
        Integer IsScannedSn = entity.getIsScannedSn();
        if (IsScannedSn != null) {
            stmt.bindLong(13, IsScannedSn);
        }
 
        Integer IsDone = entity.getIsDone();
        if (IsDone != null) {
            stmt.bindLong(14, IsDone);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TTransMonitoringPermintaanDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String NoPermintaan = entity.getNoPermintaan();
        if (NoPermintaan != null) {
            stmt.bindString(2, NoPermintaan);
        }
 
        String NoTransaksi = entity.getNoTransaksi();
        if (NoTransaksi != null) {
            stmt.bindString(3, NoTransaksi);
        }
 
        String NoRepackaging = entity.getNoRepackaging();
        if (NoRepackaging != null) {
            stmt.bindString(4, NoRepackaging);
        }
 
        String NomorMaterial = entity.getNomorMaterial();
        if (NomorMaterial != null) {
            stmt.bindString(5, NomorMaterial);
        }
 
        String Unit = entity.getUnit();
        if (Unit != null) {
            stmt.bindString(6, Unit);
        }
 
        Integer QtyPermintaan = entity.getQtyPermintaan();
        if (QtyPermintaan != null) {
            stmt.bindLong(7, QtyPermintaan);
        }
 
        String MaterialDesc = entity.getMaterialDesc();
        if (MaterialDesc != null) {
            stmt.bindString(8, MaterialDesc);
        }
 
        String QtyScan = entity.getQtyScan();
        if (QtyScan != null) {
            stmt.bindString(9, QtyScan);
        }
 
        String Kategori = entity.getKategori();
        if (Kategori != null) {
            stmt.bindString(10, Kategori);
        }
 
        String QtyPengeluaran = entity.getQtyPengeluaran();
        if (QtyPengeluaran != null) {
            stmt.bindString(11, QtyPengeluaran);
        }
 
        Integer QtyAkanDiScan = entity.getQtyAkanDiScan();
        if (QtyAkanDiScan != null) {
            stmt.bindLong(12, QtyAkanDiScan);
        }
 
        Integer IsScannedSn = entity.getIsScannedSn();
        if (IsScannedSn != null) {
            stmt.bindLong(13, IsScannedSn);
        }
 
        Integer IsDone = entity.getIsDone();
        if (IsDone != null) {
            stmt.bindLong(14, IsDone);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TTransMonitoringPermintaanDetail readEntity(Cursor cursor, int offset) {
        TTransMonitoringPermintaanDetail entity = new TTransMonitoringPermintaanDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // NoPermintaan
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // NoTransaksi
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // NoRepackaging
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // NomorMaterial
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Unit
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // QtyPermintaan
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // MaterialDesc
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // QtyScan
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // Kategori
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // QtyPengeluaran
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // QtyAkanDiScan
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // IsScannedSn
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13) // IsDone
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TTransMonitoringPermintaanDetail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNoPermintaan(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNoTransaksi(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNoRepackaging(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNomorMaterial(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUnit(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setQtyPermintaan(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setMaterialDesc(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setQtyScan(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setKategori(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setQtyPengeluaran(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setQtyAkanDiScan(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setIsScannedSn(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setIsDone(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TTransMonitoringPermintaanDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TTransMonitoringPermintaanDetail entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TTransMonitoringPermintaanDetail entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
