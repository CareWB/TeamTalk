# -*- coding: utf-8 -*-

import os, sys, time, pickle, tempfile
import math, random, itertools
import pandas as pd
import numpy as np
import time
from scipy.linalg import kron

import seaborn as sns
from joblib import Parallel, delayed
import pulp

import json
import emo2tag

import MySQLdb
import redis

local_ip = '127.0.0.1'
db_user = 'root'
db_pw = '12345'
db_name = 'teamtalk'

db_conn = None

N_JOBS = 4         # number of parallel jobs
USE_GUROBI = False # whether to use GUROBI as ILP solver

maxTravelDay = 7   # maximum travel days

## load POI dataset
fpoi = os.path.join('ScenicWithTag_New.csv')
poi_All = pd.read_csv(fpoi, error_bad_lines=False)
poi_All.set_index('id', inplace=True)
#print(poi_All.head(5))
#print(poi_All.index)

## load Hotel dataset 
fhotel = os.path.join('Hotel.csv')
hotel_All = pd.read_csv(fhotel, error_bad_lines=False)
hotel_All.set_index('hotelID', inplace=True)
#print hotel_city.head()
#print hotel_city.index

## load Transportation coordinate
ftrans = os.path.join('stations.csv')
trans_All = pd.read_csv(ftrans, error_bad_lines=False)
trans_All.set_index('id', inplace=True)

#### Compute distance between two POIs or POI-hotel using [Haversine formula](http://en.wikipedia.org/wiki/Great-circle_distance).
def calc_dist_vec(longitudes1, latitudes1, longitudes2, latitudes2):

    """Calculate the distance (unit: km) between two places on earth, vectorised"""
    # convert degrees to radians
    lng1 = np.radians(longitudes1)
    lat1 = np.radians(latitudes1)
    lng2 = np.radians(longitudes2)
    lat2 = np.radians(latitudes2)
    radius = 6371.0088 # mean earth radius, en.wikipedia.org/wiki/Earth_radius#Mean_radius

    # The haversine formula, en.wikipedia.org/wiki/Great-circle_distance
    dlng = np.fabs(lng1 - lng2)
    dlat = np.fabs(lat1 - lat2)
    dist =  2 * radius * np.arcsin( np.sqrt( 
                (np.sin(0.5*dlat))**2 + np.cos(lat1) * np.cos(lat2) * (np.sin(0.5*dlng))**2 ))
    return dist

### Distance matrix between POIs.
def calc_poi_poi_dist_mat(poi_city):

    POI_POI_DISTMAT = pd.DataFrame(data=np.zeros((poi_city.shape[0], poi_city.shape[0]), dtype=np.float),                            
    index=poi_city.index, columns=poi_city.index)

    for ix in poi_city.index:
        POI_POI_DISTMAT.loc[ix] = calc_dist_vec((poi_city.loc[ix, 'lng']),                                         
            (poi_city.loc[ix, 'lat']),                                         
            (poi_city['lng']),                                         
            (poi_city['lat']))

    #POI_POI_DISTMAT = POI_POI_DISTMAT / 20  # Assume a fixed speed of all vehicles
    return POI_POI_DISTMAT
#print calc_poi_poi_dist_mat(poi_city)

### Distance matrix between POIs and Hotels.
def calc_poi_hotel_dist_mat(poi_city, hotel_city):

    POI_HOTEL_DISTMAT = pd.DataFrame(data=np.zeros((poi_city.shape[0], hotel_city.shape[0]), dtype=np.float),                            
    index=poi_city.index, columns=hotel_city.index)

    for ix in poi_city.index:
        POI_HOTEL_DISTMAT.loc[ix] = calc_dist_vec((poi_city.loc[ix, 'lng']),                                         
            (poi_city.loc[ix, 'lat']),                                         
            (hotel_city['hotelLon']),                                         
            (hotel_city['hotelLat']))

    return POI_HOTEL_DISTMAT
# print calc_poi_hotel_dist_mat(poi_city, hotel_city)

### Distance matrix between POIs and Hotels.
def calc_trans_poi_dist_mat(trans_city, poi_city):

    TRANS_POI_DISTMAT = pd.DataFrame(data=np.zeros((trans_city.shape[0], poi_city.shape[0]), dtype=np.float),                            
    index=trans_city.index, columns=poi_city.index)

    for ix in trans_city.index:
        TRANS_POI_DISTMAT.loc[ix] = calc_dist_vec((trans_city.loc[ix, 'lng']),                                         
            (trans_city.loc[ix, 'lat']),                                         
            (poi_city['lng']),                                         
            (poi_city['lat']))

    return TRANS_POI_DISTMAT

#### Use integer linear programming (ILP) to find a simple path without repeatedly visiting POIs.

###### Way 1: Recommendation via selecting first POI, end POI, and L POIs
def path_ILP_L_POIs(V, poi_poi_distmat, ps, pe, L, withNodeWeight=False, alpha=0.5):
    
    assert(isinstance(V, pd.DataFrame))
    assert(ps in V.index)
    assert(pe in V.index)
    assert(2 < L <= V.index.shape[0])
    if withNodeWeight == True:
        assert(0 < alpha < 1)
    beta = 1 - alpha

    p0 = str(ps); pN = str(pe); N = V.index.shape[0]
    
    # REF: pythonhosted.org/PuLP/index.html
    pois = [str(p) for p in V.index] # create a string list for each POI
    pb = pulp.LpProblem('MostLikelyTraj', pulp.LpMinimize) # create problem

    # visit_i_j = 1 means POI i and j are visited in sequence
    visit_vars = pulp.LpVariable.dicts('visit', (pois, pois), 0, 1, pulp.LpInteger) 
    # a dictionary contains all dummy variables
    dummy_vars = pulp.LpVariable.dicts('u', [x for x in pois if x != p0], 2, N, pulp.LpInteger)
    
    # add objective
    objlist = []
    if withNodeWeight == True:
        objlist.append(alpha * V.loc[int(p0), 'weight'])
    for pi in [x for x in pois if x != pN]:     # from
        for pj in [y for y in pois if y != p0]: # to
            if withNodeWeight == True:
                objlist.append(visit_vars[pi][pj] * (alpha * V.loc[int(pj), 'weight'] + \
                    beta * (poi_poi_distmat.loc[int(pi),int(pj)] + V.loc[int(pj), 'playTime'])))
            else:
                # Consider the time b/t two POIs and the duration on the end POI
                #objlist.append(visit_vars[pi][pj] * (poi_poi_distmat.loc[int(pi),int(pj)] + V.loc[int(pj), 'playTime']))
                objlist.append(visit_vars[pi][pj] * (poi_poi_distmat.loc[int(pi),int(pj)])) 
    pb += pulp.lpSum(objlist), 'Objective'
    
    # add constraints, each constraint should be in ONE line
    pb += pulp.lpSum([visit_vars[p0][pj] for pj in pois if pj != p0]) == 1, 'StartAt_p0'
    pb += pulp.lpSum([visit_vars[pi][pN] for pi in pois if pi != pN]) == 1, 'EndAt_pN'
    if p0 != pN:
        pb += pulp.lpSum([visit_vars[pi][p0] for pi in pois]) == 0, 'NoIncoming_p0'
        pb += pulp.lpSum([visit_vars[pN][pj] for pj in pois]) == 0, 'NoOutgoing_pN'
    pb += pulp.lpSum([visit_vars[pi][pj] for pi in pois if pi != pN for pj in pois if pj != p0]) == L-1, 'Length'

    for pk in [x for x in pois if x not in {p0, pN}]:
        pb += pulp.lpSum([visit_vars[pi][pk] for pi in pois if pi != pN]) == \
            pulp.lpSum([visit_vars[pk][pj] for pj in pois if pj != p0]), 'ConnectedAt_' + pk
        pb += pulp.lpSum([visit_vars[pi][pk] for pi in pois if pi != pN]) <= 1, 'Enter_' + pk + '_AtMostOnce'
        pb += pulp.lpSum([visit_vars[pk][pj] for pj in pois if pj != p0]) <= 1, 'Leave_' + pk + '_AtMostOnce'
    
    for pi in [x for x in pois if x != p0]:
        for pj in [y for y in pois if y != p0]:
            pb += dummy_vars[pi] - dummy_vars[pj] + 1 <= (N - 1) * (1 - visit_vars[pi][pj]), 'SubTourElimination_' + pi + '_' + pj
    #pb.writeLP("traj_tmp.lp")
    
    # solve problem: solver should be available in PATH
    if USE_GUROBI == True:
        gurobi_options = [('TimeLimit', '7200'), ('Threads', str(N_JOBS)), ('NodefileStart', '0.2'), ('Cuts', '2')]
        pb.solve(pulp.GUROBI_CMD(path='gurobi_cl', options=gurobi_options)) # GUROBI
    else:
        pb.solve(pulp.COIN_CMD(path='/root/anaconda2/lib/python2.7/site-packages/pulp/solverdir/cbc/linux/64/cbc', 
            options=['-threads', str(N_JOBS), '-strategy', '1', '-maxIt', '10000']))#CBC
        #pb.solve(pulp.COIN_CMD(path='/root/anaconda2/lib/python2.7/site-packages/pulp/solverdir/cbc/osx/64/cbc', 
        #    options=['-threads', str(N_JOBS), '-strategy', '1', '-maxIt', '10000']))#CBC
        
    visit_mat = pd.DataFrame(data=np.zeros((len(pois), len(pois)), dtype=np.float), index=pois, columns=pois)
    for pi in pois:
        for pj in pois: 
            visit_mat.loc[pi, pj] = visit_vars[pi][pj].varValue

    # build the recommended trajectory
    poiSeq = [p0]
    timeSeq = [V.loc[int(p0), 'playTime']]

    while True:
        pi = poiSeq[-1]
        pj = visit_mat.loc[pi].idxmax()
        assert(round(visit_mat.loc[pi, pj]) == 1)
        poiSeq.append(pj)

        ## Consider both the time b/t two POIs and the end POI
        #timeSeq.append(poi_poi_distmat[pi,pj] + V.loc[int(pj), 'playTime'])
        
        ## Only consider the time of visiting the end POI
        timeSeq.append(V.loc[int(pj), 'playTime'])

        if pj == pN: 
            return [int(x) for x in poiSeq], [time for time in timeSeq]



