package com.verlumen.tradestar.core.strategies;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.ADX;
import com.verlumen.tradestar.protos.strategies.TradeStrategy.Composite;
import com.verlumen.tradestar.protos.strategies.TradeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.ta4j.core.*;
import org.ta4j.core.rules.BooleanRule;

import java.util.function.Function;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StrategyFactoryTest {
    private static final ImmutableMap<TradeStrategy.StrategyOneOfCase,
            TradeStrategy> TRADE_STRATEGIES =
            ImmutableMap.<TradeStrategy.StrategyOneOfCase,
                            TradeStrategy>builder()
                    .put(TradeStrategy.StrategyOneOfCase.COMPOSITE,
                            TradeStrategy.newBuilder()
                                    .setComposite(Composite.getDefaultInstance())
                                    .build())
                    .put(TradeStrategy.StrategyOneOfCase.ADX,
                            TradeStrategy.newBuilder()
                                    .setAdx(ADX.getDefaultInstance())
                                    .build())
                    .buildOrThrow();
    private static final ImmutableMap<TradeStrategy.StrategyOneOfCase, FakeStrategyConverter> STRATEGY_CONVERTERS =
            stream(TradeStrategy.StrategyOneOfCase.values())
                    .filter(oneOfCase -> !oneOfCase.equals(TradeStrategy.StrategyOneOfCase.STRATEGYONEOF_NOT_SET))
                    .collect(toImmutableMap(Function.identity(), FakeStrategyConverter::new));

    private static final BaseBarSeries FAKE_BAR_SERIES = new BaseBarSeriesBuilder().build();

    @Inject
    private StrategyFactory strategyFactory;

    @BeforeEach
    void setup() {
        Guice.createInjector(new TestModule()).injectMembers(this);
    }

    @Test
    void create_withNoTradeStrategy_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> strategyFactory.create(FAKE_BAR_SERIES,
                        TradeStrategy.getDefaultInstance()));
    }

    @ParameterizedTest
    @EnumSource(TradeStrategy.StrategyOneOfCase.class)
    void create_withTradeStrategy_CreatesStrategyWithCorrectConverter(TradeStrategy.StrategyOneOfCase oneOfCase) {
        assume().that(oneOfCase).isNotEqualTo(
                TradeStrategy.StrategyOneOfCase.STRATEGYONEOF_NOT_SET);
        FakeStrategyConverter fakeStrategyConverter =
                requireNonNull(STRATEGY_CONVERTERS.get(oneOfCase));
        TradeStrategy fakeTradeStrategy =
                requireNonNull(TRADE_STRATEGIES.get(oneOfCase));
        Strategy expected = fakeStrategyConverter.fakeStrategy;

        Strategy strategy = strategyFactory.create(FAKE_BAR_SERIES,
                fakeTradeStrategy);

        assertThat(strategy).isEqualTo(expected);
    }

    private static class FakeStrategyConverter implements StrategyConverter {
        private final Strategy fakeStrategy;
        private final TradeStrategy.StrategyOneOfCase supportedCase;

        FakeStrategyConverter(TradeStrategy.StrategyOneOfCase supportedCase) {
            this.fakeStrategy = new BaseStrategy(supportedCase.name(),
                    new BooleanRule(false), new BooleanRule(true));
            this.supportedCase = supportedCase;
        }

        @Override
        public Strategy convert(TradeStrategy tradeStrategy, BarSeries barSeries) {
            return fakeStrategy;
        }

        @Override
        public TradeStrategy.StrategyOneOfCase supportedCase() {
            return supportedCase;
        }
    }

    private static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            STRATEGY_CONVERTERS.values().forEach(this::bindConverter);
        }

        private void bindConverter(StrategyConverter converter) {
            newSetBinder(binder(), StrategyConverter.class)
                    .addBinding().toInstance(converter);
        }
    }
}
