package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.FolderType;

import javax.batch.api.chunk.ItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.Serializable;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FileUtils.openInputStream;

/**
 * @author Roberto Cortez
 */
@Named
public class AuctionDataItemReader extends AbstractAuctionFileProcess implements ItemReader {
    private JsonParser parser;

    @Inject
    private JobContext jobContext;
    @Inject
    private WoWBusiness woWBusiness;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        getLogger(this.getClass().getName()).log(Level.INFO, "Processing file " +
                                                             getContext().getFileToProcess().getFileName() +
                                                             " for Realm " +
                                                             getContext().getRealm().getRealmDetail());

        // todo - Configure folderType
        setParser(Json.createParser(openInputStream(getContext().getFileToProcess(FolderType.FI_TMP))));

        AuctionFile fileToProcess = getContext().getFileToProcess();
        fileToProcess.setFileStatus(FileStatus.PROCESSING);
        woWBusiness.updateAuctionFile(fileToProcess);
    }

    @Override
    public void close() throws Exception {
        AuctionFile fileToProcess = getContext().getFileToProcess();
        fileToProcess.setFileStatus(FileStatus.PROCESSED);
        woWBusiness.updateAuctionFile(fileToProcess);

        getLogger(this.getClass().getName()).log(Level.INFO, "Finished file " +
                                                             getContext().getFileToProcess().getFileName() +
                                                             " for Realm " +
                                                             getContext().getRealm().getRealmDetail());
    }

    @Override
    public Object readItem() throws Exception {
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            Auction auction = new Auction();
            switch (event) {
                case KEY_NAME:
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

    protected boolean readAuctionItem(Auction auction) {
        if (parser.getString().equalsIgnoreCase("auc")) {
            parser.next();
            auction.setAuctionId(parser.getLong());
            parser.next();
            parser.next();
            auction.setItemId(parser.getInt());
            parser.next();
            parser.next();
            parser.next();
            parser.next();
            auction.setOwnerRealm(parser.getString());
            parser.next();
            parser.next();
            auction.setBid(parser.getLong());
            parser.next();
            parser.next();
            auction.setBuyout(parser.getLong());
            parser.next();
            parser.next();
            auction.setQuantity(parser.getInt());
            return true;
        }
        return false;
    }

    public void setParser(JsonParser parser) {
        this.parser = parser;
    }
}
