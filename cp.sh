#!/bin/sh

#先从git上拉代码
git fetch
git pull 

#删除并拷贝文件
rm -rf src/main/resources/public/web 
cp -a  web src/main/resources/public/web

#git提交
git add -A
git commit -m "修改前端web"
git push
