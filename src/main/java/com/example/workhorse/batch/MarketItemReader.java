package com.example.workhorse.batch;

import com.example.workhorse.data.entity.Market;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@StepScope
public class MarketItemReader implements ItemReader<Market> {

    private final JdbcTemplate jdbcTemplate;
    private final int CHUNK_SIZE = 1000;
    private List<Market> currentBatch = Collections.emptyList();
    private int currentIndex = 0;
    private int currentOffset = 0;

    public MarketItemReader(@Qualifier("sourceDataSource") DataSource sourceDataSource) {
        this.jdbcTemplate = new JdbcTemplate(sourceDataSource);
    }

    @Override
    public Market read() {
        if (currentIndex >= currentBatch.size()) {
            currentBatch = jdbcTemplate.query(
                    "SELECT market_id, address, city, country, brand FROM market ORDER BY id LIMIT ? OFFSET ?",
                    (rs, rowNum) -> Market.builder()
                            .marketId(UUID.fromString(rs.getString("market_id")))
                            .address(rs.getString("address"))
                            .city(rs.getString("city"))
                            .country(rs.getString("country"))
                            .brand(rs.getString("brand"))
                            .build(),
                    CHUNK_SIZE, currentOffset
            );

            log.info("Fetched new chunk of size: {}", currentBatch.size());

            currentOffset += CHUNK_SIZE;
            currentIndex = 0;

            if (currentBatch.isEmpty()) {
                return null;
            }
        }

        return currentBatch.get(currentIndex++);
    }
}