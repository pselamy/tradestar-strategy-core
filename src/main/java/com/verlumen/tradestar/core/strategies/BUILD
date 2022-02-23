java_library(
    name = "adx_strategy_converter",
    srcs = ["AdxStrategyConverter.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        ":strategy_converter",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "strategies_module",
    srcs = ["StrategiesModule.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":adx_strategy_converter",
        ":strategy_converter",
        ":strategy_factory",
        ":strategy_factory_impl",
        "@maven//:com_google_inject_guice",
    ],
)

java_library(
    name = "strategy_converter",
    srcs = ["StrategyConverter.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

java_library(
    name = "strategy_factory",
    srcs = ["StrategyFactory.java"],
    visibility = ["//visibility:public"],
    deps = [
        ":strategy_converter",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
