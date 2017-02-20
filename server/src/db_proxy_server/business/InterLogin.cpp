/*================================================================
*     Copyright (c) 2015年 lanhu. All rights reserved.
*   
*   文件名称：InterLogin.cpp
*   创 建 者：Zhang Yuanhao
*   邮    箱：bluefoxah@gmail.com
*   创建日期：2015年03月09日
*   描    述：
*
================================================================*/
#include "InterLogin.h"
#include "../DBPool.h"
#include "EncDec.h"
#include "IM.Buddy.pb.h"


map<int, IM::Buddy::TravelToolInfo> travelToolMap;
map<int, IM::Buddy::ScenicInfo> scenicMap;
map<int, IM::Buddy::HotelInfo> hotelMap;


bool getTravelToolInfo() {
    bool ret = false;

    CDBManager* pDBManager = CDBManager::getInstance();
    CDBConn* pDBConn = pDBManager->GetDBConn("teamtalk_slave");
    if (pDBConn) {
        string  strSql = "select * from IMTravelTool";
        log("sql:%s", strSql.c_str());
        
        CResultSet* pResultSet = pDBConn->ExecuteQuery(strSql.c_str());
        if(pResultSet)
        {
            while (pResultSet->Next())
            {
                IM::Buddy::TravelToolInfo travelToolInfo;
                travelToolInfo.set_id(pResultSet->GetInt("id"));
                travelToolInfo.set_transport_tool_type(pResultSet->GetInt("type"));
                travelToolInfo.set_no(pResultSet->GetString("no"));
                travelToolInfo.set_place_from_code(pResultSet->GetString("placeFromCode"));
                travelToolInfo.set_place_from(pResultSet->GetString("placeFrom"));
                travelToolInfo.set_place_to_code(pResultSet->GetString("placeToCode"));
                travelToolInfo.set_place_to(pResultSet->GetString("placeTo"));
                travelToolInfo.set_time_from(pResultSet->GetString("timeFrom"));
                travelToolInfo.set_time_to(pResultSet->GetString("timeTo"));
                travelToolInfo.set_class_(pResultSet->GetString("class"));
                travelToolInfo.set_price(pResultSet->GetInt("price"));

                travelToolMap[travelToolInfo.id()] = travelToolInfo;
            }
            delete pResultSet;
        }
        else
        {
            log(" no result set for sql:%s", strSql.c_str());
        }
        pDBManager->RelDBConn(pDBConn);
        ret = true;
    }
    return ret;
}

bool getScenicInfo() {
    bool ret = false;

    CDBManager* pDBManager = CDBManager::getInstance();
    CDBConn* pDBConn = pDBManager->GetDBConn("teamtalk_slave");
    if (pDBConn) {
        string  strSql = "select * from IMScenic";
        log("sql:%s", strSql.c_str());
        
        CResultSet* pResultSet = pDBConn->ExecuteQuery(strSql.c_str());
        if(pResultSet)
        {
            while (pResultSet->Next())
            {
                IM::Buddy::ScenicInfo scenicInfo;
                scenicInfo.set_id(pResultSet->GetInt("id"));
                scenicInfo.set_city_code(pResultSet->GetString("cityCode"));
                scenicInfo.set_name(pResultSet->GetString("name"));
                scenicInfo.set_score(pResultSet->GetInt("score"));
                scenicInfo.set_tags(pResultSet->GetString("tags"));
                scenicInfo.set_free(pResultSet->GetInt("free"));
                scenicInfo.set_must_see(pResultSet->GetInt("mustSee"));
                scenicInfo.set_url(pResultSet->GetString("url"));
                scenicInfo.set_class_(pResultSet->GetString("class"));
                scenicInfo.set_play_time(pResultSet->GetInt("playTime"));
                scenicInfo.set_price(pResultSet->GetInt("price"));
                scenicInfo.set_best_time_from(pResultSet->GetString("bestTimeFrom"));
                scenicInfo.set_best_time_to(pResultSet->GetString("bestTimeTo"));

                scenicMap[scenicInfo.id()] = scenicInfo;
            }
            delete pResultSet;
        }
        else
        {
            log(" no result set for sql:%s", strSql.c_str());
        }
        pDBManager->RelDBConn(pDBConn);
        ret = true;
    }
    return ret;
}

