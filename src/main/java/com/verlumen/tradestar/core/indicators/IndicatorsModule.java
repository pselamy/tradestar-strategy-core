package com.verlumen.tradestar.core.indicators;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.util.EnumSet.allOf;

public class IndicatorsModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<IndicatorFactory> indicatorFactoryBinder =
                newSetBinder(binder(), IndicatorFactory.class);
        allOf(IndicatorFactoryProvider.class)
                .forEach(indicatorFactoryBinder.addBinding()::toProvider);
    }
}
