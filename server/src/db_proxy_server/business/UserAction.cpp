/*================================================================
 *   Copyright (C) 2014 All rights reserved.
 *
 *   文件名称：UserAction.cpp
 *   创 建 者：Zhang Yuanhao
 *   邮    箱：bluefoxah@gmail.com
 *   创建日期：2014年12月15日
 *   描    述：
 *
 ================================================================*/

#include <list>
#include <map>

#include "../ProxyConn.h"
#include "../DBPool.h"
#include "../SyncCenter.h"
#include "public_define.h"
#include "UserModel.h"
#include "IM.Login.pb.h"
#include "IM.Buddy.pb.h"
#include "IM.BaseDefine.pb.h"



namespace DB_PROXY {

    void getUserInfo(CImPdu* pPdu, uint32_t conn_uuid)
    {
        IM::Buddy::IMUsersInfoReq msg;
        IM::Buddy::IMUsersInfoRsp msgResp;
        if(msg.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength()))
        {
            CImPdu* pPduRes = new CImPdu;
            
            uint32_t from_user_id = msg.user_id();
            uint32_t userCount = msg.user_id_list_size();
            std::list<uint32_t> idList;
            for(uint32_t i = 0; i < userCount;++i) {
    		idList.push_back(msg.user_id_list(i));
            }
	std::list<IM::BaseDefine::UserInfo> lsUser;
            CUserModel::getInstance()->getUsers(idList, lsUser);
msgResp.set_user_id(from_user_id);
            for(list<IM::BaseDefine::UserInfo>::iterator it=lsUser.begin();
                it!=lsUser.end(); ++it)
            {
                IM::BaseDefine::UserInfo* pUser = msgResp.add_user_info_list();
    //            *pUser = *it;
             
   pUser->set_user_id(it->user_id());
                pUser->set_user_gender(it->user_gender());
                pUser->set_user_nick_name(it->user_nick_name());
                pUser->set_avatar_url(it->avatar_url());

                pUser->set_sign_info(it->sign_info());
    pUser->set_department_id(it->department_id());
                pUser->set_email(it->email());
                pUser->set_user_real_name(it->user_real_name());
                pUser->set_user_tel(it->user_tel());
                pUser->set_user_domain(it->user_domain());
                pUser->set_status(it->status());
            }
            log("userId=%u, userCnt=%u", from_user_id, userCount);
            msgResp.set_attach_data(msg.attach_data());
            pPduRes->SetPBMsg(&msgResp);
            pPduRes->SetSeqNum(pPdu->GetSeqNum());
            pPduRes->SetServiceId(IM::BaseDefine::SID_BUDDY_LIST);
            pPduRes->SetCommandId(IM::BaseDefine::CID_BUDDY_LIST_USER_INFO_RESPONSE);
            CProxyConn::AddResponsePdu(conn_uuid, pPduRes);
        }
        else
        {
            log("parse pb failed");
        }
    }
    
    void getChangedUser(CImPdu* pPdu, uint32_t conn_uuid)
    {
        IM::Buddy::IMAllUserReq msg;
        IM::Buddy::IMAllUserRsp msgResp;
        if(msg.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength()))
        {
            CImPdu* pPduRes = new CImPdu;
            
            uint32_t nReqId = msg.user_id();
            uint32_t nLastTime = msg.latest_update_time();
            uint32_t nLastUpdate = CSyncCenter::getInstance()->getLastUpdate();
          
            list<IM::BaseDefine::UserInfo> lsUsers;
            if( nLastUpdate > nLastTime)
            {
                list<uint32_t> lsIds;
                CUserModel::getInstance()->getChangedId(nLastTime, lsIds);
                CUserModel::getInstance()->getUsers(lsIds, lsUsers);
            }
            msgResp.set_user_id(nReqId);
            msgResp.set_latest_update_time(nLastTime);
            for (list<IM::BaseDefine::UserInfo>::iterator it=lsUsers.begin();
                 it!=lsUsers.end(); ++it) {
                IM::BaseDefine::UserInfo* pUser = msgResp.add_user_list();
                //            *pUser = *it;
                pUser->set_user_id(it->user_id());
                pUser->set_user_gender(it->user_gender());
                pUser->set_user_nick_name(it->user_nick_name());
                pUser->set_avatar_url(it->avatar_url());
                pUser->set_sign_info(it->sign_info());
                pUser->set_department_id(it->department_id());
                pUser->set_email(it->email());
                pUser->set_user_real_name(it->user_real_name());
                pUser->set_user_tel(it->user_tel());
                pUser->set_user_domain(it->user_domain());
                pUser->set_status(it->status());
            }
            log("userId=%u,nLastUpdate=%u, last_time=%u, userCnt=%u", nReqId,nLastUpdate, nLastTime, msgResp.user_list_size());
            msgResp.set_attach_data(msg.attach_data());
            pPduRes->SetPBMsg(&msgResp);
            pPduRes->SetSeqNum(pPdu->GetSeqNum());
            pPduRes->SetServiceId(IM::BaseDefine::SID_BUDDY_LIST);
            pPduRes->SetCommandId(IM::BaseDefine::CID_BUDDY_LIST_ALL_USER_RESPONSE);
            CProxyConn::AddResponsePdu(conn_uuid, pPduRes);
        }
        else
        {
            log("parse pb failed");
        }
    }
 
    void changeUserSignInfo(CImPdu* pPdu, uint32_t conn_uuid) {
               IM::Buddy::IMChangeSignInfoReq req;
                IM::Buddy::IMChangeSignInfoRsp resp;
                if(req.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength())) {
                        uint32_t user_id = req.user_id();
                       const string& sign_info = req.sign_info();
            
                        bool result = CUserModel::getInstance()->updateUserSignInfo(user_id, sign_info);
                    
                        resp.set_user_id(user_id);
                        resp.set_result_code(result ? 0 : 1);
                        if (result) {
                                resp.set_sign_info(sign_info);
                                log("changeUserSignInfo sucess, user_id=%u, sign_info=%s", user_id, sign_info.c_str());
                            } else {
                                    log("changeUserSignInfo false, user_id=%u, sign_info=%s", user_id, sign_info.c_str());
                                }

            
                        CImPdu* pdu_resp = new CImPdu();
                        resp.set_attach_data(req.attach_data());
                        pdu_resp->SetPBMsg(&resp);
                        pdu_resp->SetSeqNum(pPdu->GetSeqNum());
                        pdu_resp->SetServiceId(IM::BaseDefine::SID_BUDDY_LIST);
                        pdu_resp->SetCommandId(IM::BaseDefine::CID_BUDDY_LIST_CHANGE_SIGN_INFO_RESPONSE);
                        CProxyConn::AddResponsePdu(conn_uuid, pdu_resp);
                    
                    } else {
                            log("changeUserSignInfo: IMChangeSignInfoReq ParseFromArray failed!!!");
                        }
           }
    void doPushShield(CImPdu* pPdu, uint32_t conn_uuid) {
        IM::Login::IMPushShieldReq req;
        IM::Login::IMPushShieldRsp resp;
        if(req.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength())) {
            uint32_t user_id = req.user_id();
            uint32_t shield_status = req.shield_status();
            // const string& sign_info = req.sign_info();
            
            bool result = CUserModel::getInstance()->updatePushShield(user_id, shield_status);
            
            resp.set_user_id(user_id);
            resp.set_result_code(result ? 0 : 1);
            if (result) {
                resp.set_shield_status(shield_status);
                log("doPushShield sucess, user_id=%u, shield_status=%u", user_id, shield_status);
            } else {
                log("doPushShield false, user_id=%u, shield_status=%u", user_id, shield_status);
            }
            
            
            CImPdu* pdu_resp = new CImPdu();
            resp.set_attach_data(req.attach_data());
            pdu_resp->SetPBMsg(&resp);
            pdu_resp->SetSeqNum(pPdu->GetSeqNum());
            pdu_resp->SetServiceId(IM::BaseDefine::SID_LOGIN);
            pdu_resp->SetCommandId(IM::BaseDefine::CID_LOGIN_RES_PUSH_SHIELD);
            CProxyConn::AddResponsePdu(conn_uuid, pdu_resp);
            
        } else {
            log("doPushShield: IMPushShieldReq ParseFromArray failed!!!");
        }
    }
    
    void doQueryPushShield(CImPdu* pPdu, uint32_t conn_uuid) {
        IM::Login::IMQueryPushShieldReq req;
        IM::Login::IMQueryPushShieldRsp resp;
        if(req.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength())) {
            uint32_t user_id = req.user_id();
            uint32_t shield_status = 0;
            
            bool result = CUserModel::getInstance()->getPushShield(user_id, &shield_status);
            
            resp.set_user_id(user_id);
            resp.set_result_code(result ? 0 : 1);
            if (result) {
                resp.set_shield_status(shield_status);
                log("doQueryPushShield sucess, user_id=%u, shield_status=%u", user_id, shield_status);
            } else {
                log("doQueryPushShield false, user_id=%u", user_id);
            }
            
            
            CImPdu* pdu_resp = new CImPdu();
            resp.set_attach_data(req.attach_data());
            pdu_resp->SetPBMsg(&resp);
            pdu_resp->SetSeqNum(pPdu->GetSeqNum());
            pdu_resp->SetServiceId(IM::BaseDefine::SID_LOGIN);
            pdu_resp->SetCommandId(IM::BaseDefine::CID_LOGIN_RES_QUERY_PUSH_SHIELD);
            CProxyConn::AddResponsePdu(conn_uuid, pdu_resp);
        } else {
            log("doQueryPushShield: IMQueryPushShieldReq ParseFromArray failed!!!");
        }
    }

    void getTravelRoute(CImPdu* pPdu, uint32_t conn_uuid) {
        log("getTravelRoute enter.");
        IM::Buddy::TravelRouteReq req;
        IM::Buddy::TravelRouteRsp resp;
        if(req.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength())) {
            uint32_t user_id = req.user_id();

            bool result = CUserModel::getInstance()->getTravelRoute(user_id, req, resp);
            resp.set_user_id(user_id);
            resp.set_result_code(result ? 0 : 1);
            if ( ! result) {
                log("getTravelDetail false, user_id=%u", user_id);
            }
            
            CImPdu* pdu_resp = new CImPdu();
            resp.set_attach_data(req.attach_data());
            pdu_resp->SetPBMsg(&resp);
            pdu_resp->SetSeqNum(pPdu->GetSeqNum());
            pdu_resp->SetServiceId(IM::BaseDefine::SID_BUDDY_LIST);
            pdu_resp->SetCommandId(IM::BaseDefine::CID_BUDDY_LIST_TRAVEL_ROUTE_RESPONSE);
            CProxyConn::AddResponsePdu(conn_uuid, pdu_resp);
            
        } else {
            log("getTravelRoute: TravelRouteReq ParseFromArray failed!!!");
        }
    }

    void createTravelDetail(CImPdu* pPdu, uint32_t conn_uuid) {
        log("createTravelDetail enter.");
        IM::Buddy::CreateTravelReq req;
        IM::Buddy::CreateTravelRsp resp;
        if(req.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength())) {
            uint32_t user_id = req.user_id();
            
            uint32_t idx = CUserModel::getInstance()->createTravelDetail(user_id, &req);
            resp.set_user_id(user_id);
            resp.set_db_idx(idx);
            resp.set_result_code(0 != idx ? 0 : 1);
            if (0 == idx) {
                log("createTravelDetail result user_id=%u, idx=%u", user_id, idx);
            }
            
            CImPdu* pdu_resp = new CImPdu();
            resp.set_attach_data(req.attach_data());
            pdu_resp->SetPBMsg(&resp);
            pdu_resp->SetSeqNum(pPdu->GetSeqNum());
            pdu_resp->SetServiceId(IM::BaseDefine::SID_BUDDY_LIST);
            pdu_resp->SetCommandId(IM::BaseDefine::CID_BUDDY_LIST_TRAVEL_CREATE_RESPONSE);
            CProxyConn::AddResponsePdu(conn_uuid, pdu_resp);
            
        } else {
            log("createTravelDetail: CreateTravelReq ParseFromArray failed!!!");
        }
    }

    void getTravelDetail(CImPdu* pPdu, uint32_t conn_uuid) {
        IM::Buddy::GetTravelListReq req;
        IM::Buddy::GetTravelTripListRsp resp;
        if(req.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength())) {
            uint32_t user_id = req.user_id();
            
            bool result = CUserModel::getInstance()->getTravelDetail(user_id, resp);
            resp.set_user_id(user_id);
            resp.set_result_code(result ? 0 : 1);
            if ( ! result) {
                log("getTravelDetail false, user_id=%u", user_id);
            }
            
            CImPdu* pdu_resp = new CImPdu();
            resp.set_attach_data(req.attach_data());
            pdu_resp->SetPBMsg(&resp);
            pdu_resp->SetSeqNum(pPdu->GetSeqNum());
            pdu_resp->SetServiceId(IM::BaseDefine::SID_BUDDY_LIST);
            pdu_resp->SetCommandId(IM::BaseDefine::CID_BUDDY_LIST_TRAVEL_LIST_RESPONSE);
            CProxyConn::AddResponsePdu(conn_uuid, pdu_resp);
            
        } else {
            log("getTravelDetail: GetTravelListReq ParseFromArray failed!!!");
        }
    }

    void deleteTravelDetail(CImPdu* pPdu, uint32_t conn_uuid) {
        log("delTravelDetail enter.");
        IM::Buddy::DeleteTravelReq req;
        IM::Buddy::DeleteTravelRsp resp;
        if(req.ParseFromArray(pPdu->GetBodyData(), pPdu->GetBodyLength())) {
            uint32_t user_id = req.user_id();
            set<uint32_t> db_idx_list;
            uint32_t nCnt = req.db_idx_list_size();
            for(uint32_t i = 0; i < nCnt; ++i)
            {
                db_idx_list.insert(req.db_idx_list(i));
            }
            
            bool ret = CUserModel::getInstance()->deleteTravelDetail(user_id, db_idx_list);
            resp.set_user_id(user_id);
            resp.set_result_code(ret ? 0 : 1);
            if ( ! ret) {
                log("failed to deleteTravelDetail user_id=%u", user_id);
            }
            
            CImPdu* pdu_resp = new CImPdu();
            resp.set_attach_data(req.attach_data());
            pdu_resp->SetPBMsg(&resp);
            pdu_resp->SetSeqNum(pPdu->GetSeqNum());
            pdu_resp->SetServiceId(IM::BaseDefine::SID_BUDDY_LIST);
            pdu_resp->SetCommandId(IM::BaseDefine::CID_BUDDY_LIST_TRAVEL_DELETE_RESPONSE);
            CProxyConn::AddResponsePdu(conn_uuid, pdu_resp);
            
        } else {
            log("deleteTravelDetail: CreateTravelReq ParseFromArray failed!!!");
        }
    }
};

