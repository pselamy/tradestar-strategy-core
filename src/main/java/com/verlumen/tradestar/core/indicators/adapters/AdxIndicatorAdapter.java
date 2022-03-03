package com.verlumen.tradestar.core.indicators.adapters;

import com.verlumen.tradestar.core.indicators.IndicatorFactory;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.num.Num;

import static com.google.common.base.Preconditions.checkArgument;

class AdxIndicatorAdapter implements IndicatorFactory.Adapter {
    @Override
    public Indicator<Num> indicator(TradeStrategy strategy, BarSeries series) {
        int barCount = strategy.getAdx().getBarCount();
        checkArgument(barCount > 0);
        checkArgument(barCount < series.getBarCount());
        return new ADXIndicator(series, barCount);
    }

    @Override
    public StrategyOneOfCase supportedCase() {
        return StrategyOneOfCase.ADX;
    }
}
