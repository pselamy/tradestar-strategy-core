package com.verlumen.tradestar.core.strategies;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.util.EnumSet.allOf;

public class StrategiesModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StrategyFactory.class).to(StrategyFactoryImpl.class);

        Multibinder<IndicatorFactory> indicatorFactoryBinder =
                newSetBinder(binder(), IndicatorFactory.class);
        Multibinder<RuleFactory> ruleFactoryBinder =
                newSetBinder(binder(), RuleFactory.class);
        allOf(IndicatorFactoryProvider.class)
                .forEach(indicatorFactoryBinder.addBinding()::toProvider);
        ruleFactoryBinder.addBinding().to(AdxRuleFactory.class);
    }

}
