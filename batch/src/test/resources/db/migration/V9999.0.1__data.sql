insert into ConnectedRealm (id, region) values (1, 1);

insert into Realm (id, name, slug, connectedRealm_id) values (1, 'Grim Batol', 'grimbatol', 1);

insert into Folder(connectedRealm_id, folderType, path) values (1, 0, 'FI')
