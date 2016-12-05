package com.mogujie.tt.DB.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.mogujie.tt.DB.entity.TravelEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TravelInfo.
*/
public class TravelDao extends AbstractDao<TravelEntity, Long> {

    public static final String TABLENAME = "TravelInfo";

    /**
     * Properties of entity TravelEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PeerId = new Property(1, int.class, "peerId", false, "PEER_ID");
        public final static Property Duration = new Property(2, int.class, "duration", false, "DURATION");
        public final static Property StartDate = new Property(3, String.class, "startDate", false, "START_DATE");
        public final static Property EndDate = new Property(4, String.class, "endDate", false, "END_DATE");
        public final static Property Destination = new Property(5, String.class, "destination", false, "DESTINATION");
        public final static Property DestinationBK = new Property(6, String.class, "destinationBK", false, "DESTINATION_BK");
        public final static Property ThroughPoint = new Property(7, String.class, "throughPoint", false, "THROUGH_POINT");
        public final static Property CreatorId = new Property(8, int.class, "creatorId", false, "CREATOR_ID");
        public final static Property UserCnt = new Property(9, int.class, "userCnt", false, "USER_CNT");
        public final static Property Cost = new Property(10, int.class, "cost", false, "COST");
        public final static Property Type = new Property(11, int.class, "type", false, "TYPE");
        public final static Property Version = new Property(12, int.class, "version", false, "VERSION");
        public final static Property Status = new Property(13, int.class, "status", false, "STATUS");
        public final static Property Created = new Property(14, int.class, "created", false, "CREATED");
        public final static Property Updated = new Property(15, int.class, "updated", false, "UPDATED");
    };


    public TravelDao(DaoConfig config) {
        super(config);
    }
    
    public TravelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TravelInfo' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'PEER_ID' INTEGER NOT NULL UNIQUE ," + // 1: peerId
                "'DURATION' INTEGER NOT NULL ," + // 2: duration
                "'START_DATE' TEXT NOT NULL ," + // 3: startDate
                "'END_DATE' TEXT NOT NULL ," + // 4: endDate
                "'DESTINATION' TEXT NOT NULL ," + // 5: destination
                "'DESTINATION_BK' TEXT NOT NULL ," + // 6: destinationBK
                "'THROUGH_POINT' TEXT NOT NULL ," + // 7: throughPoint
                "'CREATOR_ID' INTEGER NOT NULL ," + // 8: creatorId
                "'USER_CNT' INTEGER NOT NULL ," + // 9: userCnt
                "'COST' INTEGER NOT NULL ," + // 10: cost
                "'TYPE' INTEGER NOT NULL ," + // 11: type
                "'VERSION' INTEGER NOT NULL ," + // 12: version
                "'STATUS' INTEGER NOT NULL ," + // 13: status
                "'CREATED' INTEGER NOT NULL ," + // 14: created
                "'UPDATED' INTEGER NOT NULL );"); // 15: updated
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TravelInfo'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TravelEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getPeerId());
        stmt.bindLong(3, entity.getDuration());
        stmt.bindString(4, entity.getStartDate());
        stmt.bindString(5, entity.getEndDate());
        stmt.bindString(6, entity.getDestination());
        stmt.bindString(7, entity.getDestinationBK());
        stmt.bindString(8, entity.getThroughPoint());
        stmt.bindLong(9, entity.getCreatorId());
        stmt.bindLong(10, entity.getUserCnt());
        stmt.bindLong(11, entity.getCost());
        stmt.bindLong(12, entity.getType());
        stmt.bindLong(13, entity.getVersion());
        stmt.bindLong(14, entity.getStatus());
        stmt.bindLong(15, entity.getCreated());
        stmt.bindLong(16, entity.getUpdated());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TravelEntity readEntity(Cursor cursor, int offset) {
        TravelEntity entity = new TravelEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // peerId
            cursor.getInt(offset + 2), // duration
            cursor.getString(offset + 3), // startDate
            cursor.getString(offset + 4), // endDate
            cursor.getString(offset + 5), // destination
            cursor.getString(offset + 6), // destinationBK
            cursor.getString(offset + 7), // throughPoint
            cursor.getInt(offset + 8), // creatorId
            cursor.getInt(offset + 9), // userCnt
            cursor.getInt(offset + 10), // cost
            cursor.getInt(offset + 11), // type
            cursor.getInt(offset + 12), // version
            cursor.getInt(offset + 13), // status
            cursor.getInt(offset + 14), // created
            cursor.getInt(offset + 15) // updated
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TravelEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPeerId(cursor.getInt(offset + 1));
        entity.setDuration(cursor.getInt(offset + 2));
        entity.setStartDate(cursor.getString(offset + 3));
        entity.setEndDate(cursor.getString(offset + 4));
        entity.setDestination(cursor.getString(offset + 5));
        entity.setDestinationBK(cursor.getString(offset + 6));
        entity.setThroughPoint(cursor.getString(offset + 7));
        entity.setCreatorId(cursor.getInt(offset + 8));
        entity.setUserCnt(cursor.getInt(offset + 9));
        entity.setCost(cursor.getInt(offset + 10));
        entity.setType(cursor.getInt(offset + 11));
        entity.setVersion(cursor.getInt(offset + 12));
        entity.setStatus(cursor.getInt(offset + 13));
        entity.setCreated(cursor.getInt(offset + 14));
        entity.setUpdated(cursor.getInt(offset + 15));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TravelEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TravelEntity entity) {
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
