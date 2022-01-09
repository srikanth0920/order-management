CREATE TABLE IF NOT EXISTS orders( 
   order_id bigint auto_increment, 
   order_type VARCHAR(100) NOT NULL, 
   order_description VARCHAR(500) NOT NULL
);