###### Way 2: Recommendation via selecting first POI, end POI, and dayCount Days
def path_ILP_T_Days(V, poi_poi_distmat, ps, pe, dayCount, startTimeIn, endTimeOut, startDayTime=9, endDayTime=18, withNodeWeight=False, alpha=0.5, ):
    
    assert(isinstance(V, pd.DataFrame))
    assert(ps in V.index)
    assert(pe in V.index)
    assert(1 <= dayCount <= maxTravelDay)
    if withNodeWeight == True:
        assert(0 < alpha < 1)
    beta = 1 - alpha

    p0 = str(ps); pN = str(pe); N = V.index.shape[0]
    
    # REF: pythonhosted.org/PuLP/index.html
    pois = [str(p) for p in V.index] # create a string list for each POI
    pb = pulp.LpProblem('MostLikelyTraj', pulp.LpMinimize) # create problem

    # visit_i_j = 1 means POI i and j are visited in sequence
    visit_vars = pulp.LpVariable.dicts('visit', (pois, pois), 0, 1, pulp.LpInteger) 
    # a dictionary contains all dummy variables
    dummy_vars = pulp.LpVariable.dicts('u', [x for x in pois if x != p0], 2, N, pulp.LpInteger)
    
    # add objective
    objlist = []
    if withNodeWeight == True:
        objlist.append(alpha * V.loc[int(p0), 'weight'])
    for pi in [x for x in pois if x != pN]:     # from
        for pj in [y for y in pois if y != p0]: # to
            if withNodeWeight == True:
                objlist.append(visit_vars[pi][pj] * (alpha * V.loc[int(pj), 'weight'] + \
                    beta * (poi_poi_distmat.loc[int(pi),int(pj)]) ) )
            else: 
                objlist.append(visit_vars[pi][pj] * (poi_poi_distmat.loc[int(pi),int(pj)])) 
    pb += pulp.lpSum(objlist), 'Objective'
    
    # add constraints, each constraint should be in ONE line
    pb += pulp.lpSum([visit_vars[p0][pj] for pj in pois if pj != p0]) == 1, 'StartAt_p0'
    pb += pulp.lpSum([visit_vars[pi][pN] for pi in pois if pi != pN]) == 1, 'EndAt_pN'

    if p0 != pN:
        pb += pulp.lpSum([visit_vars[pi][p0] for pi in pois]) == 0, 'NoIncoming_p0'
        pb += pulp.lpSum([visit_vars[pN][pj] for pj in pois]) == 0, 'NoOutgoing_pN'
    
    # sum_endTimeOut = sum(endTimeOut)
    # sum_startTimeIn = sum(startTimeIn)

    time_firstday = endDayTime - startTimeIn
    time_lastday = endTimeOut - startDayTime
    time_between = (endDayTime - startDayTime) * (dayCount - 2)
    time_sum = time_firstday + time_lastday + time_between
    #print "time_sum", time_sum

    ## Assume that travellers spend [Time_Min, Time_Min+1] hrs each day to travel
    # pb += pulp.lpSum([visit_vars[pi][pj] * V.loc[int(pj), 'playTime'] for pi in pois if pi != pN for pj in pois if pj != p0]) \
    #     <= (sum_endTimeOut - sum_startTimeIn) - (V.loc[int(p0), 'playTime'] + V.loc[int(pN), 'playTime']), 'Time_Max' 
    # pb += pulp.lpSum([visit_vars[pi][pj] * V.loc[int(pj), 'playTime'] for pi in pois if pi != pN for pj in pois if pj != p0]) \
    #     >= (sum_endTimeOut - sum_startTimeIn - 1) - (V.loc[int(p0), 'playTime'] + V.loc[int(pN), 'playTime']), 'Time_Min' 
    
    pb += pulp.lpSum([visit_vars[pi][pj] * V.loc[int(pj), 'playTime'] for pi in pois if pi != pN for pj in pois if pj != p0]) \
        <= time_sum - (V.loc[int(p0), 'playTime'] + V.loc[int(pN), 'playTime']), 'Time_Max' 
    pb += pulp.lpSum([visit_vars[pi][pj] * V.loc[int(pj), 'playTime'] for pi in pois if pi != pN for pj in pois if pj != p0]) \
        >= (time_sum - 1) - (V.loc[int(p0), 'playTime'] + V.loc[int(pN), 'playTime']), 'Time_Min' 
    

    for pk in [x for x in pois if x not in {p0, pN}]:
        pb += pulp.lpSum([visit_vars[pi][pk] for pi in pois if pi != pN]) == \
            pulp.lpSum([visit_vars[pk][pj] for pj in pois if pj != p0]), 'ConnectedAt_' + pk
        pb += pulp.lpSum([visit_vars[pi][pk] for pi in pois if pi != pN]) <= 1, 'Enter_' + pk + '_AtMostOnce'
        pb += pulp.lpSum([visit_vars[pk][pj] for pj in pois if pj != p0]) <= 1, 'Leave_' + pk + '_AtMostOnce'
    
    for pi in [x for x in pois if x != p0]:
        for pj in [y for y in pois if y != p0]:
            pb += dummy_vars[pi] - dummy_vars[pj] + 1 <= (N - 1) * (1 - visit_vars[pi][pj]), 'SubTourElimination_' + pi + '_' + pj
    #pb.writeLP("traj_tmp.lp")
    
    # solve problem: solver should be available in PATH
    if USE_GUROBI == True:
        gurobi_options = [('TimeLimit', '7200'), ('Threads', str(N_JOBS)), ('NodefileStart', '0.2'), ('Cuts', '2')]
        pb.solve(pulp.GUROBI_CMD(path='gurobi_cl', options=gurobi_options)) # GUROBI
    else:
        pb.solve(pulp.COIN_CMD(path='/root/anaconda2/lib/python2.7/site-packages/pulp/solverdir/cbc/linux/64/cbc', 
            options=['-threads', str(N_JOBS), '-strategy', '1', '-maxIt', '10000']))#CBC
        #pb.solve(pulp.COIN_CMD(path='/root/anaconda2/lib/python2.7/site-packages/pulp/solverdir/cbc/osx/64/cbc', 
        #    options=['-threads', str(N_JOBS), '-strategy', '1', '-maxIt', '10000']))#CBC
        
    visit_mat = pd.DataFrame(data=np.zeros((len(pois), len(pois)), dtype=np.float), index=pois, columns=pois)
    for pi in pois:
        for pj in pois: 
            visit_mat.loc[pi, pj] = visit_vars[pi][pj].varValue

    # build the recommended trajectory
    poiSeq = [p0]
    timeSeq = [V.loc[int(p0), 'playTime']]

    while True:
        pi = poiSeq[-1]
        pj = visit_mat.loc[pi].idxmax()
        assert(round(visit_mat.loc[pi, pj]) == 1)
        poiSeq.append(pj)

        ## Consider both the time b/t two POIs and the end POI
        #timeSeq.append(poi_poi_distmat[pi,pj] + V.loc[int(pj), 'playTime'])
        
        ## Only consider the time of visiting the end POI
        timeSeq.append(V.loc[int(pj), 'playTime'])

        if pj == pN: 
            return [int(x) for x in poiSeq], [time for time in timeSeq]


