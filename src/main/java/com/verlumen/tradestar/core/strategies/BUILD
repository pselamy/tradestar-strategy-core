java_library(
    name = "adx_rule_factory",
    srcs = ["AdxRuleFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        ":indicator_factory",
        ":rule_factory",
        ":strategy_one_of_cases",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "indicator_factory",
    srcs = ["IndicatorFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "indicator_factory_provider",
    srcs = ["IndicatorFactoryProvider.java"],
    deps = [
        "indicator_factory",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "rule_factory",
    srcs = ["RuleFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "strategies_module",
    srcs = ["StrategiesModule.java"],
    visibility = ["//visibility:public"],
    deps = [
        "strategy_factory",
        "strategy_factory_impl",
        ":adx_rule_factory",
        ":indicator_factory",
        ":indicator_factory_provider",
        ":rule_factory",
        "//src/main/java/com/verlumen/tradestar/core/signalstrength:signal_strength_module",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "strategy_factory",
    srcs = ["StrategyFactory.java"],
    visibility = ["//visibility:public"],
    deps = [
        "trade_strategy_id",
        ":rule_factory",
        ":strategy_one_of_cases",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "strategy_factory_impl",
    srcs = ["StrategyFactoryImpl.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "trade_strategy_id",
        ":rule_factory",
        ":strategy_factory",
        ":strategy_one_of_cases",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "strategy_one_of_cases",
    srcs = ["StrategyOneOfCases.java"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "trade_strategy_id",
    srcs = ["TradeStrategyIdentifier.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
