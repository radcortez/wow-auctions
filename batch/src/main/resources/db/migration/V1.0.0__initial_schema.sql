create table ConnectedRealm (
   id varchar(255) not null,
    region varchar(3),
    primary key (id)
);

create table Realm (
   id varchar(255) not null,
    name varchar(255),
    slug varchar(255),
    connectedRealm_id varchar(255),
    primary key (id)
);

alter table Realm add foreign key (connectedRealm_id) references ConnectedRealm(id);

create table Folder (
   connectedRealm_id varchar(255) not null,
    folderType varchar(10) not null,
    path varchar(255),
    primary key (connectedRealm_id, folderType)
);

alter table Folder add foreign key (connectedRealm_id) references ConnectedRealm(id);

create table AuctionFile (
   id bigint not null,
    fileName varchar(255),
    fileStatus varchar(20),
    timestamp bigint,
    connectedRealm_id varchar(255),
    primary key (id)
);

alter table AuctionFile add foreign key (connectedRealm_id) references ConnectedRealm(id);

create table Auction (
   id bigint not null,
    bid bigint,
    buyout bigint,
    itemId integer,
    quantity integer,
    auctionFile_id bigint,
    primary key (id, auctionFile_id)
);

alter table Auction add foreign key (auctionFile_id) references AuctionFile(id);

create table AuctionStatistics (
   id bigint not null,
    itemId integer,
    quantity bigint,
    bid bigint,
    minBid bigint,
    maxBid bigint,
    buyout bigint,
    minBuyout bigint,
    maxBuyout bigint,
    avgBid double precision,
    avgBuyout double precision,
    timestamp bigint,
    connectedRealm_id varchar(255),
    primary key (id)
);

alter table AuctionStatistics add foreign key (connectedRealm_id) references ConnectedRealm(id);

create sequence hibernate_sequence start with 1 increment by 1;
create sequence auction_file_id start with 1 increment by 1;