# ## Way 3: Default 1: recommendation L POIs in a descreasing order via playtime of each POI.
def traj_default_L_POIs(pi, pj, L, poi_city):

    poi_city.sort_values(by='playTime', ascending=False, inplace=True)
    POI_pop = poi_city.index.tolist()
    poiSeq = [pi] + [x for x in POI_pop if x not in {pi, pj}][:L-2] + [pj]
    timeSeq = [poi_city.loc[int(pi), 'playTime']] + [poi_city.loc[int(x), 'playTime'] for x in POI_pop if x not in {pi, pj}][:L-2] \
        + [poi_city.loc[int(pj), 'playTime']]
    
    #print('Rank POI thru duration:', poiSeq)
    return poiSeq, timeSeq


# ## Way 4: Default 2: recommendation POIs of dayCount Days in a descreasing order via playtime of each POI.
def traj_default_T_Days(pi, pj, dayCount, poi_city, startDayTime=9, endDayTime=18):

    poi_city.sort_values(by='playTime', ascending=False, inplace=True)
    POI_pop = poi_city.index.tolist()

    poiSeq = [p0]
    timeSeq = [V.loc[int(p0), 'playTime']]
    i = 1
    while sum(timeSeq) <= dayCount * (endDayTime - startDayTime):
        poiSeq.append(POI_pop[i])
        timeSeq.append(poi_city.loc[int(POI_pop[i]), 'playTime'])
        i += 1

    return poiSeq, timeSeq


#### Split time sequenece into a journey
def timeSeq2Traj(poiSeq, timeSeq, dayCount, startTimeIn, endTimeOut, startDayTime=9, endDayTime=18, maxTravelDay=maxTravelDay):

    POIs_Eachday = [[] for _ in range(maxTravelDay)]
    endPOI_Eachday = []

    idx_day = 0
    idx_time = 0

    startTimeList = []
    startTimeList.append(float(startTimeIn))
    for _ in range(int(dayCount)-1):
        startTimeList.append(float(startDayTime))


    endTimeList = []
    for _ in range(int(dayCount)-1):
        endTimeList.append(endDayTime)
    endTimeList.append(endTimeOut)

    time_cum = startTimeList[idx_day]

    while idx_time < len(timeSeq):

        time_cum += timeSeq[idx_time]

        if idx_day <= dayCount - 1:
            if time_cum <= endTimeList[idx_day]:
                POIs_Eachday[idx_day].append(poiSeq[idx_time]) 
            else:
                idx_time -= 1
                endPOI_Eachday.append(poiSeq[idx_time])
                idx_day += 1
                if idx_day <= dayCount - 1:
                    time_cum = 0
                    time_cum += startTimeList[idx_day]
        else:
            POIs_Eachday[idx_day-1].append(poiSeq[idx_time]) #多余景点全部放在最后一天

        idx_time += 1
    
    endPOI_Eachday.append(poiSeq[len(timeSeq)-1]) # if needed, we can recommend hotels on the final day even time less than 18:00PM
 
    return POIs_Eachday, endPOI_Eachday

#### Split modified time sequenece into a traj. 
def timeSeq2Traj_with_modification(poiSeq, timeSeq, dayCount):

    POIs_Eachday = [[] for _ in range(dayCount)]
    endPOI_Eachday = []

    idx_day = 0
    idx_time = 0

    time_sum = sum(timeSeq)
    time_ave = np.ceil(time_sum / dayCount)
    time_cum = 0

    while idx_day < dayCount-1:

        time_cum += timeSeq[idx_time]
        if time_cum <= time_ave:  
            POIs_Eachday[idx_day].append(poiSeq[idx_time]) 
            idx_time += 1 
        else:
            idx_time -= 1
            endPOI_Eachday.append(poiSeq[idx_time])
            idx_day += 1
            time_cum = 0
            idx_time += 1

    for idx_time_end in range(idx_time+1, len(timeSeq)):
        POIs_Eachday[dayCount-1].append(poiSeq[idx_time_end])
    endPOI_Eachday.append(poiSeq[-1])


    return POIs_Eachday, endPOI_Eachday

################Test Each Case ############################

 
# #### Recommend trajectories using ILP by leveraging POI-POI distance.
def traj_T_Days_ILP_POI(pi, pj, dayCount, poi_city, startTimeIn, endTimeOut, startDayTime=9, endDayTime=18):
    
    #print('Trajectory via POI-POI distance: ', pi, '->', pj, 'length:', L)
    poi_poi_distmat = calc_poi_poi_dist_mat(poi_city)
    rankPOIs_ILP, seqTime_ILP = path_ILP_T_Days(poi_city, poi_poi_distmat, pi, pj, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime) 
    #print rankPOIs_ILP, seqTime_ILP
    POIs_Eachday, endPOI_Eachday =  timeSeq2Traj(rankPOIs_ILP, seqTime_ILP, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)
    #print POIs_Eachday, endPOI_Eachday
    return rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday

