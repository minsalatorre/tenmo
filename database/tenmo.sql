BEGIN TRANSACTION;
DROP TABLE IF EXISTS tenmo_user, account, transactions;
DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transaction_id;
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
    balance numeric(13, 2) NOT NULL,
    CONSTRAINT PK_account PRIMARY KEY (account_id),
    CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);
CREATE SEQUENCE seq_transaction_id
    INCREMENT BY 1
    START WITH 3001
    NO MAXVALUE;
CREATE TABLE transactions (
    transaction_id int NOT NULL DEFAULT nextval('seq_transaction_id'),
    transaction_type varchar(30),
    sender_account_id int NOT NULL,
    receiver_account_id int NOT NULL,
    status varchar(15) NOT NULL,
    transfer_amount numeric(13,2) NOT NULL,
    message_text varchar (100),
    CONSTRAINT PK_transaction PRIMARY KEY (transaction_id),
    CONSTRAINT FK_transaction_account FOREIGN KEY (sender_account_id) REFERENCES account (account_id),
	CONSTRAINT FK_transaction_account_receiver FOREIGN KEY (receiver_account_id) REFERENCES account (account_id)

);
INSERT INTO tenmo_user (username, password_hash)
        VALUES ('Bank', '$2a$10$OSDOcp7MgidHCWDcXq1gC.CiHxgSjIbT4c.v7d22S/NCq9cER4lbC');
INSERT INTO tenmo_user (username, password_hash)
        VALUES ('carly', '$2a$10$VXCsQt/awU67Vgbk7mOzFukLaII1p3T2/LCYUFXkJBynmdzEnlZXO');
INSERT INTO tenmo_user (username, password_hash)
        VALUES ('Mine', '$2a$10$OSDOcp7MgidHCWDcXq1gC.CiHxgSjIbT4c.v7d22S/NCq9cER4lbC');
INSERT INTO account (user_id, balance)
        VALUES(1001, 0);
INSERT INTO account (user_id, balance)
        VALUES(1002, 0);
INSERT INTO account (user_id, balance)
        VALUES(1003, 0);
INSERT INTO transactions(transaction_type, sender_account_id, receiver_account_id, status, transfer_amount, message_text)
                VALUES('Welcome transfer', 2001, 2001, 'Approved', 10000, 'Welcome to Tenmo Bank');
INSERT INTO transactions(transaction_type, sender_account_id, receiver_account_id, status, transfer_amount, message_text)
                VALUES('Welcome transfer', 2001, 2002, 'Approved', 1000, 'Welcome to Tenmo Bank');
INSERT INTO transactions(transaction_type, sender_account_id, receiver_account_id, status, transfer_amount, message_text)
                VALUES('Welcome transfer', 2001, 2003, 'Approved', 1000, 'Welcome to Tenmo Bank');
COMMIT;



