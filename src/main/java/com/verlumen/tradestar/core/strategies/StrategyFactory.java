package com.verlumen.tradestar.core.strategies;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;


public interface StrategyFactory {
    static Strategy create(String name, Rule entryRule, Rule exitRule) {
        return new BaseStrategy(name, entryRule, exitRule);
    }

    Strategy create(BarSeries barSeries, TradeStrategy params);
}