# Recommend trajectories of specific label using ILP by leveraging POI-POI distance.
def traj_label_T_Days_ILP_POI(pi, pj, dayCount, cityCode, label, startTimeIn, endTimeOut, startDayTime=9, endDayTime=18):
    
    poi_city = poi_All[poi_All['cityCode'] == cityCode]

    poi_label_1st = poi_city[poi_city[label[0]] == 5]
    poi_label_2nd = poi_city[poi_city[label[1]] == 5]
    poi_label_3rd = poi_city[poi_city[label[2]] == 5]

    poi_label_1st_2nd = pd.concat([poi_label_1st,poi_label_2nd]).drop_duplicates()
    poi_label_1st_2nd_3rd = pd.concat([poi_label_1st_2nd,poi_label_3rd]).drop_duplicates()
    

    time_firstday = endDayTime - startTimeIn
    time_lastday = endTimeOut - startDayTime
    time_between = (endDayTime - startDayTime) * (dayCount - 2)
    time_sum = time_firstday + time_lastday + time_between
    
    #print poi_label_1st_2nd
    print sum(poi_label_1st['playTime']), sum(poi_label_2nd['playTime']), sum(poi_label_3rd['playTime'])

    if sum(poi_label_1st['playTime']) >= time_sum:
        poi_poi_distmat_label_1st = calc_poi_poi_dist_mat(poi_label_1st)
        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st, poi_poi_distmat_label_1st, pi, pj, dayCount, startTimeIn, endTimeOut)
    elif sum(poi_label_1st_2nd['playTime']) >= time_sum:
        poi_poi_distmat_label_1st_2nd = calc_poi_poi_dist_mat(poi_label_1st_2nd)
        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st_2nd, poi_poi_distmat_label_1st_2nd, pi, pj, dayCount, startTimeIn, endTimeOut)
    elif sum(poi_label_1st_2nd_3rd['playTime']) >= time_sum:    
        poi_poi_distmat_label_1st_2nd_3rd = calc_poi_poi_dist_mat(poi_label_1st_2nd_3rd)
        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st_2nd_3rd, poi_poi_distmat_label_1st_2nd_3rd, pi, pj, dayCount, startTimeIn, endTimeOut)
    else:
        raise Exception("Too long playtime")
        
    POIs_Eachday, endPOI_Eachday =  timeSeq2Traj(rankPOIs_label_ILP, seqTime_label_ILP, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)
    
    return rankPOIs_label_ILP, seqTime_label_ILP, POIs_Eachday, endPOI_Eachday

    
# Recommend trajectories of specific label using ILP by leveraging POI-POI distance.
def traj_label_T_Days_ILP_Trans(startTrans, backtool, dayCount, cityCode, label, startTimeIn, endTimeOut, startDayTime=9, endDayTime=18):
    
    # First filter pois in terms of citycode
    poi_city = poi_All[poi_All['cityCode'] == cityCode]
    poi_label_1st = poi_city[poi_city[label[0]] == 5]
    #print(poi_label_1st.index)
    poi_label_2nd = poi_city[poi_city[label[1]] == 5]
    #print(poi_label_2nd.index)
    poi_label_3rd = poi_city[poi_city[label[2]] == 5]

    poi_label_1st_2nd = pd.concat([poi_label_1st,poi_label_2nd]).drop_duplicates()
    #print(type(poi_label_1st_2nd))
    #print(poi_label_1st_2nd.index)

    poi_label_1st_2nd_3rd = pd.concat([poi_label_1st_2nd,poi_label_3rd]).drop_duplicates()
    #print(poi_label_1st_2nd_3rd.index)
    print sum(poi_label_1st['playTime']), sum(poi_label_2nd['playTime']), sum(poi_label_3rd['playTime'])

    # From intype and outtype transportations to find their corresponding nearest pois
    trans_city = trans_All[trans_All['cityCode'] == cityCode]
    trans_city_startTrans = trans_city[trans_city['type'] == startTrans]
    startTrans_idx = trans_city_startTrans.index[0]
    #print(startTrans_idx)

    trans_city_backtool = trans_city[trans_city['type'] == backtool]    
    backtool_idx = trans_city_backtool.index[0]
    #print(backtool_idx)
    
    time_firstday = endDayTime - startTimeIn
    time_lastday = endTimeOut - startDayTime
    time_between = (endDayTime - startDayTime) * (dayCount - 2)
    time_sum = time_firstday + time_lastday + time_between
    print("Sum of PlayTime:", time_sum)

    if sum(poi_label_1st['playTime']) >= time_sum: 

        poi_poi_distmat_label_1st = calc_poi_poi_dist_mat(poi_label_1st)
        print(poi_poi_distmat_label_1st.shape)

        trans_startTrans_poi_label_1st_distmat = calc_trans_poi_dist_mat(trans_city_startTrans, poi_label_1st)
        poi_label_1st_start_dist = trans_startTrans_poi_label_1st_distmat.loc[startTrans_idx,:]
        poi_label_1st_start_dist_sort = poi_label_1st_start_dist.sort_values()
        pi_list = poi_label_1st_start_dist_sort.index[:5] # choose 5 nearest POIs for each startTrans
        piIdx = np.random.randint(len(pi_list))
        pi = pi_list[piIdx]
        print('start poi:', pi)

        trans_backtool_poi_label_1st_distmat = calc_trans_poi_dist_mat(trans_city_backtool, poi_label_1st)
        poi_label_1st_end_dist = trans_backtool_poi_label_1st_distmat.loc[backtool_idx,:]
        poi_label_1st_end_dist_sort = poi_label_1st_end_dist.sort_values()
        pj_list = poi_label_1st_end_dist_sort.index[:5] # choose 5 nearest POIs for each backtool
        pjIdx = np.random.randint(len(pj_list))
        pj = pj_list[pjIdx]
        print('end poi:', pj)

        ## If end POI is the same with start POI, which forms a loop, then we need to change end POI to the 2nd nearst poi
        if pi == pj:
            if pjIdx + 1 < len(pi_list):
                pj = pj_list[pjIdx + 1]
            elif pjIdx - 1 >= 0:
                pj = pj_list[pjIdx - 1]
            print('end poi:', pj)

        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st, poi_poi_distmat_label_1st, pi, pj, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)
    
    elif sum(poi_label_1st_2nd['playTime']) >= time_sum:

        poi_poi_distmat_label_1st_2nd = calc_poi_poi_dist_mat(poi_label_1st_2nd)
        print(poi_poi_distmat_label_1st_2nd.shape)

        trans_startTrans_poi_label_1st_2nd_distmat = calc_trans_poi_dist_mat(trans_city_startTrans, poi_label_1st_2nd)
        poi_label_1st_2nd_start_dist = trans_startTrans_poi_label_1st_2nd_distmat.loc[startTrans_idx,:]
        poi_label_1st_2nd_start_dist_sort = poi_label_1st_2nd_start_dist.sort_values()
        pi_list = poi_label_1st_2nd_start_dist_sort.index[:5] # choose 5 nearest POIs for each startTrans
        print("source list:", pi_list)
        piIdx = np.random.randint(len(pi_list))
        pi = pi_list[piIdx]
        print('start poi:', pi)


        trans_backtool_poi_label_1st_2nd_distmat = calc_trans_poi_dist_mat(trans_city_backtool, poi_label_1st_2nd)
        print(trans_backtool_poi_label_1st_2nd_distmat.shape)

        poi_label_1st_2nd_end_dist = trans_backtool_poi_label_1st_2nd_distmat.loc[backtool_idx,:]
        poi_label_1st_2nd_end_dist_sort = poi_label_1st_2nd_end_dist.sort_values()
        pj_list = poi_label_1st_2nd_end_dist_sort.index[:5] # choose 5 nearest POIs for each backtool
        print("end list:", pj_list)
        pjIdx = np.random.randint(len(pj_list))
        pj = pj_list[pjIdx]
        print('end poi:', pj)

        ## If end POI is the same with start POI, which forms a loop, then we need to change end POI to the 2nd nearst poi
        if pi == pj:
            if pjIdx + 1 < len(pi_list):
                pj = pj_list[pjIdx + 1]
            elif pjIdx - 1 >= 0:
                pj = pj_list[pjIdx - 1]
            print('end poi:', pj)

        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st_2nd, poi_poi_distmat_label_1st_2nd, pi, pj, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)

    elif sum(poi_label_1st_2nd_3rd['playTime']) >= time_sum:    
        
        poi_poi_distmat_label_1st_2nd_3rd = calc_poi_poi_dist_mat(poi_label_1st_2nd_3rd)
        
        trans_startTrans_poi_label_1st_2nd_3rd_distmat = calc_trans_poi_dist_mat(trans_city_startTrans, poi_label_1st_2nd_3rd)  
        poi_label_1st_2nd_3rd_start_dist = trans_startTrans_poi_label_1st_2nd_3rd_distmat.loc[startTrans_idx,:]
        poi_label_1st_2nd_3rd_start_dist_sort = poi_label_1st_2nd_3rd_start_dist.sort_values()
        pi_list = poi_label_1st_2nd_3rd_start_dist_sort.index[:5] # choose 5 nearest POIs for each startTrans
        piIdx = np.random.randint(len(pi_list))
        pi = pi_list[piIdx]
        print('start poi:', pi)

        trans_backtool_poi_label_1st_2nd_3rd_distmat = calc_trans_poi_dist_mat(trans_city_backtool, poi_label_1st_2nd_3rd)
        poi_label_1st_2nd_3rd_end_dist = trans_backtool_poi_label_1st_2nd_3rd_distmat.loc[backtool_idx,:]
        poi_label_1st_2nd_3rd_end_dist_sort = poi_label_1st_2nd_3rd_end_dist.sort_values()
        pj_list = poi_label_1st_2nd_3rd_end_dist_sort.index[:5] # choose 5 nearest POIs for each backtool
        pjIdx = np.random.randint(len(pj_list))
        pj = pj_list[pjIdx]
        print('end poi:', pj)

        ## If end POI is the same with start POI, which forms a loop, then we need to change end POI to the 2nd nearst poi
        if pi == pj:
            if pjIdx + 1 < len(pi_list):
                pj = pj_list[pjIdx + 1]
            elif pjIdx - 1 >= 0:
                pj = pj_list[pjIdx - 1]
            print('end poi:', pj)

        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st_2nd_3rd, poi_poi_distmat_label_1st_2nd_3rd, pi, pj, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)
    else:
        raise Exception("Too long playtime")
    
    print(rankPOIs_label_ILP)
    print(seqTime_label_ILP)

    POIs_Eachday, endPOI_Eachday =  timeSeq2Traj(rankPOIs_label_ILP, seqTime_label_ILP, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)
    
    return rankPOIs_label_ILP, seqTime_label_ILP, POIs_Eachday, endPOI_Eachday

