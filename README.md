# PaymentService
**Guide to running the application.**

1. Create the database
   Run: create database payment_service_db;

2. Create the table
   Run the create table payments command below;
   CREATE TABLE payment (
   payment_id BIGSERIAL PRIMARY KEY,
   product VARCHAR(255) NOT NULL,
   quantity INT NOT NULL,
   card_number BIGINT NOT NULL,
   currency VARCHAR(10) NOT NULL,
   customer VARCHAR(255) NOT NULL
   );

3. Run Kafka
   Go to the root directory of the notification service.
   Make sure docker is running.
   Run: docker compose up
   This will run both kafka and zookeeper and create a topic called payments. 

4. Run the payment service and send requests from postman.