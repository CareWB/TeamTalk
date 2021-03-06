#!/bin/sh
export PATH=$PATH:/home/wb/TeamTalk-master/server/src/protobuf/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/wb/TeamTalk-master/server/src/protobuf/lib

SRC_DIR=./
DST_DIR=./gen

#C++
mkdir -p $DST_DIR/cpp
protoc -I=$SRC_DIR --cpp_out=$DST_DIR/cpp/ $SRC_DIR/*.proto

#JAVA
mkdir -p $DST_DIR/java
protoc -I=$SRC_DIR --java_out=$DST_DIR/java/ $SRC_DIR/*.proto

#PYTHON
mkdir -p $DST_DIR/python
protoc -I=$SRC_DIR --python_out=$DST_DIR/python/ $SRC_DIR/*.proto
