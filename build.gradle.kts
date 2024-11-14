
plugins {

    kotlin("jvm") version "1.9.10"
    application
    id("org.openjfx.javafxplugin") version "0.0.13"

}



repositories {

    mavenCentral()

}

javafx {
    version = "20"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {

    implementation("org.openjfx:javafx-controls:20")
    implementation("org.openjfx:javafx-fxml:20")

}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf(
        "--module-path",  "C:/javafx-sdk-20/lib",
        "--add-modules", "javafx.controls,javafx.fxml"
    )
}

