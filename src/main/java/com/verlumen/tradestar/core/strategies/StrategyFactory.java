package com.verlumen.tradestar.core.strategies;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

public interface StrategyFactory {
    Strategy create(TradeStrategy params, BarSeries barSeries);
}
