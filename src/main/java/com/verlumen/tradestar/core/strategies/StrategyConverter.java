package com.verlumen.tradestar.core.strategies;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

interface StrategyConverter {
    Strategy convert(TradeStrategy tradeStrategy, BarSeries barSeries);

    TradeStrategy.StrategyOneOfCase supportedCase();
}