def traj_label_T_Days_At_Will(startTrans, backtool, dayCount, cityCode, cityCodeDict, label, startTimeIn, endTimeOut, startDayTime=9, endDayTime=18):

    cityIdx = np.random.randint(len(cityCodeDict))
    cityCode = cityCodeDict[cityIdx]
    #print('cityCode:', cityCode)

    poi_city = poi_All[poi_All['cityCode'] == cityCode]

    poi_label_1st = poi_city[poi_city[label[0]] == 5]
    poi_label_2nd = poi_city[poi_city[label[1]] == 5]
    poi_label_3rd = poi_city[poi_city[label[2]] == 5]
    poi_label_1st_2nd = poi_label_1st.append(poi_label_2nd)
    poi_label_1st_2nd_3rd = poi_label_1st_2nd.append(poi_label_3rd)
    #print sum(poi_label_1st['playTime']), sum(poi_label_2nd['playTime']), sum(poi_label_3rd['playTime'])

    # From intype and outtype transportations to find their corresponding nearest pois
    trans_city = trans_All[trans_All['cityCode'] == cityCode]
    trans_city_startTrans = trans_city[trans_city['type'] == startTrans]
    startTrans_idx = trans_city_startTrans.index[0]  
    #print(startTrans_idx)

    trans_city_backtool = trans_city[trans_city['type'] == backtool]    
    backtool_idx = trans_city_backtool.index[0]
    #print(backtool_idx)

    time_firstday = endDayTime - startTimeIn
    time_lastday = endTimeOut - startDayTime
    time_between = (endDayTime - startDayTime) * (dayCount - 2)
    time_sum = time_firstday + time_lastday + time_between
    

    if sum(poi_label_1st['playTime']) >= time_sum: 

        poi_poi_distmat_label_1st = calc_poi_poi_dist_mat(poi_label_1st)

        trans_startTrans_poi_label_1st_distmat = calc_trans_poi_dist_mat(trans_city_startTrans, poi_label_1st)
        poi_label_1st_start_dist = trans_startTrans_poi_label_1st_distmat.loc[startTrans_idx,:]
        poi_label_1st_start_dist_sort = poi_label_1st_start_dist.sort_values()
        pi_list = poi_label_1st_start_dist_sort.index[:5] # choose 5 nearest POIs for each startTrans
        piIdx = np.random.randint(len(pi_list))
        pi = pi_list[piIdx]
        print('start poi:', pi)

        trans_backtool_poi_label_1st_distmat = calc_trans_poi_dist_mat(trans_city_backtool, poi_label_1st)
        poi_label_1st_end_dist = trans_backtool_poi_label_1st_distmat.loc[backtool_idx,:]
        poi_label_1st_end_dist_sort = poi_label_1st_end_dist.sort_values()
        pj_list = poi_label_1st_end_dist_sort.index[:5] # choose 5 nearest POIs for each backtool
        pjIdx = np.random.randint(len(pi_list))
        pj = pj_list[pjIdx]
        print('end poi:', pj)

        ## If end POI is the same with start POI, which forms a loop, then we need to change end POI to the 2nd nearst poi
        if pi == pj:
            if pjIdx + 1 < len(pi_list):
                pj = pj_list[pjIdx + 1]
            elif pjIdx - 1 >= 0:
                pj = pj_list[pjIdx - 1]
            print('end poi:', pj)

        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st, poi_poi_distmat_label_1st, pi, pj, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)

    elif sum(poi_label_1st_2nd['playTime']) >= time_sum:

        poi_poi_distmat_label_1st_2nd = calc_poi_poi_dist_mat(poi_label_1st_2nd)

        trans_startTrans_poi_label_1st_2nd_distmat = calc_trans_poi_dist_mat(trans_city_startTrans, poi_label_1st_2nd)
        poi_label_1st_2nd_start_dist = trans_startTrans_poi_label_1st_2nd_distmat.loc[startTrans_idx,:]
        poi_label_1st_2nd_start_dist_sort = poi_label_1st_2nd_start_dist.sort_values()
        pi_list = poi_label_1st_2nd_start_dist_sort.index[:5] # choose 5 nearest POIs for each startTrans
        piIdx = np.random.randint(len(pi_list))
        pi = pi_list[piIdx]
        print('start poi:', pi)


        trans_backtool_poi_label_1st_2nd_distmat = calc_trans_poi_dist_mat(trans_city_backtool, poi_label_1st_2nd)
        poi_label_1st_2nd_end_dist = trans_backtool_poi_label_1st_2nd_distmat.loc[backtool_idx,:]
        poi_label_1st_2nd_end_dist_sort = poi_label_1st_2nd_end_dist.sort_values()
        pj_list = poi_label_1st_2nd_end_dist_sort.index[:5] # choose 5 nearest POIs for each backtool
        pjIdx = np.random.randint(len(pi_list))
        pj = pj_list[pjIdx]
        print('end poi:', pj)


        ## If end POI is the same with start POI, which forms a loop, then we need to change end POI to the 2nd nearst poi
        if pi == pj:
            if pjIdx + 1 < len(pi_list):
                pj = pj_list[pjIdx + 1]
            elif pjIdx - 1 >= 0:
                pj = pj_list[pjIdx - 1]
            print('end poi:', pj)

        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st_2nd, poi_poi_distmat_label_1st_2nd, pi, pj, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)

    elif sum(poi_label_1st_2nd_3rd['playTime']) >= time_sum:    
        
        poi_poi_distmat_label_1st_2nd_3rd = calc_poi_poi_dist_mat(poi_label_1st_2nd_3rd)
        
        trans_startTrans_poi_label_1st_2nd_3rd_distmat = calc_trans_poi_dist_mat(trans_city_startTrans, poi_label_1st_2nd_3rd)  
        poi_label_1st_2nd_3rd_start_dist = trans_startTrans_poi_label_1st_2nd_3rd_distmat.loc[startTrans_idx,:]
        poi_label_1st_2nd_3rd_start_dist_sort = poi_label_1st_2nd_3rd_start_dist.sort_values()
        pi_list = poi_label_1st_2nd_3rd_start_dist_sort.index[:5] # choose 5 nearest POIs for each startTrans
        piIdx = np.random.randint(len(pi_list))
        pi = pi_list[piIdx]
        print('start poi:', pi)

        trans_backtool_poi_label_1st_2nd_3rd_distmat = calc_trans_poi_dist_mat(trans_city_backtool, poi_label_1st_2nd_3rd)
        poi_label_1st_2nd_3rd_end_dist = trans_backtool_poi_label_1st_2nd_3rd_distmat.loc[backtool_idx,:]
        poi_label_1st_2nd_3rd_end_dist_sort = poi_label_1st_2nd_3rd_end_dist.sort_values()
        pj_list = poi_label_1st_2nd_3rd_end_dist_sort.index[:5] # choose 5 nearest POIs for each backtool
        pjIdx = np.random.randint(len(pi_list))
        pj = pj_list[pjIdx]
        print('end poi:', pj)

        ## If end POI is the same with start POI, which forms a loop, then we need to change end POI to the 2nd nearst poi
        if pi == pj:
            if pjIdx + 1 < len(pi_list):
                pj = pj_list[pjIdx + 1]
            elif pjIdx - 1 >= 0:
                pj = pj_list[pjIdx - 1]
            print('end poi:', pj)

        rankPOIs_label_ILP, seqTime_label_ILP = path_ILP_T_Days(poi_label_1st_2nd_3rd, poi_poi_distmat_label_1st_2nd_3rd, pi, pj, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)
    else:
        raise Exception("Too long playtime")
        
    POIs_Eachday, endPOI_Eachday =  timeSeq2Traj(rankPOIs_label_ILP, seqTime_label_ILP, dayCount, startTimeIn, endTimeOut)
    
    return rankPOIs_label_ILP, seqTime_label_ILP, POIs_Eachday, endPOI_Eachday

