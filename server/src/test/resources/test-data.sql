BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transaction_id
    INCREMENT BY 1
    START WITH 3001
    NO MAXVALUE;

CREATE TABLE transactions (
    transaction_id int NOT NULL DEFAULT nextval('seq_transaction_id'),
    transaction_type varchar(10),
    sender_account_id int NOT NULL,
    receiver_account_id int NOT NULL,
    status varchar(10) NOT NULL,
    transfer_amount numeric(10,2) NOT NULL,
    message_text varchar (100),
    CONSTRAINT PK_transaction PRIMARY KEY (transaction_id),
    CONSTRAINT FK_transaction_account FOREIGN KEY (sender_account_id) REFERENCES account (account_id),
	CONSTRAINT FK_transaction_account_receiver FOREIGN KEY (receiver_account_id) REFERENCES account (account_id)
	);


INSERT INTO tenmo_user (username, password_hash)
VALUES ('Bank', '$2a$10$OSDOcp7MgidHCWDcXq1gC.CiHxgSjIbT4c.v7d22S/NCq9cER4lbC'),
       ('bob', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
       ('user', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy');

INSERT INTO account (user_id, balance)
        VALUES(1001, 10000000);
INSERT INTO account (user_id, balance)
        VALUES(1002, 1000);
INSERT INTO account (user_id, balance)
        VALUES(1003, 1000);

INSERT INTO transactions(transaction_type, sender_account_id, receiver_account_id, status, transfer_amount, message_text)
VALUES ('Send', 2001, 2002, 'Approved', 1000, 'Welcome to Tenmo Bank!'),
       ('Send', 2001, 2003, 'Approved', 1000, 'Welcome to Tenmo Bank!'),
       ('Send', 2002, 2003, 'Approved', 150, 'bob sending to user'),
       ('Send', 2003, 2002, 'Approved', 151, 'user sending to bob');

COMMIT;