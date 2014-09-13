package com.radcortez.wow.auctions.batch.process;

import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionHouse;
import com.radcortez.wow.auctions.entity.FolderType;

import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.Serializable;

import static org.apache.commons.io.FileUtils.openInputStream;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemReader extends AbstractAuctionFileProcess implements ItemReader {
    private JsonParser parser;
    private AuctionHouse auctionHouse;

    @Inject
    private JobContext jobContext;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        // todo - Configure folderType
        setParser(Json.createParser(openInputStream(getContext().getFileToProcess(FolderType.FI_TMP))));
    }

    @Override
    public void close() throws Exception {}

    @Override
    public Object readItem() throws Exception {
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            Auction auction = new Auction();
            switch (event) {
                case KEY_NAME:
                    updateAuctionHouseIfNeeded(auction);

                    if (readAuctionItem(auction)) {
                        return auction;
                    }
                    break;
            }
        }
        return null;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }

    protected void updateAuctionHouseIfNeeded(Auction auction) {
        if (parser.getString().equalsIgnoreCase(AuctionHouse.ALLIANCE.toString())) {
            auctionHouse = AuctionHouse.ALLIANCE;
        } else if (parser.getString().equalsIgnoreCase(AuctionHouse.HORDE.toString())) {
            auctionHouse = AuctionHouse.HORDE;
        } else if (parser.getString().equalsIgnoreCase(AuctionHouse.NEUTRAL.toString())) {
            auctionHouse = AuctionHouse.NEUTRAL;
        }

        auction.setAuctionHouse(auctionHouse);
    }

    protected boolean readAuctionItem(Auction auction) {
        if (parser.getString().equalsIgnoreCase("auc")) {
            parser.next();
            auction.setAuctionId(parser.getLong());
            parser.next();
            parser.next();
            auction.setItemId(parser.getInt());
            parser.next();
            parser.next();
            auction.setOwner(parser.getString());
            parser.next();
            parser.next();
            auction.setOwnerRealm(parser.getString());
            parser.next();
            parser.next();
            auction.setBid(parser.getInt());
            parser.next();
            parser.next();
            auction.setBuyout(parser.getInt());
            parser.next();
            parser.next();
            auction.setQuantity(parser.getInt());
            parser.next();
            parser.next();
            auction.setTimeLeft(parser.getString());
            parser.next();
            parser.next();
            auction.setRand(parser.getInt());
            parser.next();
            parser.next();
            auction.setSeed(parser.getLong());
            return true;
        }
        return false;
    }

    public void setParser(JsonParser parser) {
        this.parser = parser;
    }
}