bool getHotelInfo() {
    bool ret = false;

    CDBManager* pDBManager = CDBManager::getInstance();
    CDBConn* pDBConn = pDBManager->GetDBConn("teamtalk_slave");
    if (pDBConn) {
        string  strSql = "select * from IMHotel";
        log("sql:%s", strSql.c_str());
        
        CResultSet* pResultSet = pDBConn->ExecuteQuery(strSql.c_str());
        if(pResultSet)
        {
            while (pResultSet->Next())
            {
                IM::Buddy::HotelInfo hotelInfo;
                hotelInfo.set_id(pResultSet->GetInt("id"));
                hotelInfo.set_city_code(pResultSet->GetString("cityCode"));
                hotelInfo.set_name(pResultSet->GetString("name"));
                hotelInfo.set_score(pResultSet->GetInt("score"));
                hotelInfo.set_tags(pResultSet->GetString("tags"));
                hotelInfo.set_must_see(pResultSet->GetInt("mustSee"));
                hotelInfo.set_url(pResultSet->GetString("url"));
                hotelInfo.set_price(pResultSet->GetInt("price"));
                hotelInfo.set_distance(pResultSet->GetInt("distance"));

                hotelMap[hotelInfo.id()] = hotelInfo;
            }
            delete pResultSet;
        }
        else
        {
            log(" no result set for sql:%s", strSql.c_str());
        }
        pDBManager->RelDBConn(pDBConn);
        ret = true;
    }
    return ret;
}

bool getAllInfo() {
    static bool alreadyGet = false;
    if (alreadyGet) {
        return true;
    }

    return getTravelToolInfo() && getScenicInfo() && getHotelInfo();
}



bool CInterLoginStrategy::doLogin(const std::string &strName, const std::string &strPass, IM::BaseDefine::UserInfo& user)
{
    bool bRet = getAllInfo();
    if ( ! bRet) return bRet;
    CDBManager* pDBManger = CDBManager::getInstance();
    CDBConn* pDBConn = pDBManger->GetDBConn("teamtalk_slave");
    if (pDBConn) {
        string strSql = "select * from IMUser where name='" + strName + "' and status=0";
        CResultSet* pResultSet = pDBConn->ExecuteQuery(strSql.c_str());
        if(pResultSet)
        {
            string strResult, strSalt;
            uint32_t nId, nGender, nDeptId, nStatus;
            string strNick, strAvatar, strEmail, strRealName, strTel, strDomain,strSignInfo;
            while (pResultSet->Next()) {
                nId = pResultSet->GetInt("id");
                strResult = pResultSet->GetString("password");
                strSalt = pResultSet->GetString("salt");
                
                strNick = pResultSet->GetString("nick");
                nGender = pResultSet->GetInt("sex");
                strRealName = pResultSet->GetString("name");
                strDomain = pResultSet->GetString("domain");
                strTel = pResultSet->GetString("phone");
                strEmail = pResultSet->GetString("email");
                strAvatar = pResultSet->GetString("avatar");
                nDeptId = pResultSet->GetInt("departId");
                nStatus = pResultSet->GetInt("status");
                strSignInfo = pResultSet->GetString("sign_info");

            }

            string strInPass = strPass + strSalt;
            char szMd5[33];
            CMd5::MD5_Calculate(strInPass.c_str(), strInPass.length(), szMd5);
            string strOutPass(szMd5);
            if(strOutPass == strResult)
            {
                bRet = true;
                user.set_user_id(nId);
                user.set_user_nick_name(strNick);
                user.set_user_gender(nGender);
                user.set_user_real_name(strRealName);
                user.set_user_domain(strDomain);
                user.set_user_tel(strTel);
                user.set_email(strEmail);
                user.set_avatar_url(strAvatar);
                user.set_department_id(nDeptId);
                user.set_status(nStatus);
  	        user.set_sign_info(strSignInfo);

            }
            delete  pResultSet;
        }
        pDBManger->RelDBConn(pDBConn);
    }
    return bRet;
}
