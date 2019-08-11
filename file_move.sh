#!/bin/bash
hadoop fs -mkdir /user/hive/$1
hadoop fs -put /py_code/hive/$2 /user/hive/$1/$2