load("//:junit5.bzl", "java_junit5_test")

# Use java_test after bazel officially support junit5
java_junit5_test(
    name = "adx_rule_factory_test",
    srcs = glob(["AdxRuleFactoryTest.java"]),
    test_package = "com.verlumen.tradestar.core.strategies",
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/strategies:adx_rule_factory",
        "//src/main/java/com/verlumen/tradestar/core/strategies:indicator_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_truth_truth",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)

# Use java_test after bazel officially support junit5
java_junit5_test(
    name = "strategy_factory_test",
    srcs = glob(["StrategyFactoryTest.java"]),
    test_package = "com.verlumen.tradestar.core.strategies",
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/strategies:rule_factory",
        "//src/main/java/com/verlumen/tradestar/core/strategies:strategy_factory",
        "//src/main/java/com/verlumen/tradestar/core/strategies:trade_strategy_id",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_truth_truth",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
