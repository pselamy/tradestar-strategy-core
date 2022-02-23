package com.verlumen.tradestar.core.strategies;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.function.Function.identity;

class StrategyFactory {
    private final ImmutableMap<StrategyOneOfCase, StrategyConverter> converterMap;

    @Inject
    StrategyFactory(Set<StrategyConverter> converters) {
        this.converterMap = converters
                .stream()
                .collect(toImmutableMap(StrategyConverter::supportedCase,
                        identity()));
    }

    public Strategy create(BarSeries barSeries, TradeStrategy params) {
        StrategyOneOfCase strategyOneOfCase =
                firstNonNull(params.getStrategyOneOfCase(), StrategyOneOfCase.STRATEGYONEOF_NOT_SET);
        return Optional
                .ofNullable(converterMap.get(strategyOneOfCase))
                .map(converter -> converter.convert(params, barSeries))
                .orElseThrow(UnsupportedOperationException::new);
    }
}
