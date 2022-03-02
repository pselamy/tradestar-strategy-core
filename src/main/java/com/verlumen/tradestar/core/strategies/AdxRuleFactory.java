package com.verlumen.tradestar.core.strategies;

import com.google.inject.Inject;
import com.verlumen.tradestar.core.signalstrength.SignalStrengthSpec;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.ADX;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.verlumen.tradestar.core.strategies.StrategyOneOfCases.getHandlerForSupportedCase;

class AdxRuleFactory implements RuleFactory {
    private final IndicatorFactory indicatorFactory;
    private final SignalStrengthSpec signalStrengthSpec;

    @Inject
    AdxRuleFactory(Set<IndicatorFactory> indicatorFactories,
                   Set<SignalStrengthSpec> signalStrengthSpecs) {
        TradeStrategy.StrategyOneOfCase supportedCase = TradeStrategy.StrategyOneOfCase.ADX;
        this.indicatorFactory = getHandlerForSupportedCase(
                indicatorFactories, IndicatorFactory::supportedCase, supportedCase);
        this.signalStrengthSpec = getHandlerForSupportedCase(
                signalStrengthSpecs, SignalStrengthSpec::supportedCase, supportedCase);
    }

    @Override
    public CrossedUpIndicatorRule buyRule(TradeStrategy params,
                                          BarSeries barSeries) {
        ADX adx = params.getAdx();
        checkArgument(adx.getBarCount() > 0);
        checkArgument(adx.getBarCount() < barSeries.getBarCount());
        Indicator<Num> indicator = indicatorFactory.create(barSeries,
                adx.getBarCount());
        Num threshold = signalStrengthSpec
                .range(adx.getBuySignalStrength()).lowerEndpoint();
        return new CrossedUpIndicatorRule(indicator, threshold);
    }

    @Override
    public CrossedDownIndicatorRule sellRule(TradeStrategy params,
                                             BarSeries barSeries) {
        ADX adx = params.getAdx();
        checkArgument(adx.getBarCount() < barSeries.getBarCount());
        int barCount = adx.getBarCount();
        checkArgument(barCount > 0);
        Indicator<Num> indicator = indicatorFactory.create(barSeries, barCount);
        Num threshold = signalStrengthSpec
                .range(adx.getSellSignalStrength()).upperEndpoint();
        return new CrossedDownIndicatorRule(indicator, threshold);
    }

    @Override
    public TradeStrategy.StrategyOneOfCase supportedCase() {
        return TradeStrategy.StrategyOneOfCase.ADX;
    }
}