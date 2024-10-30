# 1. Create customized MySQL Database Image based on Dockerfile
```
docker build -t mysql-restaurant .

```
# 2. Create a new Container and start it

```
docker run -it -p 3307:3306 --expose 3307 --name mysql-restaurant -v restaurant-db:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=restaurantdb -d mysql-restaurant 
```


# 3. Export .sql file 
```
docker exec -it mysql-restaurant sh -c "mysqldump -uroot -prestaurantdb restaurant>restaurantdb_dump.sql;"
```

# 4. Import .sql file
```
docker cp <local_file_path>/restaurantdb_dump.sql mysql-restaurant:/ 
docker exec -it mysql-restaurant sh -c "mysql -uroot -prestaurantdb restaurant<restaurantdb_dump.sql;"

```