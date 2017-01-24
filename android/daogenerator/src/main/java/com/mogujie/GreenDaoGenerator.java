package com.mogujie;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * @author : yingmu on 14-12-31.
 * @email : yingmu@mogujie.com.
 *
 * 其中UserEntity、 GroupEntity 继承PeerEntity
 * 由于 UserEntity、 GroupEntity是自动生成，PeerEntity会有重复字段，所以每次生成之后要处理下成员变量。
 * PeerEntity成员变量名与子类统一。
 *
 * 【备注】session表中的create与update字段没有特别的区分，主要是之前服务端的习惯。。。
 */
public class GreenDaoGenerator {
    private static String entityPath = "com.mogujie.tt.DB.entity";

    public static void main(String[] args) throws Exception {
        int dbVersion = 12;
        Schema schema = new Schema(dbVersion, "com.mogujie.tt.DB.dao");

        schema.enableKeepSectionsByDefault();
        addDepartment(schema);
        addUserInfo(schema);
        addGroupInfo(schema);
        addMessage(schema);
        addSessionInfo(schema);
        addTravelInfo(schema);
        addPlayConfigInfo(schema);
        addSightInfo(schema);
        addHotelInfo(schema);
        addTrafficInfo(schema);
        addDetailDispInfo(schema);
        addCityInfo(schema);

        // todo 绝对路径,根据自己的路径设定， 例子如下
        String path = "D:\\android\\dao";
        new DaoGenerator().generateAll(schema, path);
    }

    private static void addDepartment(Schema schema){
        Entity department = schema.addEntity("DepartmentEntity");
        department.setTableName("Department");
        department.setClassNameDao("DepartmentDao");
        department.setJavaPackage(entityPath);

        department.addIdProperty().autoincrement();
        department.addIntProperty("departId").unique().notNull().index();
        department.addStringProperty("departName").unique().notNull().index();
        department.addIntProperty("priority").notNull();
        department.addIntProperty("status").notNull();

        department.addIntProperty("created").notNull();
        department.addIntProperty("updated").notNull();

        department.setHasKeepSections(true);
    }

    private static void addUserInfo(Schema schema) {
        Entity userInfo = schema.addEntity("UserEntity");
        userInfo.setTableName("UserInfo");
        userInfo.setClassNameDao("UserDao");
        userInfo.setJavaPackage(entityPath);

        userInfo.addIdProperty().autoincrement();
        userInfo.addIntProperty("peerId").unique().notNull().index();
        userInfo.addIntProperty("gender").notNull();
        userInfo.addStringProperty("mainName").notNull();
        // 这个可以自动生成pinyin
        userInfo.addStringProperty("pinyinName").notNull();
        userInfo.addStringProperty("realName").notNull();
        userInfo.addStringProperty("avatar").notNull();
        userInfo.addStringProperty("phone").notNull();
        userInfo.addStringProperty("email").notNull();
        userInfo.addIntProperty("departmentId").notNull();

        userInfo.addIntProperty("status").notNull();
        userInfo.addIntProperty("created").notNull();
        userInfo.addIntProperty("updated").notNull();

        userInfo.setHasKeepSections(true);

        //todo 索引还没有设定
        // 一对一 addToOne 的使用
        // 支持protobuf
        // schema.addProtobufEntity();
    }

    private static void addGroupInfo(Schema schema) {
        Entity groupInfo = schema.addEntity("GroupEntity");
        groupInfo.setTableName("GroupInfo");
        groupInfo.setClassNameDao("GroupDao");
        groupInfo.setJavaPackage(entityPath);

        groupInfo.addIdProperty().autoincrement();
        groupInfo.addIntProperty("peerId").unique().notNull();
        groupInfo.addIntProperty("groupType").notNull();
        groupInfo.addStringProperty("mainName").notNull();
        groupInfo.addStringProperty("avatar").notNull();
        groupInfo.addIntProperty("creatorId").notNull();
        groupInfo.addIntProperty("userCnt").notNull();

        groupInfo.addStringProperty("userList").notNull();
        groupInfo.addIntProperty("version").notNull();
        groupInfo.addIntProperty("status").notNull();
        groupInfo.addIntProperty("created").notNull();
        groupInfo.addIntProperty("updated").notNull();

        groupInfo.setHasKeepSections(true);
    }

    private static void addMessage(Schema schema){
        Entity message = schema.addEntity("MessageEntity");
        message.setTableName("Message");
        message.setClassNameDao("MessageDao");
        message.setJavaPackage(entityPath);

        message.implementsSerializable();
        message.addIdProperty().autoincrement();
        Property msgProId = message.addIntProperty("msgId").notNull().getProperty();
        message.addIntProperty("fromId").notNull();
        message.addIntProperty("toId").notNull();
        // 是不是需要添加一个sessionkey标示一下，登陆的用户在前面
        Property sessionPro  = message.addStringProperty("sessionKey").notNull().getProperty();
        message.addStringProperty("content").notNull();
        message.addIntProperty("msgType").notNull();
        message.addIntProperty("displayType").notNull();

        message.addIntProperty("status").notNull().index();
        message.addIntProperty("created").notNull().index();
        message.addIntProperty("updated").notNull();

        Index index = new Index();
        index.addProperty(msgProId);
        index.addProperty(sessionPro);
        index.makeUnique();
        message.addIndex(index);

        message.setHasKeepSections(true);
    }