def traj_with_N_defaults(startTrans, backtool, dayCount, cityCodeDict, label, startTimeIn, endTimeOut, N, startDayTime=9, endDayTime=18):

    POIs_list = []
    timeSeq_list = []
    POIs_Eachday_list = []
    endPOI_Eachday_list = []

    for _ in range(N):
        rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday \
            = traj_label_T_Days_At_Will(startTrans, backtool, dayCount, cityCodeDict, label, startTimeIn, endTimeOut, startDayTime, endDayTime)
        
        POIs_list.append(rankPOIs_ILP)
        timeSeq_list.append(seqTime_ILP)
        POIs_Eachday_list.append(POIs_Eachday)
        endPOI_Eachday_list.append(endPOI_Eachday)

    return POIs_list, timeSeq_list, POIs_Eachday_list, endPOI_Eachday_list

def traj_label_T_days_ILP_modification(startTrans, backtool, dayCount, cityCode, label, startTimeIn, endTimeOut, delPOIs, addPOIs, startDayTime=9, endDayTime=18):

    rankPOIs, _, = traj_label_T_Days_ILP_Trans(startTrans, backtool, dayCount, cityCode, label, startTimeIn, endTimeOut, startDayTime, endDayTime)

    rankPOIs.remove(delPOIs)
    rankPOIs.append(addPOIs)

    ps = rankPOIs[0]
    pe = rankPOIs[-1]
    L = len(rankPOIs)

    poi_label = poi_city[poi_city.index ==  rankPOIs] 
    poi_poi_distmat_label = calc_poi_poi_dist_mat(poi_label)

    rankPOIs_ILP, seqTime_ILP = path_ILP_L_POIs(poi_label, poi_poi_distmat_label, ps, pe, L)
    POIs_Eachday, endPOI_Eachday =  timeSeq2Traj(rankPOIs_ILP, seqTime_ILP, dayCount, startTimeIn, endTimeOut, startDayTime, endDayTime)

    return rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday



# ps = 36
# pe = 30
# L = 8
# dayCount = 3
# N = 5
# startTrans = 2
# backtool = 1

# cityCodeDict = ['XMN', 'SZX', 'CAN']
# cityCode = 'CAN'  # Define a more general dictionary as cityCodeDict = {'厦门'.decode('utf8').encode('gb2312'):'XMN', ...} 
# poi_city = poi_All[poi_All[u"cityCode"] == cityCode]

# hotel_city = hotel_All[hotel_All['cityCode'] == cityCode]
# trans_city = trans_All[trans_All['cityCode'] == cityCode]

# # startTimeIn = [9] * dayCount  #AM Start time for the 1st POI of each day 
# # endTimeOut = [18] * dayCount #PM  End time for each day

# startTimeIn = 9
# endTimeOut = 18

# label = []
# tags = emo2tag.getTags("我毕业了")
# #print tags[0], tags[1], tags[2]

# tagDict = {'文艺'.decode('utf8').encode('gb2312'):'literature', \
#     '刺激'.decode('utf8').encode('gb2312'):'excite', \
#     '舒适'.decode('utf8').encode('gb2312'):'comfort', \
#     '探险'.decode('utf8').encode('gb2312'):'exploration', \
#     '艳遇'.decode('utf8').encode('gb2312'):'encounter'}
# print tagDict
# #print tagDict['文艺'.decode('utf8').encode('gb2312')]
# print tagDict[tags[0]]

# label.append(tagDict[tags[0]])
# label.append(tagDict[tags[1]])
# label.append(tagDict[tags[2]])
# print label

## Case 1
# print('Trajectory via POI-POI distance: ', ps, '->', pe, 'POIs', L)
# rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday = traj_L_POIs_ILP_POI(ps, pe, L, poi_city, startTimeIn, endTimeOut)

# # ## Case 2
# print('Trajectory via POI-POI distance: ', ps, '->', pe, 'Day', dayCount)
# rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday = traj_T_Days_ILP_POI(ps, pe, dayCount, poi_city, startTimeIn, endTimeOut)

# ## CAse 3
# print('Trajectory of specific label via POI-POI distance: ', ps, '->', pe, 'POIs', L)
# rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday = traj_label_L_POIs_ILP_POI(ps, pe, L, cityCode, label, startTimeIn, endTimeOut)

# # # ## Case 4
# print('Trajectory of specific label via POI-POI distance: ', ps, '->', pe, 'Day', dayCount)
# rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday = traj_label_T_Days_ILP_POI(ps, pe, dayCount, cityCode, label, startTimeIn, endTimeOut)

# # ## Case 5: 
# print('Trajectory of startTrans', startTrans, 'and backtool', backtool, 'Day', dayCount)
# rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday = traj_label_T_Days_ILP_Trans(startTrans, backtool, dayCount, cityCode, label, startTimeIn, endTimeOut)

