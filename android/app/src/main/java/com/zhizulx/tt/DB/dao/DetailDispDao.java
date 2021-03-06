package com.zhizulx.tt.DB.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.zhizulx.tt.DB.entity.DetailDispEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DetailDispInfo.
*/
public class DetailDispDao extends AbstractDao<DetailDispEntity, Long> {

    public static final String TABLENAME = "DetailDispInfo";

    /**
     * Properties of entity DetailDispEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DbID = new Property(1, int.class, "dbID", false, "DB_ID");
        public final static Property Type = new Property(2, int.class, "type", false, "TYPE");
        public final static Property Image = new Property(3, String.class, "image", false, "IMAGE");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property CityCode = new Property(5, String.class, "cityCode", false, "CITY_CODE");
        public final static Property Time = new Property(6, String.class, "time", false, "TIME");
        public final static Property Edited = new Property(7, int.class, "edited", false, "EDITED");
        public final static Property Version = new Property(8, int.class, "version", false, "VERSION");
        public final static Property Status = new Property(9, int.class, "status", false, "STATUS");
        public final static Property Created = new Property(10, int.class, "created", false, "CREATED");
        public final static Property Updated = new Property(11, int.class, "updated", false, "UPDATED");
    };


    public DetailDispDao(DaoConfig config) {
        super(config);
    }
    
    public DetailDispDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DetailDispInfo' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'DB_ID' INTEGER NOT NULL ," + // 1: dbID
                "'TYPE' INTEGER NOT NULL ," + // 2: type
                "'IMAGE' TEXT NOT NULL ," + // 3: image
                "'TITLE' TEXT NOT NULL ," + // 4: title
                "'CITY_CODE' TEXT NOT NULL ," + // 5: cityCode
                "'TIME' TEXT NOT NULL ," + // 6: time
                "'EDITED' INTEGER NOT NULL ," + // 7: edited
                "'VERSION' INTEGER NOT NULL ," + // 8: version
                "'STATUS' INTEGER NOT NULL ," + // 9: status
                "'CREATED' INTEGER NOT NULL ," + // 10: created
                "'UPDATED' INTEGER NOT NULL );"); // 11: updated
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DetailDispInfo'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DetailDispEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getDbID());
        stmt.bindLong(3, entity.getType());
        stmt.bindString(4, entity.getImage());
        stmt.bindString(5, entity.getTitle());
        stmt.bindString(6, entity.getCityCode());
        stmt.bindString(7, entity.getTime());
        stmt.bindLong(8, entity.getEdited());
        stmt.bindLong(9, entity.getVersion());
        stmt.bindLong(10, entity.getStatus());
        stmt.bindLong(11, entity.getCreated());
        stmt.bindLong(12, entity.getUpdated());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DetailDispEntity readEntity(Cursor cursor, int offset) {
        DetailDispEntity entity = new DetailDispEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // dbID
            cursor.getInt(offset + 2), // type
            cursor.getString(offset + 3), // image
            cursor.getString(offset + 4), // title
            cursor.getString(offset + 5), // cityCode
            cursor.getString(offset + 6), // time
            cursor.getInt(offset + 7), // edited
            cursor.getInt(offset + 8), // version
            cursor.getInt(offset + 9), // status
            cursor.getInt(offset + 10), // created
            cursor.getInt(offset + 11) // updated
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DetailDispEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDbID(cursor.getInt(offset + 1));
        entity.setType(cursor.getInt(offset + 2));
        entity.setImage(cursor.getString(offset + 3));
        entity.setTitle(cursor.getString(offset + 4));
        entity.setCityCode(cursor.getString(offset + 5));
        entity.setTime(cursor.getString(offset + 6));
        entity.setEdited(cursor.getInt(offset + 7));
        entity.setVersion(cursor.getInt(offset + 8));
        entity.setStatus(cursor.getInt(offset + 9));
        entity.setCreated(cursor.getInt(offset + 10));
        entity.setUpdated(cursor.getInt(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DetailDispEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DetailDispEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
