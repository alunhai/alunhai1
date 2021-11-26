plugins {
    scala
    java
    id("org.springframework.boot") version "2.5.4"
}
group = "com.mhy.work"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        mavenCentral()
    }
}

dependencies {
//    implementation("org.apache.livy:livy-client-http:0.7.0-incubating")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("mysql:mysql-connector-java:8.0.27")
    implementation("org.apache.commons:commons-csv:1.9.0")
    implementation("org.apache.hadoop:hadoop-hdfs-client:2.10.1")
    implementation("org.apache.hadoop:hadoop-common:2.10.1") {
        exclude(module = "log4j")
        exclude(module = "slf4j-log4j12")
        exclude(group = "org.mortbay.jetty")
        exclude(group = "javax.servlet")
        exclude(group = "javax.servlet.jsp")
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.create("product", type = Copy::class) {
    group = "build"
    from("tasks/build/libs/")
    into("build/libs/")
}.dependsOn(":bootJar")
        .dependsOn(":tasks:shadowJar")