# print('Rank POI thru distance via ILP:', rankPOIs_ILP)
# print('Time sequence thru distance via ILP:', seqTime_ILP)
# print('POIs in each day:')
# for i in range(len(POIs_Eachday)):
#     if len(POIs_Eachday[i]) != 0:
#         print POIs_Eachday[i]

# poi_hotel_distmat = calc_poi_hotel_dist_mat(poi_city, hotel_city)
# print('End POI in each day:')
# day = 1
# for i in range(len(endPOI_Eachday)):
#     print 'day', day, ':', endPOI_Eachday[i]

#     ### Recommend hotels: first find nearest hotels for each endPOI; 
#     ## then customers can filter each type (economic, luxurious, cost-effective) according to the price and rates
#     hotel_loc_dict = poi_hotel_distmat.loc[endPOI_Eachday[i],:]
#     #print hotel_loc_dict[:]
#     hotel_loc_dist_sort = hotel_loc_dict.sort_values()
#     print 'Recommended hotels:', hotel_loc_dist_sort.index[:6].tolist() # recommend 6 near hotels for each endPOI
    
#     day += 1

# Case 6: Recommend N trajectories at will one by one
# for j in range(N):

#     print('Trajectory', j, 'starts with Day', dayCount)
#     rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday = traj_label_T_Days_At_Will(startTrans, backtool, dayCount, cityCodeDict, label, startTimeIn, endTimeOut)

#     print('Rank POI thru distance via ILP:', rankPOIs_ILP)
#     print('Time sequence thru distance via ILP:', seqTime_ILP)
#     print('POIs in each day:')
#     for i in range(len(POIs_Eachday)):
#         if len(POIs_Eachday[i]) != 0:
#             print POIs_Eachday[i]
#     print('Trajectory', j, 'ends')

## Case 7: Recommend N trajectories at will one time
# POIs_list, timeSeq_list, POIs_Eachday_list, endPOI_Eachday_list = traj_with_N_defaults(startTrans, backtool, dayCount, cityCodeDict, label, startTimeIn, endTimeOut, N)

# for j in range(len(POIs_list)):

#     print('Trajectory', i, 'starts')
#     print('Rank POI thru distance via ILP:', POIs_list[j])
#     print('Time sequence thru distance via ILP:', timeSeq_list[j])
#     print('POIs in each day:')
#     for i in range(len(POIs_Eachday_list[j])):
#         if len(POIs_Eachday_list[j][i]) != 0:
#             print POIs_Eachday_list[j][i] 
#     print('Trajectory', i, 'ends')   


# # ## Case 8: Recommend trajectory with modification
# rankPOIs, seqTime, _, _, = traj_label_T_Days_ILP_Trans(startTrans, backtool, dayCount, cityCode, label, startTimeIn, endTimeOut)
# print(rankPOIs)
# print(seqTime)

# ps = rankPOIs[0]
# pe = rankPOIs[-1]

# addPOIs = [132, 137]
# delPOIs = [139, 125]

# assert(ps not in delPOIs)
# assert(pe not in delPOIs)

# for delPOI in delPOIs:
#     rankPOIs.remove(delPOI)
# for addPOI in addPOIs:
#     rankPOIs.append(addPOI)
# print(rankPOIs)

# #ps = rankPOIs[0]
# #pe = rankPOIs[-1]
# L = len(rankPOIs)

# poi_final = poi_city.ix[rankPOIs] 
# poi_poi_distmat_final = calc_poi_poi_dist_mat(poi_final)
# rankPOIs_ILP, seqTime_ILP = path_ILP_L_POIs(poi_final, poi_poi_distmat_final, ps, pe, L)
# print(rankPOIs_ILP)
# print(seqTime_ILP)

# POIs_Eachday, endPOI_Eachday =  timeSeq2Traj_with_modification(rankPOIs_ILP, seqTime_ILP, dayCount)

# print('Rank POI thru distance via ILP:', rankPOIs_ILP)
# print('Time sequence thru distance via ILP:', seqTime_ILP)
# print('POIs in each day:')
# for i in range(len(POIs_Eachday)):
#     if len(POIs_Eachday[i]) != 0:
#         print POIs_Eachday[i]



######### Cases for wangbin #######

ps = 36
pe = 30
L = 8
dayCount = 5
N = 5
startTrans = 2
backtool = 1

cityCodeDict = ['XMN', 'SZX', 'CAN']
cityCode = 'XMN'  # Define a more general dictionary as cityCodeDict = {'厦门'.decode('utf8').encode('gb2312'):'XMN', ...} 
startTimeIn = 9 #AM Start time for the 1st POI of the 1st day 
endTimeOut = 18 #PM  End time for the last POI of the last day

tags = emo2tag.getTags('我毕业了')
print unicode(tags[0], 'gb2312'), unicode(tags[1], 'gb2312'),  unicode(tags[1], 'gb2312')

# tagDict = {'文艺'.decode('utf8').encode('gb2312'):'literature', \
#     '刺激'.decode('utf8').encode('gb2312'):'excite', \
#     '舒适'.decode('utf8').encode('gb2312'):'comfort', \
#     '探险'.decode('utf8').encode('gb2312'):'exploration', \
#     '艳遇'.decode('utf8').encode('gb2312'):'encounter'}
# print tagDict
# #print tagDict['文艺'.decode('utf8').encode('gb2312')]
# print tagDict[tags[0]]

# label.append(tagDict[tags[0]])
# label.append(tagDict[tags[1]])
# label.append(tagDict[tags[2]])
# print label


#### User defines transportation, start time of the 1st day, end time of the last day, city code, dayCount
def getRoutes_type1(cityCode, dayCount, tags, startTrans=1, backtool=1, startTimeIn=9, endTimeOut=18, startDayTime=9, endDayTime=18):
    

    tagDict = {'文艺':'literature', \
    '刺激':'excite', \
    '舒适':'comfort', \
    '探险':'exploration', \
    '艳遇':'encounter'}
    print(tagDict)
    #print tagDict
    #print tagDict['文艺'.decode('utf8').encode('gb2312')]
    #print tagDict[tags[0]]

    label = []
    label.append(tagDict[tags[0]])
    label.append(tagDict[tags[1]])
    label.append(tagDict[tags[2]])
    #print label


    rankPOIs_ILP, seqTime_ILP, POIs_Eachday, endPOI_Eachday = traj_label_T_Days_ILP_Trans(startTrans, backtool, dayCount, cityCode, label, startTimeIn, endTimeOut, startDayTime, endDayTime)
    print('POIs:', rankPOIs_ILP)
    print('PlayTime:', seqTime_ILP)

    hotel_loc_dict = [[] for _ in range(dayCount)]
    hotel_loc_dist_sort = [[] for _ in range(dayCount)]

    poi_city = poi_All[poi_All[u"cityCode"] == cityCode]
    hotel_city = hotel_All[hotel_All['cityCode'] == cityCode]
    

    poi_hotel_distmat = calc_poi_hotel_dist_mat(poi_city, hotel_city)

    for i in range(dayCount):
        hotel_loc_dict[i] = poi_hotel_distmat.loc[endPOI_Eachday[i],:]
        hotel_loc_dist_sort[i] = hotel_loc_dict[i].sort_values()
        print 'Recommended hotels:', hotel_loc_dist_sort[i].index[:6].tolist() # recommend 6 near hotels for each endPOI
        
    all_data = {}
    #fill all right data
    day = [{} for _ in range(dayCount)]
    for i in range(dayCount):
        day[i]['cityCode'] = cityCode
        day[i]['dayCount'] = dayCount
        day[i]['startTrans'] = startTrans
        day[i]['backTrans'] = backtool
        day[i]['startTimeIn'] = startTimeIn
        day[i]['endTimeOut'] = endTimeOut
        day[i]['category'] = tags[0]
        day[i]['dayNum'] = i + 1
        day[i]['scenics'] = POIs_Eachday[i]
        day[i]['timeSeq'] = seqTime_ILP[i]
        day[i]['endPOI'] = endPOI_Eachday[i]
        day[i]['hotels'] = hotel_loc_dist_sort[i].index[:6].tolist()
        all_data[day[i]['dayNum']] = day[i]

    return all_data




