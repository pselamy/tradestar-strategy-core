package com.verlumen.tradestar.core.strategies;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.rules.RuleFactory;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.StrategyOneOfCase;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.verlumen.tradestar.core.strategies.StrategyOneOfCases.keyBySupportedCase;

class StrategyFactoryImpl implements StrategyFactory {
    private static final ImmutableSet<StrategyOneOfCase>
            SUPPORTED_CASES = ImmutableSet.copyOf(EnumSet.complementOf(
            EnumSet.of(StrategyOneOfCase.STRATEGYONEOF_NOT_SET,
                    StrategyOneOfCase.COMPOSITE)));

    private final ImmutableMap<StrategyOneOfCase, RuleFactory> ruleFactories;

    @Inject
    StrategyFactoryImpl(Set<RuleFactory> ruleFactories) {
        this.ruleFactories = keyBySupportedCase(ruleFactories,
                RuleFactory::supportedCase);
    }

    private static Strategy create(String name, Rule entryRule, Rule exitRule) {
        return new BaseStrategy(name, entryRule, exitRule);
    }

    @Override
    public Strategy create(TradeStrategy params, BarSeries barSeries) {
        checkArgument(SUPPORTED_CASES.contains(params.getStrategyOneOfCase()));
        String name = getStrategyName(params);
        RuleFactory ruleFactory = ruleFactory(params.getStrategyOneOfCase());
        Rule entryRule = ruleFactory.buyRule(params, barSeries);
        Rule exitRule = ruleFactory.sellRule(params, barSeries);
        return create(name, entryRule, exitRule);
    }

    private String getStrategyName(TradeStrategy params) {
        return TradeStrategyIdentifier.get(params.getStrategyOneOfCase()).name(params);
    }

    private RuleFactory ruleFactory(StrategyOneOfCase strategyOneOfCase) {
        return Optional.ofNullable(ruleFactories.get(strategyOneOfCase))
                .orElseThrow(IllegalArgumentException::new);
    }
}