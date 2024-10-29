# 1. Create customized MySQL Database Image based on Dockerfile
```
docker build -t mysql-restaurant .

```
# 2. Create a new Container and start it

```
docker run -it -p 3307:3306 --expose 3307 --name mysql-restaurant -e MYSQL_ROOT_PASSWORD=restaurantdb -d mysql-restaurant
```
