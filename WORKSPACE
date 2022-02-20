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
    commit = "af992c6540e65b16b4df52f8abed5f0ffa03c3d5",
    remote = "https://github.com/pselamy/tradestar-protos",
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.auto.value:auto-value:1.9",
        "com.google.auto.value:auto-value-annotations:1.9",
        "com.google.code.findbugs:jsr305:1.3.9",
        "com.google.errorprone:error_prone_annotations:2.0.18",
        "com.google.guava:guava:31.0.1-jre",
        "com.google.inject:guice:5.0.1",
        "com.google.mug:mug:5.9",
        "org.ta4j:ta4j-core:0.14",
        # Test Only Artifacts
        "com.google.inject.extensions:guice-testlib:5.1.0",
        "com.google.truth:truth:1.1.2",
        "org.junit.platform:junit-platform-engine:1.5.0",
        "org.junit.platform:junit-platform-launcher:1.5.0",
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
