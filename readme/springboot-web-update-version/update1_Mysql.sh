#!/bin/bash

# 1. 更新Mysql	

mysql -u root -p'portal@300!**' --database portal -f <sqlScript/update_V1.1.03201_patch.sql
echo "from sqlScript/update_V1.1.03201_patch.sql"


echo "update sql done ."
