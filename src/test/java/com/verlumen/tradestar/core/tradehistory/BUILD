load("//:junit5.bzl", "java_junit5_test")

# Use java_test after bazel officially support junit5
java_junit5_test(
    name = "bar_factory_test",
    srcs = glob(["BarFactoryTest.java"]),
    test_package = "com.verlumen.tradestar.core.tradehistory",
    deps = [
        "//src/main/java/com/verlumen/tradestar/core/tradehistory:bar_factory",
        "//src/main/java/com/verlumen/tradestar/core/tradehistory:granularity_spec",
        "@maven//:com_google_truth_truth",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
        "@tradestar_protos//:timestamp_java_proto",
    ],
)
