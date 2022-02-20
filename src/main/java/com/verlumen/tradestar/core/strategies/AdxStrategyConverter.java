package com.verlumen.tradestar.core.strategies;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.verlumen.tradestar.protos.strategies.ADXParams;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

class AdxStrategyConverter implements StrategyConverter {
    private static final ImmutableMap<SignalStrength, Range<Integer>>
            ADX_VALUES_BY_SIGNAl_STRENGTH = ImmutableMap.of(
            SignalStrength.WEAK, Range.closedOpen(0, 25),
            SignalStrength.STRONG, Range.closedOpen(25, 50),
            SignalStrength.VERY_STRONG, Range.closedOpen(50, 75),
            SignalStrength.EXTREMELY_STRONG, Range.closed(75, 100));

    private static Optional<Range<Integer>> getAdxSignalStrengthRange(SignalStrength signalStrength) {
        return Optional.ofNullable(signalStrength)
                .filter(ADX_VALUES_BY_SIGNAl_STRENGTH::containsKey)
                .map(ADX_VALUES_BY_SIGNAl_STRENGTH::get);
    }

    @Override
    public Strategy convert(TradeStrategy params, BarSeries barSeries) {
        return create(barSeries, params.getAdxParams());
    }

    @Override
    public TradeStrategy.StrategyOneOfCase supportedCase() {
        return TradeStrategy.StrategyOneOfCase.ADX_PARAMS;
    }

    private Strategy create(BarSeries barSeries, ADXParams adxParams) {
        checkArgument(adxParams.hasBarCount());
        checkArgument(adxParams.hasBuySignalStrength());
        checkArgument(adxParams.hasSellSignalStrength());
        int barCount = adxParams.getBarCount();
        SignalStrength buyStrength = adxParams.getBuySignalStrength();
        SignalStrength sellStrength = adxParams.getSellSignalStrength();

        return getAdxSignalStrengthRange(buyStrength)
                .flatMap(buyRange -> getAdxSignalStrengthRange(sellStrength)
                        .map(sellRange -> create(barSeries,
                                barCount, buyRange, sellRange)))
                .orElseThrow(IllegalStateException::new);
    }

    private Strategy create(BarSeries barSeries,
                            int barCount,
                            Range<Integer> buyRange,
                            Range<Integer> sellRange) {
        int buyThreshold = buyRange.lowerEndpoint();
        int sellThreshold = sellRange.upperEndpoint();
        ADXIndicator indicator = new ADXIndicator(barSeries, barCount);
        Rule entryRule = new CrossedUpIndicatorRule(indicator, buyThreshold);
        Rule exitRule = new CrossedDownIndicatorRule(indicator, sellThreshold);
        String name = String.format("ADX-%d-%d-%d",
                barCount, buyThreshold, sellThreshold);
        return new BaseStrategy(name, entryRule, exitRule);
    }
}