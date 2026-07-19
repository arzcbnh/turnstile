plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.spotless)
}

version = "0.1.0-beta.1"
group = "dev.arzcbnh"

loom {
    splitEnvironmentSourceSets()

    mods {
        create("sublog") {
            sourceSet(sourceSets.main.get())
        }
    }
}

repositories {
    maven {
        url = uri("https://maven.nucleoid.xyz")
    }

    ivy {
        url = uri("https://github.com/NucleoidMC/Server-Translations/releases/download")
        patternLayout {
            artifact("v[revision]/[artifact]-[revision].[ext]")
        }
        metadataSources {
            artifact()
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)
    implementation(libs.serverTranslations)
    include(libs.serverTranslations.api)
}

tasks.processResources {
    val fabric_version = libs.versions.fabric.api.map {
        val (major, minor) = it.substringBefore("+").split(".")
        ">=$major.$minor"
    }

    inputs.property("version", project.version)
    inputs.property("minecraft_version", libs.versions.minecraft)
    inputs.property("fabric_version", fabric_version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to libs.versions.minecraft.get(),
            "fabric_version" to fabric_version.get()
        )
    }

    from("src/main/resources/assets/sublog/lang") {
        into("data/sublog/lang")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    from("LICENSE.txt")
}

spotless {
    format("misc") {
        target(".gitattributes", ".gitignore", "*.kts", "*.properties", "*.toml")
        trimTrailingWhitespace()
        leadingTabsToSpaces()
        endWithNewline()
    }

    java {
        palantirJavaFormat("2.94.0").formatJavadoc(true)
        formatAnnotations()
    }

    json {
        target("src/**/*.json")
        gson().indentWithSpaces(2)
    }
}
