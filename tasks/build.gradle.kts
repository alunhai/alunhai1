plugins {
    scala
    id("com.github.johnrengelman.shadow") version("7.1.0")
}

dependencies {
    implementation("com.github.scopt:scopt_2.11:4.0.1")
    implementation("mysql:mysql-connector-java:8.0.27")
    compileOnly("org.apache.spark:spark-core_2.11:2.4.8")
    compileOnly("org.apache.spark:spark-sql_2.11:2.4.8")
}