#!/bin/sh

#先从git上拉代码
git fecth
git pull 

#删除并拷贝文件
rm -rf web 
cp -a  web1 web

#git提交
git add .
git commit -m "修改前端web"
git push
