package com.verlumen.tradestar.core.strategies;

import com.google.inject.AbstractModule;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

public class StrategiesModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StrategyFactory.class).to(StrategyFactoryImpl.class);
        newSetBinder(binder(), StrategyConverter.class)
                .addBinding().to(AdxStrategyConverter.class);
    }
}
