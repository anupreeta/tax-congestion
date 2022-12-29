# calculate-congestion-tax
* A SpringBoot application to calculate congestion tax for vehicles in a city for a specific year. 
* The application is easily configurable to cater for different cities, vehicles and years using PostGreSQL database. 
The tax rules are also configurable per city.
* Database init script is in `sql` folder to create database `calculator`
* Flyway is used for data migration. SQL scripts for creating and populating tables are 
under `src/main/resources/db/migration`

## REST API ENDPOINT
`http://localhost:8080/api/v1/tax`

## How to run the application in dockerized container using docker-compose
* Pre-requisite:
    * Docker
* Build and run tests locally:
   `mvn clean install`

* To build and run application with `postgresql` database
``````
  * docker-compose up
``````

* To shutdown the application:
````
  * docker-compose down
  * docker rmi docker-tax-calculator-postgres:latest
````

## Testing

To calculate congestion tax for a vehicle for a list of date entries in a city(Eg: Gothenburg), send a POST 
request as below:

http://localhost:8080/api/v1/tax

### Request Body:

````
{
    "vehicle": {
    "type": "Car"
},
"checkInTime": [
                "2013-01-14 06:00:00","2013-01-14 07:30:00",
                "2013-01-14 15:33:27","2013-01-14 16:40:00",
                "2013-02-08 06:27:00","2013-02-08 06:20:27",
                "2013-02-08 14:35:00","2013-02-08 15:29:00",
                "2013-02-08 15:47:00","2013-02-08 16:01:00",
                "2013-02-08 16:48:00","2013-02-08 17:49:00",
                "2013-02-08 18:29:00","2013-02-08 18:35:00",
                "2013-03-26 14:25:00","2013-03-28 14:07:27"
                ]
}
````

### Response Body

````````
{
    "taxAmount": 128.00,
    "datewiseTaxCharges": {
        "2013-01-14": 60,
        "2013-03-26": 8.00,
        "2013-02-08": 60
    }
}
````````

## Connect to PostGreSQL Database docker container
``````
docker exec -it db /bin/bash
bash-5.1# psql -h localhost -p 5432 -d calculator -U myuser
psql (13.1)
Type "help" for help.

calculator=# \dt
                List of relations
 Schema |         Name          | Type  | Owner  
--------+-----------------------+-------+--------
 public | city                  | table | myuser
 public | city_holiday_months   | table | myuser
 public | city_holidays         | table | myuser
 public | city_tax_charges      | table | myuser
 public | city_tax_days         | table | myuser
 public | city_tax_rules        | table | myuser
 public | city_vehicle          | table | myuser
 public | flyway_schema_history | table | myuser
 public | vehicle               | table | myuser
(9 rows)

calculator=# select * from city;
 id |    name    
----+------------
  1 | Gothenburg
(1 row)

calculator=# select * from city_holiday_months;
 city_id | is_january | is_february | is_march | is_april | is_may | is_june | is_july | is_august | is_september | is_october | is_november | is_december 
---------+------------+-------------+----------+----------+--------+---------+---------+-----------+--------------+------------+-------------+-------------
       1 |          0 |           0 |        0 |        0 |      0 |       0 |       1 |         0 |            0 |          0 |           0 |           0
(1 row)

calculator=# select * from city_holidays;
 id |        date         | city_id 
----+---------------------+---------
  1 | 2013-01-01 00:00:00 |       1
  2 | 2013-03-28 00:00:00 |       1
  3 | 2013-03-29 00:00:00 |       1
  4 | 2013-04-01 00:00:00 |       1
  5 | 2013-05-01 00:00:00 |       1
  6 | 2013-05-08 00:00:00 |       1
  7 | 2013-05-09 00:00:00 |       1
  8 | 2013-06-06 00:00:00 |       1
  9 | 2013-06-21 00:00:00 |       1
 10 | 2013-11-01 00:00:00 |       1
 11 | 2013-12-24 00:00:00 |       1
 12 | 2013-12-25 00:00:00 |       1
 13 | 2013-12-26 00:00:00 |       1                     
 14 | 2013-12-31 00:00:00 |       1
(14 rows)

calculator=# select * from city_tax_charges;
 id | city_id | charge | start_time | end_time  
----+---------+--------+-----------+----------
  1 |       1 |   8.00 | 06:00:00  | 06:29:59
  2 |       1 |  13.00 | 06:30:00  | 06:59:59
  3 |       1 |  18.00 | 07:00:00  | 07:59:59
  4 |       1 |  13.00 | 08:00:00  | 08:29:59
  5 |       1 |   8.00 | 08:30:00  | 14:59:59
  6 |       1 |  13.00 | 15:00:00  | 15:29:59
  7 |       1 |  18.00 | 15:30:00  | 16:59:59
  8 |       1 |  13.00 | 17:00:00  | 17:59:59
  9 |       1 |   8.00 | 18:00:00  | 18:29:59
 10 |       1 |   0.00 | 18:30:00  | 23:59:59
 11 |       1 |   0.00 | 00:00:00  | 05:59:59
(11 rows)

calculator=# select * from city_tax_days;
 city_id | is_monday | is_tuesday | is_wednesday | is_thursday | is_friday | is_saturday | is_sunday 
---------+-----------+------------+--------------+-------------+-----------+-------------+-----------
       1 |         1 |          1 |            1 |           1 |         1 |           0 |         0
(1 row)

calculator=# select * from city_tax_rules;
 max_tax_per_day | number_of_tax_free_days_after_holiday | number_of_tax_free_days_before_holiday | single_charge_period_mins | city_entity_id 
-----------------+---------------------------------------+----------------------------------------+-------------------------------+----------------
              60 |                                     0 |                                      1 |                            60 |              1
(1 row)

calculator=# select * from city_vehicle;
 city_id | vehicle_id 
---------+------------
       1 |          1
       1 |          2
       1 |          3
       1 |          4
       1 |          5
       1 |          6
(6 rows)

calculator=# select * from vehicle;
 id |    name    
----+------------
  1 | Emergency
  2 | Bus
  3 | Diplomat
  4 | Motorcycle
  5 | Military
  6 | Foreign
  7 | Car
  8 | Motorbike
(8 rows)

calculator=# select * from flyway_schema_history;
 installed_rank | version | description | type |    script    |  checksum  | installed_by |        installed_on        | execution_time | success 
----------------+---------+-------------+------+--------------+------------+--------------+----------------------------+----------------+---------
              1 | 1       | data        | SQL  | V1__data.sql | -833502865 | myuser       | 2022-12-29 16:02:34.948821 |            114 | t
(1 row)

``````````

