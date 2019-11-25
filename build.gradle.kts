plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "com.david"
version = "1.0-SNAPSHOT"

javafx {
    modules("javafx.controls", "javafx.base","javafx.fxml")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    compile("no.tornado:tornadofx:1.7.19")
    compile(group= "net.java.openjfx.backport", name= "openjfx-78-backport-compat", version= "1.8.0.1")
    compile(group= "org.freemarker", name= "freemarker", version= "2.3.29")
    compile("org.kordamp.bootstrapfx:bootstrapfx-core:0.2.4")

}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}