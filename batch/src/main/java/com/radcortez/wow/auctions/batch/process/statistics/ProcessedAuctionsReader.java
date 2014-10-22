package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import org.apache.commons.dbutils.DbUtils;

import javax.annotation.Resource;
import javax.batch.api.chunk.ItemReader;
import javax.inject.Named;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Ivan St. Ivanov
 */
@Named
public class ProcessedAuctionsReader extends AbstractAuctionFileProcess implements ItemReader {
    @Resource(name = "java:comp/DefaultDataSource")
    protected DataSource dataSource;

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        Connection connection = dataSource.getConnection();

        preparedStatement = connection.prepareStatement("SELECT" +
                                                        "   itemid as itemId," +
                                                        "   sum(quantity)," +
                                                        "   sum(bid)," +
                                                        "   sum(buyout)," +
                                                        "   min(bid / quantity)," +
                                                        "   min(buyout / quantity)," +
                                                        "   max(bid / quantity)," +
                                                        "   max(buyout / quantity)" +
                                                        " FROM auction" +
                                                        " WHERE auctionfile_id = " +
                                                        getContext().getFileToProcess().getId() +
                                                        " GROUP BY itemid" +
                                                        " ORDER BY 1",
                                                        ResultSet.TYPE_FORWARD_ONLY,
                                                        ResultSet.CONCUR_READ_ONLY,
                                                        ResultSet.HOLD_CURSORS_OVER_COMMIT
                                                       );

        // Weird bug with Postgre here.
        //preparedStatement.setLong(1, getContext().getFileToProcess().getId());

        resultSet = preparedStatement.executeQuery();
    }

    @Override
    public void close() throws Exception {
        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(preparedStatement);
    }

    @Override
    public Object readItem() throws Exception {
        return resultSet.next() ? resultSet : null;
    }

    @Override public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
