create table ConnectedRealm (
   id varchar(255) not null,
    primary key (id)
);

create table Realm (
   id varchar(255) not null,
    name varchar(255),
    nameAuction varchar(255),
    region integer,
    slug varchar(255),
    connectedRealm_id varchar(255),
    primary key (id)
);

create table Auction (
   id varchar(255) not null,
    bid bigint,
    buyout bigint,
    itemId integer,
    ownerRealm varchar(255),
    quantity integer,
    auctionFile_id varchar(255) not null,
    realm_id varchar(255) not null,
    primary key (id, auctionFile_id)
);

create table AuctionFile (
   id varchar(255) not null,
    fileName varchar(255),
    fileStatus integer,
    lastModified bigint,
    url varchar(255),
    realm_id varchar(255),
    primary key (id)
);
