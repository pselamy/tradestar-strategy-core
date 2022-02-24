package com.verlumen.tradestar.core.strategies;

import com.verlumen.tradestar.protos.strategies.TradeStrategy;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;

enum TradeStrategyIdentifier {
    ADX(params -> String.format("ADX-%d-BUY-%s-SELL-%s",
            params.getAdx().getBarCount(),
            params.getAdx().getBuySignalStrength(),
            params.getAdx().getSellSignalStrength()),
            params -> params.getAdx().getBarCount() > 0 &&
                    params.getAdx().hasBuySignalStrength() &&
                    params.getAdx().hasSellSignalStrength());

    private final Function<TradeStrategy, String> nameFunction;
    private final Predicate<TradeStrategy> hasRequiredAttributes;

    TradeStrategyIdentifier(Function<TradeStrategy, String> nameFunction,
                            Predicate<TradeStrategy> hasRequiredAttributes) {
        this.nameFunction = nameFunction;
        this.hasRequiredAttributes = hasRequiredAttributes;
    }

    static TradeStrategyIdentifier get(
            TradeStrategy.StrategyOneOfCase supportedCase) {
        return TradeStrategyIdentifier.valueOf(supportedCase.name());
    }

    String name(TradeStrategy tradeStrategy) {
        checkArgument(hasRequiredAttributes.test(tradeStrategy));
        return nameFunction.apply(tradeStrategy);
    }

    TradeStrategy.StrategyOneOfCase supportedCase() {
        return TradeStrategy.StrategyOneOfCase.valueOf(name());
    }
}
