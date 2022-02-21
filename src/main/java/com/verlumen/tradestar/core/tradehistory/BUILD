java_library(
    name = "bar_factory",
    srcs = ["BarFactory.java"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
    ],
)

java_library(
    name = "bar_series_factory",
    srcs = ["BarSeriesFactory.java"],
    deps = [
        ":bar_factory",
        "@maven//:com_google_guava_guava",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:candles_java_proto",
    ],
)
