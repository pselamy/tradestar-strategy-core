workspace(name = "tradestar_strategy_core")

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "contrib_rules_jvm",
    remote = "https://github.com/bazel-contrib/rules_jvm",
    tag = "v0.1.0",
)

load("@contrib_rules_jvm//:repositories.bzl", "contrib_rules_jvm_deps")

contrib_rules_jvm_deps()

load("@contrib_rules_jvm//:setup.bzl", "contrib_rules_jvm_setup")

contrib_rules_jvm_setup()

git_repository(
    name = "rules_jvm_external",
    remote = "https://github.com/bazelbuild/rules_jvm_external",
    tag = "4.2",
)

git_repository(
    name = "com_google_protobuf",
    commit = "498de9f761bef56a032815ee44b6e6dbe0892cc4",
    remote = "https://github.com/protocolbuffers/protobuf",
    shallow_since = "1580681072 -0800",
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

git_repository(
    name = "tradestar_protos",
    commit = "835a65045f1fc3353ec4c8035cd8aab8d8c36067",
    remote = "https://github.com/pselamy/tradestar-protos",
    shallow_since = "1645389653 -0600",
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.auto.value:auto-value:1.9",
        "com.google.auto.value:auto-value-annotations:1.9",
        "com.google.guava:guava:31.0.1-jre",
        "com.google.inject:guice:5.0.1",
        "org.ta4j:ta4j-core:0.14",
        # Test Only Artifacts
        "com.google.truth:truth:1.1.2",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

load(":junit5.bzl", "junit_jupiter_java_repositories", "junit_platform_java_repositories")

JUNIT_JUPITER_VERSION = "5.8.2"

JUNIT_PLATFORM_VERSION = "1.8.2"

junit_jupiter_java_repositories(
    version = JUNIT_JUPITER_VERSION,
)

junit_platform_java_repositories(
    version = JUNIT_PLATFORM_VERSION,
)
