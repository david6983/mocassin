plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "com.david"
version = "1.0"

javafx {
    modules("javafx.controls", "javafx.base","javafx.fxml")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
    implementation("org.apache.logging.log4j:log4j-api:2.11.1")
    implementation("org.apache.logging.log4j:log4j-core:2.11.1")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    compile("no.tornado:tornadofx:1.7.19")
    compile(group= "net.java.openjfx.backport", name= "openjfx-78-backport-compat", version= "1.8.0.1")
    compile(group= "org.freemarker", name= "freemarker", version= "2.3.29")
    compile("org.kordamp.bootstrapfx:bootstrapfx-core:0.2.4")
    compile("org.mini2Dx:parcl:1.6.1")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

val bgImageName = "$name-background.png"
val bgImageWidth = 512

val jarOutputDir = "$buildDir/libs"
val iconPngPath = "$buildDir/resources/main/icons/mocassin.png"

val mainClass = "MainKt"

tasks.jar {
    manifest {
        attributes["Main-Class"] = mainClass
    }

    into("resources") {
        from("resources")
    }

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.register("jar2pkg") {
    val iconset = "${project.name}.iconset"
    val macosxPackage = "package/macosx"

    group = "deploy"
    description = "bundle output jar file to a mac os package using javapackager"
    // create the jar if not exist
    dependsOn("jar")

    doLast {
        val hasJavapackager = exec {
            workingDir(jarOutputDir)
            commandLine("command", "-v", "javapackager")
        }

        if (hasJavapackager.exitValue == 0) {
            println("### started bundling jar file to a mac os package using javapackager ###")
            exec {
                workingDir(jarOutputDir)
                println("[Create background icon image from main icon using sips]")
                commandLine("sips -z $bgImageWidth $bgImageWidth $iconPngPath --out $bgImageName".split(" "))
            }
            exec {
                workingDir(jarOutputDir)
                println("[creating iconset folder]")
                commandLine("mkdir", "-p", iconset)
            }

            exec {
                workingDir(jarOutputDir)
                println("[creating 128x128 icon in iconset]")
                commandLine("sips -z 128 128 $iconPngPath --out $iconset/icon_128x128.png".split(" "))
            }

            exec {
                workingDir(jarOutputDir)
                println("[converting iconset to icns]")
                commandLine("iconutil", "-c", "icns", iconset)
            }

            exec {
                workingDir(jarOutputDir)
                println("[creating package/macosx folder]")
                commandLine("mkdir", "-p", macosxPackage)
            }

            println("[copying icons and images in package/macosx]")
            copy {
                from(jarOutputDir)
                into("$jarOutputDir/$macosxPackage")
                include("*.png", "*.icns")
            }

            exec {
                workingDir(jarOutputDir)
                environment("JAVA_HOME", properties["org.gradle.java.home"])
                println("[creating package using javapackager]")
                println(commandLine("javapackager",
                    "-deploy", "-native", "pkg", "-name", project.name, "-Bappversion=$version",
                    "-Bicon=$macosxPackage/${project.name}.icns", "-srcdir", ".", "-srcfiles",
                    "${project.name}-$version.jar", "-appclass", mainClass, "-outdir", "deploy", "-outfile", project.name
                ))
            }

            println("[copying generated package to current build directory]")
            copy {
                from("$jarOutputDir/deploy/bundles")
                into(jarOutputDir)
                include("*.pkg")
                rename("${project.name}-${version}.pkg", "${project.name}-installer.pkg")
            }
            delete("$jarOutputDir/deploy")

            println("### The application has been packaged for mac os x ;) ###")
        } else {
            println("error: javapackager not found, make sure JAVA_HOME point to correct jdk (jdk1.8 for exemple)")
        }
    }
}