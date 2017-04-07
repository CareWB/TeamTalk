package com.zhizulx.tt.DB.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.zhizulx.tt.DB.entity.HotelEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table HotelInfo.
*/
public class HotelDao extends AbstractDao<HotelEntity, Long> {

    public static final String TABLENAME = "HotelInfo";

    /**
     * Properties of entity HotelEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PeerId = new Property(1, int.class, "peerId", false, "PEER_ID");
        public final static Property CityCode = new Property(2, String.class, "cityCode", false, "CITY_CODE");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Pic = new Property(4, String.class, "pic", false, "PIC");
        public final static Property Star = new Property(5, int.class, "star", false, "STAR");
        public final static Property Tag = new Property(6, String.class, "tag", false, "TAG");
        public final static Property Url = new Property(7, String.class, "url", false, "URL");
        public final static Property Price = new Property(8, int.class, "price", false, "PRICE");
        public final static Property Longitude = new Property(9, double.class, "longitude", false, "LONGITUDE");
        public final static Property Latitude = new Property(10, double.class, "latitude", false, "LATITUDE");
        public final static Property StartTime = new Property(11, String.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(12, String.class, "endTime", false, "END_TIME");
        public final static Property Distance = new Property(13, int.class, "distance", false, "DISTANCE");
        public final static Property Select = new Property(14, int.class, "select", false, "SELECT");
        public final static Property Version = new Property(15, int.class, "version", false, "VERSION");
        public final static Property Status = new Property(16, int.class, "status", false, "STATUS");
        public final static Property Created = new Property(17, int.class, "created", false, "CREATED");
        public final static Property Updated = new Property(18, int.class, "updated", false, "UPDATED");
    };


    public HotelDao(DaoConfig config) {
        super(config);
    }
    
    public HotelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HotelInfo' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'PEER_ID' INTEGER NOT NULL UNIQUE ," + // 1: peerId
                "'CITY_CODE' TEXT NOT NULL ," + // 2: cityCode
                "'NAME' TEXT NOT NULL ," + // 3: name
                "'PIC' TEXT NOT NULL ," + // 4: pic
                "'STAR' INTEGER NOT NULL ," + // 5: star
                "'TAG' TEXT NOT NULL ," + // 6: tag
                "'URL' TEXT NOT NULL ," + // 7: url
                "'PRICE' INTEGER NOT NULL ," + // 8: price
                "'LONGITUDE' REAL NOT NULL ," + // 9: longitude
                "'LATITUDE' REAL NOT NULL ," + // 10: latitude
                "'START_TIME' TEXT NOT NULL ," + // 11: startTime
                "'END_TIME' TEXT NOT NULL ," + // 12: endTime
                "'DISTANCE' INTEGER NOT NULL ," + // 13: distance
                "'SELECT' INTEGER NOT NULL ," + // 14: select
                "'VERSION' INTEGER NOT NULL ," + // 15: version
                "'STATUS' INTEGER NOT NULL ," + // 16: status
                "'CREATED' INTEGER NOT NULL ," + // 17: created
                "'UPDATED' INTEGER NOT NULL );"); // 18: updated
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HotelInfo'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HotelEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getPeerId());
        stmt.bindString(3, entity.getCityCode());
        stmt.bindString(4, entity.getName());
        stmt.bindString(5, entity.getPic());
        stmt.bindLong(6, entity.getStar());
        stmt.bindString(7, entity.getTag());
        stmt.bindString(8, entity.getUrl());
        stmt.bindLong(9, entity.getPrice());
        stmt.bindDouble(10, entity.getLongitude());
        stmt.bindDouble(11, entity.getLatitude());
        stmt.bindString(12, entity.getStartTime());
        stmt.bindString(13, entity.getEndTime());
        stmt.bindLong(14, entity.getDistance());
        stmt.bindLong(15, entity.getSelect());
        stmt.bindLong(16, entity.getVersion());
        stmt.bindLong(17, entity.getStatus());
        stmt.bindLong(18, entity.getCreated());
        stmt.bindLong(19, entity.getUpdated());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public HotelEntity readEntity(Cursor cursor, int offset) {
        HotelEntity entity = new HotelEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // peerId
            cursor.getString(offset + 2), // cityCode
            cursor.getString(offset + 3), // name
            cursor.getString(offset + 4), // pic
            cursor.getInt(offset + 5), // star
            cursor.getString(offset + 6), // tag
            cursor.getString(offset + 7), // url
            cursor.getInt(offset + 8), // price
            cursor.getDouble(offset + 9), // longitude
            cursor.getDouble(offset + 10), // latitude
            cursor.getString(offset + 11), // startTime
            cursor.getString(offset + 12), // endTime
            cursor.getInt(offset + 13), // distance
            cursor.getInt(offset + 14), // select
            cursor.getInt(offset + 15), // version
            cursor.getInt(offset + 16), // status
            cursor.getInt(offset + 17), // created
            cursor.getInt(offset + 18) // updated
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HotelEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPeerId(cursor.getInt(offset + 1));
        entity.setCityCode(cursor.getString(offset + 2));
        entity.setName(cursor.getString(offset + 3));
        entity.setPic(cursor.getString(offset + 4));
        entity.setStar(cursor.getInt(offset + 5));
        entity.setTag(cursor.getString(offset + 6));
        entity.setUrl(cursor.getString(offset + 7));
        entity.setPrice(cursor.getInt(offset + 8));
        entity.setLongitude(cursor.getDouble(offset + 9));
        entity.setLatitude(cursor.getDouble(offset + 10));
        entity.setStartTime(cursor.getString(offset + 11));
        entity.setEndTime(cursor.getString(offset + 12));
        entity.setDistance(cursor.getInt(offset + 13));
        entity.setSelect(cursor.getInt(offset + 14));
        entity.setVersion(cursor.getInt(offset + 15));
        entity.setStatus(cursor.getInt(offset + 16));
        entity.setCreated(cursor.getInt(offset + 17));
        entity.setUpdated(cursor.getInt(offset + 18));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(HotelEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(HotelEntity entity) {
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
