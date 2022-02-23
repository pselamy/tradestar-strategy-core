load("//:junit5.bzl", "java_junit5_test")

# Use java_test after bazel officially support junit5
java_junit5_test(
    name = "strategy_factory_test",
    srcs = glob(["StrategyFactoryTest.java"]),
    test_package = "com.verlumen.tradestar.core.strategies",
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/strategies:strategy_converter",
        "//src/main/java/com/verlumen/tradestar/core/strategies:strategy_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:com_google_truth_truth",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
