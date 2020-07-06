package com.radcortez.wow.auctions.batch.process.data;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import com.radcortez.wow.auctions.business.WoWBusiness;
import com.radcortez.wow.auctions.business.WoWBusinessBean;
import com.radcortez.wow.auctions.entity.Auction;
import com.radcortez.wow.auctions.entity.AuctionFile;
import com.radcortez.wow.auctions.entity.FileStatus;
import com.radcortez.wow.auctions.entity.FolderType;

import javax.batch.api.chunk.ItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;
import static org.apache.commons.io.FileUtils.openInputStream;

/**
 * @author Roberto Cortez
 */
@Dependent
@Named
public class AuctionDataItemReader extends AbstractAuctionFileProcess implements ItemReader {
    private JsonParser parser;
    private FileInputStream in;

    @Inject
    WoWBusinessBean woWBusiness;

    public AuctionDataItemReader() {
        in = null;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        getLogger(this.getClass().getName()).log(Level.INFO, "Processing file " +
                                                             getContext().getFileToProcess().getFileName() +
                                                             " for Realm " +
                                                             getContext().getRealm().getRealmDetail());

        // todo - Configure folderType
        in = openInputStream(getContext().getFileToProcess(FolderType.FI_TMP));
        setParser(Json.createParser(in));

        AuctionFile fileToProcess = getContext().getFileToProcess();
        fileToProcess.setFileStatus(FileStatus.PROCESSING);
        woWBusiness.updateAuctionFile(fileToProcess);
    }

    @Override
    public void close() throws Exception {
        AuctionFile fileToProcess = getContext().getFileToProcess();
        fileToProcess.setFileStatus(FileStatus.PROCESSED);
        woWBusiness.updateAuctionFile(fileToProcess);

        if (in != null) {
            in.close();
        }

        getLogger(this.getClass().getName()).log(Level.INFO, "Finished file " +
                                                             getContext().getFileToProcess().getFileName() +
                                                             " for Realm " +
                                                             getContext().getRealm().getRealmDetail());
    }

    @Override
    public Object readItem() {
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            Auction auction = new Auction();
            if (event == JsonParser.Event.KEY_NAME) {
                if (readAuctionItem(auction)) {
                    return auction;
                }
            }
        }
        return null;
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }

    protected boolean readAuctionItem(Auction auction) {
        if (parser.getString().equalsIgnoreCase("auc")) {
            parser.next();
            auction.setId(parser.getString());
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
