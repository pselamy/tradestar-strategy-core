package com.verlumen.tradestar.core.tradehistory;

import com.google.common.collect.ImmutableList;
import com.verlumen.tradestar.protos.candles.Candle;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class BarSeriesFactory {
    public static BarSeries create(ImmutableList<Candle> candles) {
        ImmutableList<Bar> bars = candles.stream()
                .map(BarFactory::create)
                .collect(toImmutableList());
        return new BaseBarSeriesBuilder()
                .withBars(bars)
                .build();
    }
}
