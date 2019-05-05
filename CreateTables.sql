




create table zip_codes(

    zip_code integer(10),
    city varchar(30),
    state varchar(10),
    latitude double,
    longitude double,
    PRIMARY KEY (zip_code)

);

create table stores (
    
    store_id integer NOT NULL,
    address varchar(20),
    city varchar(20),
    state varchar(10),
    type varchar(30),
    PRIMARY KEY (store_id)
);

create table store_hours (
   
    monday varchar(20),
    tuesday  varchar(20),
    wednesday varchar(20), 
    thursday varchar(20),
    friday varchar(20),
    saturday varchar(20),
    sunday varchar(20),
    store_id integer,
    FOREIGN KEY (store_id) REFERENCES stores(store_id)
);

create table log (
     
    id varchar(20),
    password varchar(20),
    PRIMARY KEY (id)
);
                