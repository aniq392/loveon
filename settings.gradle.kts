pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // 💛 카카오 SDK 다운로드를 위한 전용 레포지토리 주소 추가
        maven {
            url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/")
        }
    }
}

rootProject.name = "LoveOn"
include(":app")