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
 * DAO for table "TLIST_SN_MATERIAL_PENERIMAAN_ULP".
*/
public class TListSnMaterialPenerimaanUlpDao extends AbstractDao<TListSnMaterialPenerimaanUlp, Long> {

    public static final String TABLENAME = "TLIST_SN_MATERIAL_PENERIMAAN_ULP";

    /**
     * Properties of entity TListSnMaterialPenerimaanUlp.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NoRepackaging = new Property(1, String.class, "NoRepackaging", false, "NO_REPACKAGING");
        public final static Property NoMaterial = new Property(2, String.class, "NoMaterial", false, "NO_MATERIAL");
        public final static Property NoSerialNumber = new Property(3, String.class, "NoSerialNumber", false, "NO_SERIAL_NUMBER");
        public final static Property Status = new Property(4, String.class, "Status", false, "STATUS");
        public final static Property IsScanned = new Property(5, Integer.class, "IsScanned", false, "IS_SCANNED");
    }


    public TListSnMaterialPenerimaanUlpDao(DaoConfig config) {
        super(config);
    }
    
    public TListSnMaterialPenerimaanUlpDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TLIST_SN_MATERIAL_PENERIMAAN_ULP\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NO_REPACKAGING\" TEXT," + // 1: NoRepackaging
                "\"NO_MATERIAL\" TEXT," + // 2: NoMaterial
                "\"NO_SERIAL_NUMBER\" TEXT," + // 3: NoSerialNumber
                "\"STATUS\" TEXT," + // 4: Status
                "\"IS_SCANNED\" INTEGER);"); // 5: IsScanned
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TLIST_SN_MATERIAL_PENERIMAAN_ULP\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TListSnMaterialPenerimaanUlp entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String NoRepackaging = entity.getNoRepackaging();
        if (NoRepackaging != null) {
            stmt.bindString(2, NoRepackaging);
        }
 
        String NoMaterial = entity.getNoMaterial();
        if (NoMaterial != null) {
            stmt.bindString(3, NoMaterial);
        }
 
        String NoSerialNumber = entity.getNoSerialNumber();
        if (NoSerialNumber != null) {
            stmt.bindString(4, NoSerialNumber);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(5, Status);
        }
 
        Integer IsScanned = entity.getIsScanned();
        if (IsScanned != null) {
            stmt.bindLong(6, IsScanned);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TListSnMaterialPenerimaanUlp entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String NoRepackaging = entity.getNoRepackaging();
        if (NoRepackaging != null) {
            stmt.bindString(2, NoRepackaging);
        }
 
        String NoMaterial = entity.getNoMaterial();
        if (NoMaterial != null) {
            stmt.bindString(3, NoMaterial);
        }
 
        String NoSerialNumber = entity.getNoSerialNumber();
        if (NoSerialNumber != null) {
            stmt.bindString(4, NoSerialNumber);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(5, Status);
        }
 
        Integer IsScanned = entity.getIsScanned();
        if (IsScanned != null) {
            stmt.bindLong(6, IsScanned);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TListSnMaterialPenerimaanUlp readEntity(Cursor cursor, int offset) {
        TListSnMaterialPenerimaanUlp entity = new TListSnMaterialPenerimaanUlp( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // NoRepackaging
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // NoMaterial
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // NoSerialNumber
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Status
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5) // IsScanned
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TListSnMaterialPenerimaanUlp entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNoRepackaging(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNoMaterial(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNoSerialNumber(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsScanned(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TListSnMaterialPenerimaanUlp entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TListSnMaterialPenerimaanUlp entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TListSnMaterialPenerimaanUlp entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}