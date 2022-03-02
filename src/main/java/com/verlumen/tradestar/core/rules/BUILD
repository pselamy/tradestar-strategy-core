java_library(
    name = "adx_rule_factory",
    srcs = ["AdxRuleFactory.java"],
    visibility = ["//src/test/java:__subpackages__"],
    deps = [
        ":indicator_factory",
        ":rule_factory",
        ":strategy_one_of_cases",
        "//src/main/java/com/verlumen/tradestar/core/signalstrength:signal_strength_spec",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_inject_guice",
        "@maven//:org_ta4j_ta4j_core",
        "@tradestar_protos//:strategies_java_proto",
    ],
)
