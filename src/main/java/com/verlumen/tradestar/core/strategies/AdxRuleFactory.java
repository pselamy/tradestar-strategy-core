package com.verlumen.tradestar.core.strategies;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.ADX;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

class AdxRuleFactory implements RuleFactory {
    private final IndicatorFactory indicatorFactory;

    @Inject
    AdxRuleFactory(Set<IndicatorFactory> indicatorFactories) {
        TradeStrategy.StrategyOneOfCase supportedCase = TradeStrategy.StrategyOneOfCase.ADX;
        this.indicatorFactory = StrategyOneOfCases.forSupportedCase(
                indicatorFactories, IndicatorFactory::supportedCase, supportedCase);
    }

    @Override
    public CrossedUpIndicatorRule buyRule(TradeStrategy params, BarSeries barSeries) {
        checkArgument(params.getAdx().getBarCount() > 0);
        SignalStrengthSpec signalStrengthSpec =
                SignalStrengthSpec.get(params.getAdx().getBuySignalStrength());
        Indicator<Num> indicator = indicatorFactory.create(barSeries,
                params.getAdx().getBarCount());
        return new CrossedUpIndicatorRule(indicator,
                signalStrengthSpec.range.lowerEndpoint());
    }

    @Override
    public CrossedDownIndicatorRule sellRule(TradeStrategy params,
                                             BarSeries barSeries) {
        ADX adxParams = params.getAdx();
        int barCount = adxParams.getBarCount();
        checkArgument(barCount > 0);
        Indicator<Num> indicator = indicatorFactory.create(barSeries, barCount);
        SignalStrengthSpec signalStrengthSpec =
                SignalStrengthSpec.get(adxParams.getSellSignalStrength());
        return new CrossedDownIndicatorRule(indicator,
                signalStrengthSpec.range.upperEndpoint());
    }

    @Override
    public TradeStrategy.StrategyOneOfCase supportedCase() {
        return TradeStrategy.StrategyOneOfCase.ADX;
    }

    enum SignalStrengthSpec {
        WEAK(0, 25),
        STRONG(25, 50),
        VERY_STRONG(50, 75),
        EXTREMELY_STRONG(75, 100);

        private final Range<Integer> range;

        SignalStrengthSpec(int lower, int upper) {
            this.range = Range.closedOpen(lower, upper);
        }

        static SignalStrengthSpec get(SignalStrength signalStrength) {
            return SignalStrengthSpec.valueOf(signalStrength.name());
        }
    }
}