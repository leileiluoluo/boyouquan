-- for cloud database
-- mysqldump -h 172.19.0.2 -P 3306 -u root -p --set-gtid-purged=OFF boyouquan-dev > /tmp/boyouquan-dev.sql

-- only export data
mysqldump --complete-insert -u root -p -t boyouquan > /tmp/boyouquan.sql

-- import
mysql -u root -p boyouquan < /tmp/boyouquan.sql
