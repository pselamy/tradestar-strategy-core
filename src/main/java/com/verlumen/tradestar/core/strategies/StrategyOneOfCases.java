package com.verlumen.tradestar.core.strategies;

import com.google.common.collect.ImmutableMap;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

public class StrategyOneOfCases {
    static <T> ImmutableMap<TradeStrategy.StrategyOneOfCase, T> keyBySupportedCase(
            Set<T> set, Function<T, TradeStrategy.StrategyOneOfCase> keyFunction) {
        return set.stream().collect(toImmutableMap(keyFunction, identity()));
    }

    static  <T> T forSupportedCase(Set<T> set, Function<T,
            TradeStrategy.StrategyOneOfCase> keyFunction, TradeStrategy.StrategyOneOfCase supportedCase) {
        return Optional.ofNullable(keyBySupportedCase(set, keyFunction)
                        .get(supportedCase))
                .orElseThrow(IllegalStateException::new);
    }
}
