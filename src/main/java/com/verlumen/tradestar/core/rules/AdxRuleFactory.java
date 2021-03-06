package com.verlumen.tradestar.core.rules;

import com.google.inject.Inject;
import com.verlumen.tradestar.core.indicators.IndicatorFactory;
import com.verlumen.tradestar.core.signalstrength.SignalStrengthSpec;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.ADX;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.util.Set;

import static com.verlumen.tradestar.core.strategies.StrategyOneOfCases.getHandlerForSupportedCase;

class AdxRuleFactory implements RuleFactory {
    private final IndicatorFactory indicatorFactory;
    private final SignalStrengthSpec signalStrengthSpec;

    @Inject
    AdxRuleFactory(IndicatorFactory indicatorFactory,
                   Set<SignalStrengthSpec> signalStrengthSpecs) {
        TradeStrategy.StrategyOneOfCase supportedCase = TradeStrategy.StrategyOneOfCase.ADX;
        this.indicatorFactory = indicatorFactory;
        this.signalStrengthSpec = getHandlerForSupportedCase(
                signalStrengthSpecs, SignalStrengthSpec::supportedCase, supportedCase);
    }

    @Override
    public CrossedUpIndicatorRule buyRule(TradeStrategy params,
                                          BarSeries barSeries) {
        ADX adx = params.getAdx();
        Indicator<Num> indicator = indicatorFactory.create(params, barSeries);
        Num threshold = signalStrengthSpec
                .range(adx.getBuySignalStrength()).lowerEndpoint();
        return new CrossedUpIndicatorRule(indicator, threshold);
    }

    @Override
    public CrossedDownIndicatorRule sellRule(TradeStrategy params,
                                             BarSeries barSeries) {
        ADX adx = params.getAdx();
        Indicator<Num> indicator = indicatorFactory.create(params, barSeries);
        Num threshold = signalStrengthSpec
                .range(adx.getSellSignalStrength()).upperEndpoint();
        return new CrossedDownIndicatorRule(indicator, threshold);
    }

    @Override
    public TradeStrategy.StrategyOneOfCase supportedCase() {
        return TradeStrategy.StrategyOneOfCase.ADX;
    }
}