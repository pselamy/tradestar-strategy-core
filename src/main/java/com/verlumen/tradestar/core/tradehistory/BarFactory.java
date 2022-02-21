package com.verlumen.tradestar.core.tradehistory;

import com.google.common.collect.ImmutableMap;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.num.DecimalNum;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

public class BarFactory {
    private static final ImmutableMap<Granularity, Long> SECONDS_BY_GRANULARITY =
            ImmutableMap.<Granularity, Long>builder()
                    .put(Granularity.ONE_MINUTE, 60L)
                    .put(Granularity.FIVE_MINUTES, 300L)
                    .put(Granularity.FIFTEEN_MINUTES, 900L)
                    .put(Granularity.ONE_HOUR, 3600L)
                    .put(Granularity.SIX_HOURS, 21600L)
                    .put(Granularity.ONE_DAY, 86400L)
                    .build();

    static Bar create(Candle candle) {
        Duration timePeriod = toDuration(candle.getGranularity());
        Instant start = Instant.ofEpochSecond((candle.getStart()).getSeconds());
        ZonedDateTime end = start.plus(timePeriod).atZone(ZoneOffset.UTC);
        return BaseBar.builder()
                .timePeriod(timePeriod)
                .endTime(end)
                .openPrice(DecimalNum.valueOf(candle.getOpen()))
                .highPrice(DecimalNum.valueOf(candle.getHigh()))
                .lowPrice(DecimalNum.valueOf(candle.getLow()))
                .closePrice(DecimalNum.valueOf(candle.getClose()))
                .volume(DecimalNum.valueOf(candle.getVolume()))
                .build();
    }

    private static Duration toDuration(Granularity granularity) {
        return Optional.ofNullable(SECONDS_BY_GRANULARITY.get(granularity))
                .map(Duration::ofSeconds)
                .orElseThrow(IllegalArgumentException::new);
    }
}
