package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.FolderType;
import lombok.extern.java.Log;

import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.transaction.Transactional;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Optional;

import static org.apache.commons.io.FileUtils.openInputStream;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
@Log
@Transactional
public class AuctionDataItemReader extends AbstractAuctionFileProcess implements ItemReader {
    private JsonParser parser;
    private FileInputStream in;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        log.info("Processing file " + getContext().getAuctionFile().getFileName() + " for Realm " + getContext().getConnectedRealm().getId());

        // todo - Configure folderType
        in = openInputStream(getContext().getAuctionFile(FolderType.FI_TMP));
        setParser(Json.createParser(in));

        AuctionFile fileToProcess = getContext().getAuctionFile();
        fileToProcess.setFileStatus(FileStatus.PROCESSING);
    }

    @Override
    public void close() throws Exception {
        AuctionFile fileToProcess = getContext().getAuctionFile();
        fileToProcess.setFileStatus(FileStatus.PROCESSED);

        if (in != null) {
            in.close();
        }

        log.info("Finished file " + getContext().getAuctionFile().getFileName() + " for Realm " + getContext().getConnectedRealm().getId());
    }

    @Override
    public Object readItem() {
        while (parser.hasNext()) {
            Auction auction = new Auction();
            if (readAuctionItem(auction)) {
                return auction;
            }
        }
        return null;
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }

    protected boolean readAuctionItem(Auction auction) {
        JsonParser.Event event = parser.next();
        if (event == JsonParser.Event.START_OBJECT) {
            JsonObject object = parser.getObject();
            if (object.getJsonObject("item") != null) {
                auction.setId(object.getJsonNumber("id").longValue());
                auction.setItemId(object.getJsonObject("item").getInt("id"));
                auction.setBid(Optional.ofNullable(object.getJsonNumber("bid")).map(JsonNumber::longValue).orElse(0L));
                auction.setBuyout(Optional.ofNullable(object.getJsonNumber("buyout")).map(JsonNumber::longValue).orElse(0L));
                auction.setQuantity(object.getInt("quantity"));
                return true;
            }
        }
        return false;
    }

    public void setParser(JsonParser parser) {
        this.parser = parser;
        searchAuctions();
    }

    private void searchAuctions() {
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.KEY_NAME && parser.getString().equalsIgnoreCase("auctions")) {
                parser.next();
                break;
            }
        }
    }
}
