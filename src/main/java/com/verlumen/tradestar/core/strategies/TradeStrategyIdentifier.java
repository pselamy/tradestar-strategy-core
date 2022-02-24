package com.verlumen.tradestar.core.strategies;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;

enum TradeStrategyIdentifier {
    ADX(params -> String.format("ADX-%d-%s-%s",
            params.getAdx().getBarCount(),
            params.getAdx().getBuySignalStrength(),
            params.getAdx().getSellSignalStrength()),
            params -> params.getAdx().getBarCount() > 0 &&
                    params.getAdx().hasBuySignalStrength() &&
                    params.getAdx().hasSellSignalStrength());

    private final Function<TradeStrategy, String> nameFunction;
    private final Predicate<TradeStrategy> isValidTradeStrategy;

    TradeStrategyIdentifier(Function<TradeStrategy, String> nameFunction, Predicate<TradeStrategy> isValidTradeStrategy) {
        this.nameFunction = nameFunction;
        this.isValidTradeStrategy = isValidTradeStrategy;
    }

    String name(TradeStrategy tradeStrategy) {
        checkArgument(isValidTradeStrategy.test(tradeStrategy));
        return nameFunction.apply(tradeStrategy);
    }

    TradeStrategy.StrategyOneOfCase supportedCase() {
        return TradeStrategy.StrategyOneOfCase.valueOf(name());
    }
}
