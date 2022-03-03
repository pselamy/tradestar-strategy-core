package com.verlumen.tradestar.core.rules;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.verlumen.tradestar.core.indicators.IndicatorFactory;
import com.verlumen.tradestar.core.signalstrength.SignalStrengthModule;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.ta4j.core.*;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static java.lang.Math.random;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdxRuleFactoryTest {
    private static final ImmutableMap<SignalStrength, Range<Integer>>
            ADX_VALUES_BY_SIGNAl_STRENGTH = ImmutableMap.of(
            SignalStrength.WEAK, Range.closedOpen(0, 25),
            SignalStrength.STRONG, Range.closedOpen(25, 50),
            SignalStrength.VERY_STRONG, Range.closedOpen(50, 75),
            SignalStrength.EXTREMELY_STRONG, Range.closed(75, 100));

    private static final int BAR_COUNT = 1;
    private static final ImmutableList<Bar> BARS = ImmutableList.of(
            newBar(), newBar());
    private static final BarSeries BAR_SERIES =
            new BaseBarSeriesBuilder().withBars(BARS).build();
    private static final ADXIndicator INDICATOR = new ADXIndicator(BAR_SERIES,
            BAR_COUNT);
    private static final TradeStrategy.ADX ADX_TRADE_STRATEGY =
            TradeStrategy.ADX.newBuilder()
                    .setBarCount(BAR_COUNT)
                    .setBuySignalStrength(SignalStrength.VERY_STRONG)
                    .setSellSignalStrength(SignalStrength.WEAK)
                    .build();
    private static final TradeStrategy.ADX TRADE_STRATEGY_WITH_ZERO_BAR_COUNT = ADX_TRADE_STRATEGY.toBuilder()
            .clearBarCount()
            .build();
    private static final TradeStrategy.ADX TRADE_STRATEGY_WITH_NEGATIVE_BAR_COUNT = ADX_TRADE_STRATEGY.toBuilder()
            .setBarCount(-1)
            .build();
    private static final TradeStrategy.ADX TRADE_STRATEGY_WITH_EXCESSIVE_BAR_COUNT =
            ADX_TRADE_STRATEGY.toBuilder()
                    .setBarCount(2)
                    .build();
    @Inject
    private AdxRuleFactory factory;

    private static BaseBar newBar() {
        return BaseBar.builder()
                .timePeriod(Duration.ZERO)
                .endTime(ZonedDateTime.now())
                .closePrice(DecimalNum.valueOf(random()))
                .build();
    }

    private static Stream<Arguments> signalStrengths() {
        SignalStrength[] values = SignalStrength.values();
        return stream(values)
                .flatMap(signalStrength1 -> stream(values)
                        .map(signalStrength2 ->
                                Arguments.of(signalStrength1, signalStrength2)));
    }

    @BeforeEach
    void setup() {
        Guice.createInjector(
                        new SignalStrengthModule(),
                        new TestModule())
                .injectMembers(this);
    }

    @ParameterizedTest
    @EnumSource(BuyRuleWithMissingArgumentsTestCase.class)
    void buyRule_withMissingArguments_throwsIllegalArgumentException(
            BuyRuleWithMissingArgumentsTestCase testCase) {
        assertThrows(IllegalArgumentException.class,
                () -> factory.buyRule(testCase.tradeStrategy, BAR_SERIES));
    }

    @ParameterizedTest
    @MethodSource("signalStrengths")
    void buyRule_withValidSignalStrength_returnsEntryRule(
            SignalStrength signalStrength) {
        assume().that(signalStrength).isNotEqualTo(SignalStrength.UNSPECIFIED);
        int buyThreshold = getSignalStrengthRange(signalStrength).lowerEndpoint();
        CrossedUpIndicatorRule expected = new CrossedUpIndicatorRule(INDICATOR, buyThreshold);
        TradeStrategy.ADX adx = ADX_TRADE_STRATEGY.toBuilder()
                .setBuySignalStrength(signalStrength)
                .build();
        TradeStrategy tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();

        CrossedUpIndicatorRule actual = factory.buyRule(tradeStrategy, BAR_SERIES);

        assertThat(actual.getLow().toString()).isEqualTo(
                expected.getLow().toString());
        assertThat(actual.getUp().toString()).isEqualTo(
                expected.getUp().toString());
    }

    @ParameterizedTest
    @EnumSource(SellRuleWithMissingArgumentsTestCase.class)
    void sellRule_withMissingArguments_throwsIllegalArgumentException(
            SellRuleWithMissingArgumentsTestCase testCase) {
        assertThrows(IllegalArgumentException.class,
                () -> factory.sellRule(testCase.tradeStrategy, BAR_SERIES));
    }

    @ParameterizedTest
    @MethodSource("signalStrengths")
    void sellRule_withValidSignalStrengths_returnsExitRule(
            SignalStrength signalStrength) {
        assume().that(signalStrength).isNotEqualTo(SignalStrength.UNSPECIFIED);
        int sellThreshold = getSignalStrengthRange(signalStrength).upperEndpoint();
        CrossedDownIndicatorRule expected = new CrossedDownIndicatorRule(INDICATOR, sellThreshold);
        TradeStrategy.ADX adx = ADX_TRADE_STRATEGY.toBuilder()
                .setSellSignalStrength(signalStrength)
                .build();
        TradeStrategy tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();

        CrossedDownIndicatorRule actual = factory.sellRule(tradeStrategy, BAR_SERIES);

        assertThat(actual.getLow().toString()).isEqualTo(
                expected.getLow().toString());
        assertThat(actual.getUp().toString()).isEqualTo(
                expected.getUp().toString());
    }

    private Range<Integer> getSignalStrengthRange(SignalStrength buySignalStrength) {
        return ADX_VALUES_BY_SIGNAl_STRENGTH.get(buySignalStrength);
    }

    private enum BuyRuleWithMissingArgumentsTestCase {
        EMPTY(TradeStrategy.ADX.getDefaultInstance()),
        NEGATIVE_BAR_COUNT(TRADE_STRATEGY_WITH_NEGATIVE_BAR_COUNT),
        ZERO_BAR_COUNT(TRADE_STRATEGY_WITH_ZERO_BAR_COUNT),
        HIGHER_BAR_COUNT_THAN_BAR_SERIES_COUNT(TRADE_STRATEGY_WITH_EXCESSIVE_BAR_COUNT),
        NO_BUY_SIGNAL_STRENGTH(ADX_TRADE_STRATEGY.toBuilder()
                .clearBuySignalStrength()
                .build());

        private final TradeStrategy tradeStrategy;

        BuyRuleWithMissingArgumentsTestCase(TradeStrategy.ADX adx) {
            this.tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();
        }
    }

    private enum SellRuleWithMissingArgumentsTestCase {
        EMPTY(TradeStrategy.ADX.getDefaultInstance()),
        NEGATIVE_BAR_COUNT(TRADE_STRATEGY_WITH_NEGATIVE_BAR_COUNT),
        ZERO_BAR_COUNT(TRADE_STRATEGY_WITH_ZERO_BAR_COUNT),
        HIGHER_BAR_COUNT_THAN_BAR_SERIES_COUNT(TRADE_STRATEGY_WITH_EXCESSIVE_BAR_COUNT),
        NO_SELL_SIGNAL_STRENGTH(ADX_TRADE_STRATEGY.toBuilder()
                .clearSellSignalStrength()
                .build());

        private final TradeStrategy tradeStrategy;

        SellRuleWithMissingArgumentsTestCase(TradeStrategy.ADX adx) {
            this.tradeStrategy = TradeStrategy.newBuilder().setAdx(adx).build();
        }
    }

    private static class FakeIndicatorFactory implements IndicatorFactory {
        @Override
        public Indicator<Num> create(TradeStrategy tradeStrategy, BarSeries barSeries) {
            return INDICATOR;
        }
    }

    private static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(IndicatorFactory.class).to(FakeIndicatorFactory.class);
        }
    }
}
