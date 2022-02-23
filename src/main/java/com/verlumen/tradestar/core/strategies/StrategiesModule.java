package com.verlumen.tradestar.core.strategies;

import com.google.inject.AbstractModule;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class StrategiesModule extends AbstractModule {
    @Override
    protected void configure() {
        newSetBinder(binder(), StrategyConverter.class)
                .addBinding().to(AdxStrategyConverter.class);
    }
}
