load("//:junit5.bzl", "java_junit5_test")

# Use java_test after bazel officially support junit5
java_junit5_test(
    name = "strategy_factory_impl_test",
    srcs = glob(["StrategyFactoryImplTest.java"]),
    test_package = "com.verlumen.tradestar.core.strategies",
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/rules:rule_factory",
        "//src/main/java/com/verlumen/tradestar/core/strategies:strategy_factory_impl",
        "//src/main/java/com/verlumen/tradestar/core/strategies:trade_strategy_id",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_truth_truth",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
