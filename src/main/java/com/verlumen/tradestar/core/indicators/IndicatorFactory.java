package com.verlumen.tradestar.core.indicators;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

import java.util.function.BiFunction;

public interface IndicatorFactory {
    static IndicatorFactory create(
            TradeStrategy.StrategyOneOfCase supportedCase,
            BiFunction<BarSeries, Integer, Indicator<Num>> indicatorFunction) {
        return new IndicatorFactory() {
            @Override
            public Indicator<Num> create(BarSeries barSeries, int barCount) {
                return indicatorFunction.apply(barSeries, barCount);
            }

            @Override
            public TradeStrategy.StrategyOneOfCase supportedCase() {
                return supportedCase;
            }
        };
    }

    Indicator<Num> create(BarSeries barSeries, int barCount);

    TradeStrategy.StrategyOneOfCase supportedCase();

    interface Adapter {}
}
