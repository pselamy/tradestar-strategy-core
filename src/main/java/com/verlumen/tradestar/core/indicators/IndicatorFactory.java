package com.verlumen.tradestar.core.indicators;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public interface IndicatorFactory {
    Indicator<Num> create(TradeStrategy tradeStrategy, BarSeries barSeries);

    interface Adapter {
        Indicator<Num> indicator(TradeStrategy strategy, BarSeries series);

        TradeStrategy.StrategyOneOfCase supportedCase();
    }
}