def getRoutes_type2(cityCode, dayCount, tags, idList, nType=1, backtool=1, startTimeIn=9, endTimeOut=18, startDayTime=9, endDayTime=18):
    

    tagDict = {'文艺'.decode('utf8').encode('gb2312'):'literature', \
    '刺激'.decode('utf8').encode('gb2312'):'excite', \
    '舒适'.decode('utf8').encode('gb2312'):'comfort', \
    '探险'.decode('utf8').encode('gb2312'):'exploration', \
    '艳遇'.decode('utf8').encode('gb2312'):'encounter'}
    #print tagDict
    #print tagDict['文艺'.decode('utf8').encode('gb2312')]
    #print tagDict[tags[0]]

    label = []
    label.append(tagDict[tags[0]])
    label.append(tagDict[tags[1]])
    label.append(tagDict[tags[2]])
    #print label

    ps = idList[0]
    pe = idList[-1]
    L = len(idList)

    poi_city = poi_All[poi_All[u"cityCode"] == cityCode]
    hotel_city = hotel_All[hotel_All['cityCode'] == cityCode]
    

    poi_final = poi_city.ix[idList] 
    poi_poi_distmat_final = calc_poi_poi_dist_mat(poi_final)
    poi_hotel_distmat = calc_poi_hotel_dist_mat(poi_final, hotel_city)


    rankPOIs_ILP, seqTime_ILP = path_ILP_L_POIs(poi_final, poi_poi_distmat_final, ps, pe, L)
    POIs_Eachday, endPOI_Eachday =  timeSeq2Traj_with_modification(rankPOIs_ILP, seqTime_ILP, dayCount)

    hotel_loc_dict = [[] for _ in range(dayCount)]
    hotel_loc_dist_sort = [[] for _ in range(dayCount)]

    
    for i in range(dayCount):
        hotel_loc_dict[i] = poi_hotel_distmat.loc[endPOI_Eachday[i],:]
        hotel_loc_dist_sort[i] = hotel_loc_dict[i].sort_values()
        print 'Recommended hotels:', hotel_loc_dist_sort[i].index[:6].tolist() # recommend 6 near hotels for each endPOI
        
    all_data = {}
    #fill all right data
    day = [{} for _ in range(dayCount)]
    for i in range(dayCount):
        day[i]['cityCode'] = cityCode
        day[i]['dayCount'] = dayCount
        day[i]['startTrans'] = startTrans
        day[i]['backTrans'] = backtool
        day[i]['startTimeIn'] = startTimeIn
        day[i]['endTimeOut'] = endTimeOut
        day[i]['category'] = tags[0]
        day[i]['dayNum'] = i + 1
        day[i]['scenics'] = POIs_Eachday[i]
        day[i]['timeSeq'] = seqTime_ILP[i]
        day[i]['endPOI'] = endPOI_Eachday[i]
        day[i]['hotels'] = hotel_loc_dist_sort[i].index[:6].tolist()
        all_data[day[i]['dayNum']] = day[i]

    return all_data

def loop_listen_redis():
    pool=redis.ConnectionPool(host=local_ip,port=6379,db=1)
    r = redis.StrictRedis(connection_pool=pool)
    p = r.pubsub()
    p.subscribe('route')
    for item in p.listen():
        print(item)
        if item['type'] == 'message':
            data = item['data']
            dic = eval(data)

            if dic['cmd'] == 'finish':
                mark_user_route_finish(dic['userId'])
            elif dic['cmd'] == 'create':
                insert_to_db(get_AI_route(dic))
            
    p.unsubscribe('route')

def trans_tags(tags):
    tagDict = {u'文艺':'literature', u'刺激':'excite', u'舒适':'comfort', u'探险':'exploration', u'艳遇':'encounter'}


    tags[0] = tagDict[tags[0].decode('utf-8')]
    tags[1] = tagDict[tags[1].decode('utf-8')]
    tags[2] = tagDict[tags[2].decode('utf-8')]

    return tags

def get_AI_route(input):
    tags = []
    for tag in input['tags'].strip().split():
        tags.append(tag)
    
    append_size = 3 - len(tags);
    while append_size > 0:
        tags.append(tags[0])
        append_size -= 1

    #tags = trans_tags(tags)
    print(tags)
    if input.has_key('scenicList') and input['scenicList'] != '':
        scenicList = []
        for item in input['scenicList'].split():
            if item != '':
                scenicList.append(int(item))
        data = getRoutes_type1(input['cityCode'] , input['dayCount'], tags, scenicList, input['startTool'], input['endTool'], int(input['startTime'][:2]), int(input['endTime'][:2]))
    else:
        if input.has_key('startTool'):
            data = getRoutes_type1(input['cityCode'] , input['dayCount'], tags, input['startTool'], input['endTool'], int(input['startTime'][:2]), int(input['endTime'][:2]))
        else:
            data = getRoutes_type1(input['cityCode'] , input['dayCount'], tags)
    data['userId'] = input['userId']
    print('get_AI_route')
    print(data)
    return data

def connect_db():
    global db_conn
    db_conn = MySQLdb.connect(host=local_ip, port = 3306, user=db_user, passwd=db_pw,db=db_name,charset='utf8')

def mark_user_route_finish(userId):
    global db_conn
    cur = db_conn.cursor()
    sql = 'UPDATE IMRoute SET status=1 WHERE status=0 and userId=' + str(userId)
    cur.execute(sql)
    db_conn.commit()
    cur.close()

def insert_to_db(data):
    global db_conn
    cur = db_conn.cursor()
    userId = data['userId']

    sql = "SELECT MAX(lineId) from IMRoute"
    cur.execute(sql)
    id = 1;
    for lineId in cur.fetchall():
        if lineId[0] is None:
            id = 1
        else:
            id = lineId[0] + 1

    sql = "INSERT INTO IMRoute (userId, lineId, cityCode, dayCount, startTool, endTool, startTime, endTime, quality, dayNum,routes, hotels, status) VALUES "

    for k,v in data.items():
        if k == 'userId':
            continue
            
        routes = ""
        for item in v['scenics']:
            routes += str(item) + " "

        hotels = ""
        for item in v['hotels']:
            hotels += str(item) + " "

        sql += "({0}, {1}, '{2}', {3}, {4}, {5}, '{6}', '{7}', '{8}', {9}, '{10}', '{11}', 0),".format(
        userId, id, v['cityCode'], v['dayCount'], v['startTrans'], v['backTrans'], "%02d:00" % v['startTimeIn'], "%02d:00" % v['endTimeOut'], v['category'], v['dayNum'], routes.strip(), hotels.strip())
        
    sql = sql[:-1]
    print(sql)

    cur.execute(sql)
    db_conn.commit()
    cur.close()


############ End for wangbin ##########

if __name__ == '__main__':
    connect_db()
    loop_listen_redis()
    db_conn.close()

    '''
    all_data = getRoutes_type1(cityCode, dayCount, label, inType, outType, startTimeIn, endTimeOut, 9, 18)
    print all_data

    idList = [126, 121, 175, 178, 141, 132, 136, 134, 147, 137]
    all_data = getRoutes_type2(cityCode, dayCount, label, idList, inType, outType, startTimeIn, endTimeOut, 9, 18)
    print all_data
    '''