package com.mogujie.tt.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table HotelInfo.
 */
public class HotelEntity {

    private Long id;
    private int peerId;
    /** Not-null value. */
    private String name;
    /** Not-null value. */
    private String pic;
    private int star;
    /** Not-null value. */
    private String tag;
    private int optimize;
    private int price;
    private int select;
    private int focus;
    private int version;
    private int status;
    private int created;
    private int updated;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public HotelEntity() {
    }

    public HotelEntity(Long id) {
        this.id = id;
    }

    public HotelEntity(Long id, int peerId, String name, String pic, int star, String tag, int optimize, int price, int select, int focus, int version, int status, int created, int updated) {
        this.id = id;
        this.peerId = peerId;
        this.name = name;
        this.pic = pic;
        this.star = star;
        this.tag = tag;
        this.optimize = optimize;
        this.price = price;
        this.select = select;
        this.focus = focus;
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

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    /** Not-null value. */
    public String getPic() {
        return pic;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    /** Not-null value. */
    public String getTag() {
        return tag;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getOptimize() {
        return optimize;
    }

    public void setOptimize(int optimize) {
        this.optimize = optimize;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
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