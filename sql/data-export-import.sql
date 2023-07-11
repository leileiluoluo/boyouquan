mysqldump -h 172.19.0.2 -P 3306 -u root -p --set-gtid-purged=OFF boyouquan-dev > /tmp/boyouquan-dev.sql

mysql -u root -p boyouquan-dev < /tmp/boyouquan-dev.sql