package com.verlumen.tradestar.core.indicators;

import com.google.inject.AbstractModule;

public class IndicatorsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IndicatorFactory.class).to(IndicatorFactoryImpl.class);
    }
}
