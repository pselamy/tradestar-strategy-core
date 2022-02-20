package com.verlumen.tradestar.backtesting;

import com.verlumen.tradestar.protos.candles.Granularity;
import com.verlumen.tradestar.protos.exchanges.ExchangeMetadata;
import org.ta4j.core.BarSeriesManager;

import java.time.Instant;

interface BarSeriesManagerFactory {
    BarSeriesManager create(ExchangeMetadata exchangeMetadata,
                            Granularity granularity, Instant start, Instant end);
}
