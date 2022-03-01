CREATE TABLE Product (
                         ID bigint auto_increment PRIMARY KEY,
                         Name varchar(255) DEFAULT NULL,
                         Price double DEFAULT NULL
);

INSERT INTO Product VALUES (1,'BMW 1 Series',100),
                           (2,'BMW 2 Series',200),
                           (3,'BMW 3 Series',300),
                           (4,'BMW 4 Series',400),
                           (5,'BMW 5 Series',500),
                           (6,'BMW 6 Series',600),
                           (7,'BMW 7 Series',700),
                           (8,'BMW 8 Series',800);

CREATE SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 9;