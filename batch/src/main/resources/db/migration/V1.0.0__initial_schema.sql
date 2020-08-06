create table ConnectedRealm (
   id varchar(255) not null,
    primary key (id),
    region integer,
);

create table Folder (
   connectedRealm_id varchar(255) not null,
    folderType integer not null,
    path varchar(255),
    primary key (connectedRealm_id, folderType)
);

create table Realm (
   id varchar(255) not null,
    name varchar(255),
    slug varchar(255),
    connectedRealm_id varchar(255),
    primary key (id)
);

create table Auction (
   id varchar(255) not null,
    bid bigint,
    buyout bigint,
    itemId integer,
    quantity integer,
    auctionFile_id varchar(255),
    connectedRealm_id varchar(255),
    primary key (id, auctionFile_id)
);

create table AuctionFile (
   id varchar(255) not null,
    fileName varchar(255),
    fileStatus integer,
    connectedRealm_id varchar(255),
    primary key (id)
);
