package com.packt.footballobs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.context.ApplicationEventPublisher;

class TradingServiceTests {

    @Test
    void tradeCardsPublishesBrokenEventWhenPendingOrdersAreHigh() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        TradingService tradingService = spy(new TradingService(publisher));
        doReturn(95).when(tradingService).getPendingOrders();

        int result = tradingService.tradeCards(10);

        assertThat(result).isEqualTo(10);
        verify(publisher).publishEvent(any(AvailabilityChangeEvent.class));
    }

    @Test
    void tradeCardsPublishesCorrectEventWhenPendingOrdersAreLow() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        TradingService tradingService = spy(new TradingService(publisher));
        doReturn(20).when(tradingService).getPendingOrders();

        int result = tradingService.tradeCards(5);

        assertThat(result).isEqualTo(5);
        verify(publisher).publishEvent(any(AvailabilityChangeEvent.class));
    }
}
