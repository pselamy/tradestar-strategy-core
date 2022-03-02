package com.verlumen.tradestar.core.strategies;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.multibindings.Multibinder;
import com.verlumen.tradestar.core.rules.RuleFactory;
import com.verlumen.tradestar.protos.strategies.SignalStrength;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.ta4j.core.*;

import java.util.EnumSet;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StrategyFactoryImplTest {
    private static final Rule BUY_RULE = (i, tradingRecord) -> false;
    private static final Rule SELL_RULE = (i, tradingRecord) -> true;

    private static final BaseBarSeries FAKE_BAR_SERIES =
            new BaseBarSeriesBuilder().build();
    private static final TradeStrategy ADX_TRADE_STRATEGY =
            TradeStrategy.newBuilder()
                    .setAdx(TradeStrategy.ADX.newBuilder()
                            .setBarCount(123)
                            .setBuySignalStrength(SignalStrength.STRONG)
                            .setSellSignalStrength(SignalStrength.WEAK))
                    .build();

    @Inject
    private StrategyFactoryImpl strategyFactory;

    @BeforeEach
    void setup() {
        Guice.createInjector(new TestModule()).injectMembers(this);
    }

    @ParameterizedTest
    @EnumSource(UnsupportedTradeStrategyTestCase.class)
    void create_withUnsupportedTradeStrategy_throwsIllegalArgumentException(
            UnsupportedTradeStrategyTestCase testCase) {
        assertThrows(IllegalArgumentException.class,
                () -> strategyFactory.create(testCase.tradeStrategy,
                        FAKE_BAR_SERIES));
    }

    @ParameterizedTest
    @EnumSource(TradeStrategy.StrategyOneOfCase.class)
    void create_withTradeStrategy_createsTa4jStrategy(
            TradeStrategy.StrategyOneOfCase oneOfCase) {
        assume().that(oneOfCase).isNotEqualTo(
                TradeStrategy.StrategyOneOfCase.STRATEGYONEOF_NOT_SET);
        assume().that(oneOfCase).isNotEqualTo(
                TradeStrategy.StrategyOneOfCase.COMPOSITE);
        TradeStrategy fakeTradeStrategy = TestTradeStrategy.get(oneOfCase);
        String expectedName = "ADX-123-BUY-STRONG-SELL-WEAK";

        Strategy strategy = strategyFactory.create(fakeTradeStrategy,
                FAKE_BAR_SERIES);

        assertThat(strategy.getName()).isEqualTo(expectedName);
        assertThat(strategy.getEntryRule()).isSameInstanceAs(BUY_RULE);
        assertThat(strategy.getExitRule()).isSameInstanceAs(SELL_RULE);
    }

    private enum FakeRuleFactory implements RuleFactory {
        ADX(TradeStrategy.StrategyOneOfCase.ADX);

        private final TradeStrategy.StrategyOneOfCase supportedCase;

        FakeRuleFactory(TradeStrategy.StrategyOneOfCase supportedCase) {
            this.supportedCase = supportedCase;
        }

        @Override
        public Rule buyRule(TradeStrategy params, BarSeries barSeries) {
            return BUY_RULE;
        }

        @Override
        public Rule sellRule(TradeStrategy params, BarSeries barSeries) {
            return SELL_RULE;
        }

        @Override
        public TradeStrategy.StrategyOneOfCase supportedCase() {
            return supportedCase;
        }
    }

    private enum TestTradeStrategy {
        ADX(ADX_TRADE_STRATEGY);

        private final TradeStrategy tradeStrategy;

        TestTradeStrategy(TradeStrategy tradeStrategy) {
            this.tradeStrategy = tradeStrategy;
        }

        public static TradeStrategy get(
                TradeStrategy.StrategyOneOfCase oneOfCase) {
            return valueOf(oneOfCase.name()).tradeStrategy;
        }
    }

    private enum UnsupportedTradeStrategyTestCase {
        STRATEGYONEOF_NOT_SET(TradeStrategy.getDefaultInstance()),
        COMPOSITE(TradeStrategy.newBuilder()
                .setComposite(TradeStrategy.Composite.newBuilder()
                        .addStrategies(ADX_TRADE_STRATEGY))
                .build());

        private final TradeStrategy tradeStrategy;
        private final TradeStrategy.StrategyOneOfCase oneOfCase;

        UnsupportedTradeStrategyTestCase(TradeStrategy tradeStrategy) {
            this.tradeStrategy = tradeStrategy;
            this.oneOfCase = TradeStrategy.StrategyOneOfCase.valueOf(name());
        }
    }

    private static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            Multibinder<RuleFactory> ruleFactoryBinder =
                    newSetBinder(binder(), RuleFactory.class);
            EnumSet.allOf(FakeRuleFactory.class).forEach(
                    ruleFactoryBinder.addBinding()::toInstance);
        }

    }
}
