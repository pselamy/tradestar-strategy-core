package com.verlumen.tradestar.core.indicators;

import com.google.inject.Provider;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.num.Num;

import java.util.function.BiFunction;

enum IndicatorFactoryProvider implements Provider<IndicatorFactory> {
    ADX(ADXIndicator::new);

    private final BiFunction<BarSeries, Integer, Indicator<Num>> indicatorFunction;

    IndicatorFactoryProvider(BiFunction<BarSeries, Integer, Indicator<Num>> indicatorFunction) {
        this.indicatorFunction = indicatorFunction;
    }

    @Override
    public IndicatorFactory get() {
        TradeStrategy.StrategyOneOfCase supportedCase =
                TradeStrategy.StrategyOneOfCase.valueOf(name());
        return IndicatorFactory.create(supportedCase, indicatorFunction);
    }
}
