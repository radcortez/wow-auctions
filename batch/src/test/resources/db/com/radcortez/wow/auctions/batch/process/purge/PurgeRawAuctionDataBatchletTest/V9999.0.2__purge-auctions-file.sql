insert into AuctionFile (id, fileName, fileStatus, lastModified, url, realm_id) values (1, 'auction.json', 3, 1, null, 1);

insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (1, 125, 160, 1, null, 1, 1, 1);
insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (2, 213, 255, 1, null, 3, 1, 1);
insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (3, 595, 700, 1, null, 5, 1, 1);
insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (4, 26, 66, 2, null, 1, 1, 1);
insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (5, 185, 205, 2, null, 5, 1, 1);
insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (6, 54, 54, 2, null, 18, 1, 1);
insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (7, 125, 220, 3, null, 1, 1, 1);

insert into AuctionFile (id, fileName, fileStatus, lastModified, url, realm_id) values (2, 'auction.json', 3, 1, null, 1);
insert into Auction (id, bid, buyout, itemId, ownerRealm, quantity, auctionFile_id, realm_id) values (1, 125, 160, 1, null, 1, 2, 1);
