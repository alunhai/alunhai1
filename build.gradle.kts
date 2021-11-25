plugins {
    scala
    java
    id("org.springframework.boot") version "2.5.4"
    id("com.github.johnrengelman.shadow") version("7.1.0")
}
group = "com.mhy.work"
version = "1.0-SNAPSHOT"


repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    mavenCentral()
}

dependencies {
    implementation("org.apache.hive:hive-jdbc:3.1.2")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("mysql:mysql-connector-java:8.0.27")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.create("product", type = Copy::class) {
    group = "build"
    from("server/build/libs/")
    from("tasks/build/libs/")
    into("build/libs/")
}.dependsOn(":server:bootJar")
        .dependsOn(":tasks:shadowJar")