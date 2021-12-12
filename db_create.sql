# Step 1: Create user (execute under root)
# Note: if you want to see all users: select * from mysql.user;
CREATE USER 'user'@'%' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' WITH GRANT OPTION;

# Step 2: Create database (execute under the user)
CREATE DATABASE testmethods;
USE testmethods;

# Step 3: Create tables
CREATE TABLE `CUSTOMER` (
	`id` TEXT NOT NULL,
	`first_name` TEXT NOT NULL,
	`last_name` TEXT NOT NULL,
	`login` TEXT NOT NULL,
	`pass` TEXT NOT NULL,
	`balance` INT(11) NOT NULL DEFAULT '0'
);

CREATE TABLE `PLAN` (
	`id` TEXT NOT NULL,
	`name` TEXT NOT NULL,
	`details` TEXT NOT NULL,
	`fee` INT(11) NOT NULL
);

CREATE TABLE `SUBSCRIPTION` (
	`id` TEXT NOT NULL,
	`customer_id` TEXT NOT NULL,
	`plan_id` TEXT NOT NULL
);
