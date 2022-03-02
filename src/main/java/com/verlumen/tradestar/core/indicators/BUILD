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
