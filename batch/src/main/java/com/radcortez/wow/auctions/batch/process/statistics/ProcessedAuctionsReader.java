package com.radcortez.wow.auctions.batch.process.statistics;

import com.radcortez.wow.auctions.batch.process.AbstractAuctionFileProcess;
import lombok.extern.java.Log;
import org.apache.commons.dbutils.DbUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.batch.api.chunk.ItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * @author Ivan St. Ivanov
 */
@Dependent
@Named
@Log
public class ProcessedAuctionsReader extends AbstractAuctionFileProcess implements ItemReader {
    @Inject
    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String url;
    @Inject
    @ConfigProperty(name = "quarkus.datasource.username")
    Optional<String> username;
    @Inject
    @ConfigProperty(name = "quarkus.datasource.password")
    Optional<String> password;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        log.info("Processing Auctions Statistics for Realm " + getContext().getConnectedRealm().getId());
        // This is a workaround because Agroal doesn't honor ResultSet.HOLD_CURSORS_OVER_COMMIT. See https://github.com/quarkusio/quarkus/issues/6770
        connection = DriverManager.getConnection(url, username.orElse(null), password.orElse(null));

        preparedStatement = this.connection.prepareStatement(
            "SELECT" +
            "   itemid as itemId," +
            "   sum(quantity)," +
            "   sum(bid)," +
            "   sum(buyout)," +
            "   min(bid / quantity)," +
            "   min(buyout / quantity)," +
            "   max(bid / quantity)," +
            "   max(buyout / quantity)" +
            " FROM auction" +
            " WHERE auctionfile_id = " + getContext().getAuctionFile().getId() +
            " GROUP BY itemid" +
            " ORDER BY 1",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);

        resultSet = preparedStatement.executeQuery();
    }

    @Override
    public void close() {
        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(preparedStatement);
        DbUtils.closeQuietly(connection);
        log.info("Finished Auctions Statistics for Realm " + getContext().getConnectedRealm().getId());
    }

    @Override
    public Object readItem() throws Exception {
        return resultSet.next() ? resultSet : null;
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }
}
