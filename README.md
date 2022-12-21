Run MySQL container
```agsl
sudo docker run --name pfm-db -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=pfm-dev -d -p 3306:3306 mysql:latest
```