    private static void addSessionInfo(Schema schema){
        Entity sessionInfo = schema.addEntity("SessionEntity");
        sessionInfo.setTableName("Session");
        sessionInfo.setClassNameDao("SessionDao");
        sessionInfo.setJavaPackage(entityPath);

        //point to userId/groupId need sessionType 区分
        sessionInfo.addIdProperty().autoincrement();
        sessionInfo.addStringProperty("sessionKey").unique().notNull(); //.unique()
        sessionInfo.addIntProperty("peerId").notNull();
        sessionInfo.addIntProperty("peerType").notNull();

        sessionInfo.addIntProperty("latestMsgType").notNull();
        sessionInfo.addIntProperty("latestMsgId").notNull();
        sessionInfo.addStringProperty("latestMsgData").notNull();

        sessionInfo.addIntProperty("talkId").notNull();
        sessionInfo.addIntProperty("created").notNull();
        sessionInfo.addIntProperty("updated").notNull();

        sessionInfo.setHasKeepSections(true);
    }

    private static void addTravelInfo(Schema schema) {
        Entity travelInfo = schema.addEntity("TravelEntity");
        travelInfo.setTableName("TravelInfo");
        travelInfo.setClassNameDao("TravelDao");
        travelInfo.setJavaPackage(entityPath);

        travelInfo.addIdProperty().autoincrement();
        travelInfo.addIntProperty("dbId").unique().notNull();
        travelInfo.addIntProperty("creatorId").notNull();
        travelInfo.addIntProperty("duration").notNull();
        travelInfo.addStringProperty("startDate").notNull();
        travelInfo.addStringProperty("endDate").notNull();
        travelInfo.addStringProperty("startPlace").notNull();
        travelInfo.addStringProperty("endPlace").notNull();
        travelInfo.addStringProperty("destination").notNull();
        travelInfo.addStringProperty("destinationBK").notNull();
        travelInfo.addIntProperty("personNum").notNull();
        travelInfo.addIntProperty("trafficWay").notNull();
        travelInfo.addStringProperty("trafficStartTime").notNull();
        travelInfo.addStringProperty("trafficEndTime").notNull();
        travelInfo.addIntProperty("trafficQuality").notNull();
        travelInfo.addIntProperty("transit").notNull();
        travelInfo.addIntProperty("cost").notNull();
        travelInfo.addIntProperty("version").notNull();
        travelInfo.addIntProperty("status").notNull();
        travelInfo.addIntProperty("created").notNull();
        travelInfo.addIntProperty("updated").notNull();

        travelInfo.setHasKeepSections(true);
    }

    private static void addPlayConfigInfo(Schema schema) {
        Entity playConfiglInfo = schema.addEntity("PlayConfigEntity");
        playConfiglInfo.setTableName("PlayConfigInfo");
        playConfiglInfo.setClassNameDao("PlayConfigDao");
        playConfiglInfo.setJavaPackage(entityPath);

        playConfiglInfo.addIdProperty().autoincrement();
        playConfiglInfo.addStringProperty("cityName").notNull();
        playConfiglInfo.addIntProperty("quality").notNull();
        playConfiglInfo.addStringProperty("startTime").notNull();
        playConfiglInfo.addStringProperty("endTime").notNull();
        playConfiglInfo.addIntProperty("transportToolType").notNull();
        playConfiglInfo.addIntProperty("position").notNull();
        playConfiglInfo.addIntProperty("version").notNull();
        playConfiglInfo.addIntProperty("status").notNull();
        playConfiglInfo.addIntProperty("created").notNull();
        playConfiglInfo.addIntProperty("updated").notNull();

        playConfiglInfo.setHasKeepSections(true);
    }

    private static void addSightInfo(Schema schema) {
        Entity sightlInfo = schema.addEntity("SightEntity");
        sightlInfo.setTableName("SightInfo");
        sightlInfo.setClassNameDao("SightDao");
        sightlInfo.setJavaPackage(entityPath);

        sightlInfo.addIdProperty().autoincrement();
        sightlInfo.addIntProperty("peerId").unique().notNull();
        sightlInfo.addStringProperty("name").notNull();
        sightlInfo.addStringProperty("pic").notNull();
        sightlInfo.addIntProperty("star").notNull();
        sightlInfo.addStringProperty("tag").notNull();
        sightlInfo.addIntProperty("free").notNull();
        sightlInfo.addIntProperty("mustGo").notNull();
        sightlInfo.addStringProperty("url").notNull();
        sightlInfo.addIntProperty("playTime").notNull();
        sightlInfo.addIntProperty("price").notNull();
        sightlInfo.addStringProperty("bestStartTime").notNull();
        sightlInfo.addStringProperty("bestEndTime").notNull();
        sightlInfo.addIntProperty("select").notNull();
        sightlInfo.addIntProperty("version").notNull();
        sightlInfo.addIntProperty("status").notNull();
        sightlInfo.addIntProperty("created").notNull();
        sightlInfo.addIntProperty("updated").notNull();

        sightlInfo.setHasKeepSections(true);
    }

