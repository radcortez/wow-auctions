create sequence REALM_ID_SEQ start with 1 increment by 1;

create table Realm (
   id bigint not null,
    name varchar(255),
    nameAuction varchar(255),
    region integer,
    slug varchar(255),
    status boolean not null,
    primary key (id)
);
