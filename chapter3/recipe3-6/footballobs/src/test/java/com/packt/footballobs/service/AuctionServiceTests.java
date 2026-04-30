package com.packt.footballobs.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

class AuctionServiceTests {

    @Test
    void addBidRecordsMetricsAndClearsPendingBids() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        AuctionService auctionService = new AuctionService(meterRegistry);

        auctionService.addBid("Aitana Bonmatí", "100");

        assertThat(meterRegistry.get("football.bids.received").counter().count()).isEqualTo(1.0);
        assertThat(meterRegistry.get("football.bids.duration").timer().count()).isEqualTo(1);
        assertThat(meterRegistry.get("football.bids.pending").gauge().value()).isEqualTo(0.0);
    }
}