    private static void addHotelInfo(Schema schema) {
        Entity hotelInfo = schema.addEntity("HotelEntity");
        hotelInfo.setTableName("HotelInfo");
        hotelInfo.setClassNameDao("HotelDao");
        hotelInfo.setJavaPackage(entityPath);

        hotelInfo.addIdProperty().autoincrement();
        hotelInfo.addIntProperty("peerId").unique().notNull();
        hotelInfo.addStringProperty("name").notNull();
        hotelInfo.addStringProperty("pic").notNull();
        hotelInfo.addIntProperty("star").notNull();
        hotelInfo.addStringProperty("tag").notNull();
        hotelInfo.addIntProperty("mustGo").notNull();
        hotelInfo.addStringProperty("url").notNull();
        hotelInfo.addIntProperty("price").notNull();
        hotelInfo.addIntProperty("distance").notNull();
        hotelInfo.addIntProperty("select").notNull();
        hotelInfo.addIntProperty("version").notNull();
        hotelInfo.addIntProperty("status").notNull();
        hotelInfo.addIntProperty("created").notNull();
        hotelInfo.addIntProperty("updated").notNull();

        hotelInfo.setHasKeepSections(true);
    }

    private static void addTrafficInfo(Schema schema) {
        Entity trafficInfo = schema.addEntity("TrafficEntity");
        trafficInfo.setTableName("TrafficInfo");
        trafficInfo.setClassNameDao("TrafficDao");
        trafficInfo.setJavaPackage(entityPath);

        trafficInfo.addIdProperty().autoincrement();
        trafficInfo.addIntProperty("type").notNull();
        trafficInfo.addStringProperty("no").notNull();
        trafficInfo.addStringProperty("startCityCode").notNull();
        trafficInfo.addStringProperty("startStation").notNull();
        trafficInfo.addStringProperty("endCityCode").notNull();
        trafficInfo.addStringProperty("endStation").notNull();
        trafficInfo.addStringProperty("startTime").notNull();
        trafficInfo.addStringProperty("endTime").notNull();
        trafficInfo.addStringProperty("duration").notNull();
        trafficInfo.addIntProperty("price").notNull();
        trafficInfo.addStringProperty("seatClass").notNull();
        trafficInfo.addIntProperty("select").notNull();
        trafficInfo.addIntProperty("version").notNull();
        trafficInfo.addIntProperty("status").notNull();
        trafficInfo.addIntProperty("created").notNull();
        trafficInfo.addIntProperty("updated").notNull();

        trafficInfo.setHasKeepSections(true);
    }

    private static void addDetailDispInfo(Schema schema) {
        Entity detailDispInfo = schema.addEntity("DetailDispEntity");
        detailDispInfo.setTableName("DetailDispInfo");
        detailDispInfo.setClassNameDao("DetailDispDao");
        detailDispInfo.setJavaPackage(entityPath);

        detailDispInfo.addIdProperty().autoincrement();
        detailDispInfo.addIntProperty("type").notNull();
        detailDispInfo.addStringProperty("backgroud").notNull();
        detailDispInfo.addStringProperty("title").notNull();
        detailDispInfo.addStringProperty("content").notNull();
        detailDispInfo.addStringProperty("time").notNull();
        detailDispInfo.addIntProperty("version").notNull();
        detailDispInfo.addIntProperty("status").notNull();
        detailDispInfo.addIntProperty("created").notNull();
        detailDispInfo.addIntProperty("updated").notNull();

        detailDispInfo.setHasKeepSections(true);
    }

    private static void addCityInfo(Schema schema) {
        Entity cityInfo = schema.addEntity("CityEntity");
        cityInfo.setTableName("cityInfo");
        cityInfo.setClassNameDao("CityDao");
        cityInfo.setJavaPackage(entityPath);

        cityInfo.addIdProperty().autoincrement();
        cityInfo.addStringProperty("code").notNull();
        cityInfo.addStringProperty("name").notNull();
        cityInfo.addStringProperty("icon").notNull();
        cityInfo.addStringProperty("pic").notNull();
        cityInfo.addIntProperty("select").notNull();
        cityInfo.addIntProperty("version").notNull();
        cityInfo.addIntProperty("status").notNull();
        cityInfo.addIntProperty("created").notNull();
        cityInfo.addIntProperty("updated").notNull();

        cityInfo.setHasKeepSections(true);
    }
}
