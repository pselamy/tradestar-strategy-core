package com.verlumen.tradestar.core.strategies;

import com.google.inject.AbstractModule;
import com.verlumen.tradestar.core.indicators.IndicatorsModule;
import com.verlumen.tradestar.core.rules.RulesModule;
import com.verlumen.tradestar.core.signalstrength.SignalStrengthModule;

public class StrategiesModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new IndicatorsModule());
        install(new RulesModule());
        install(new SignalStrengthModule());

        bind(StrategyFactory.class).to(StrategyFactoryImpl.class);
    }

}
