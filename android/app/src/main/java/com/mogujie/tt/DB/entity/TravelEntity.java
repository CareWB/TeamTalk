package com.mogujie.tt.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TravelInfo.
 */
public class TravelEntity {

    private Long id;
    private int peerId;
    private int duration;
    /** Not-null value. */
    private String startDate;
    /** Not-null value. */
    private String endDate;
    /** Not-null value. */
    private String destination;
    /** Not-null value. */
    private String destinationBK;
    /** Not-null value. */
    private String throughPoint;
    private int creatorId;
    private int userCnt;
    private int cost;
    private int type;
    private int version;
    private int status;
    private int created;
    private int updated;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public TravelEntity() {
    }

    public TravelEntity(Long id) {
        this.id = id;
    }

    public TravelEntity(Long id, int peerId, int duration, String startDate, String endDate, String destination, String destinationBK, String throughPoint, int creatorId, int userCnt, int cost, int type, int version, int status, int created, int updated) {
        this.id = id;
        this.peerId = peerId;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.destinationBK = destinationBK;
        this.throughPoint = throughPoint;
        this.creatorId = creatorId;
        this.userCnt = userCnt;
        this.cost = cost;
        this.type = type;
        this.version = version;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPeerId() {
        return peerId;
    }

    public void setPeerId(int peerId) {
        this.peerId = peerId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /** Not-null value. */
    public String getStartDate() {
        return startDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /** Not-null value. */
    public String getEndDate() {
        return endDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /** Not-null value. */
    public String getDestination() {
        return destination;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /** Not-null value. */
    public String getDestinationBK() {
        return destinationBK;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDestinationBK(String destinationBK) {
        this.destinationBK = destinationBK;
    }

    /** Not-null value. */
    public String getThroughPoint() {
        return throughPoint;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setThroughPoint(String throughPoint) {
        this.throughPoint = throughPoint;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getUserCnt() {
        return userCnt;
    }

    public void setUserCnt(int userCnt) {
        this.userCnt = userCnt;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}