package com.radcortez.wow.auctions.entity;

import com.radcortez.flyway.test.annotation.DataSource;
import com.radcortez.flyway.test.annotation.FlywayTest;
import com.radcortez.wow.auctions.QuarkusDataSourceProvider;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@FlywayTest(@DataSource(QuarkusDataSourceProvider.class))
class AuctionFileTest {
    @Test
    @Transactional
    void create() {
        ConnectedRealm connectedRealm = ConnectedRealm.findById("1");

        AuctionFile auctionFile = AuctionFile.builder()
                                             .fileName("payload-1597409317668.json")
                                             .fileStatus(FileStatus.DOWNLOADED)
                                             .connectedRealm(connectedRealm)
                                             .build();

        AuctionFile created = auctionFile.create();
        assertNotNull(created);
        assertNotNull(created.getId());
    }
}
