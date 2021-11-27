package com.movie.analyse.tasks

import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

object Main {
  protected lazy val sc: SparkContext = SparkContext
    .getOrCreate()
  protected lazy val spark: SparkSession = SparkSession
    .builder()
    .appName("Spark SQL basic example")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()

  private var t : Long = 0

  def main(args: Array[String]): Unit = {
    t = args(0).toLong
    val props = new Properties()
    props.put("username", "bytedance")
    props.put("password", "bytedance")
    val jdbcUrl = "jdbc:mysql://localhost:3306/movie_analyse"

    val userDf = spark.read.jdbc(jdbcUrl, "user", props)
    val movieDf = spark.read.jdbc(jdbcUrl, "movie", props)

    val ratingDf = spark.read.format("csv").option("header", "true").load("hdfs://localhost:9000/ml-latest/ratings.csv")
    val tagDf = spark.read.format("csv").option("header", "true").load("hdfs://localhost:9000/ml-latest/tags.csv")

    userDf.createOrReplaceTempView("user")
    movieDf.createOrReplaceTempView("movie")
    ratingDf.createOrReplaceTempView("rating")
    tagDf.createOrReplaceTempView("tag")

    withDf()
    byGender()
    byAge()
    byOccupation()
    byZipCode()
  }

  def withDf(): Unit = {
    val df = spark sql
      """
        | select rating.rating, user.gender, user.age, user.occupation, user.zipCode, movie.genres
        | from rating join user join movie
        | on rating.userId = user.id and movie.id = rating.movieId
        |""".stripMargin
    df.cache()
    df.createOrReplaceTempView("wtd")
  }

  def byGender(): Unit = {
    val df = spark sql
      """
        |select category, gender, avg(rating) as rate_avg from wtd lateral view explode(split(genres,'\\|')) as category
        |group by category, gender
        |""".stripMargin
    df.write.csv(s"hdfs://localhost:9000/analyse/gender-$t.csv")
  }

  def byAge(): Unit = {
    val df=spark sql
      """
        |select category, age, avg(rating) as rate_avg from wtd lateral view explode(split(genres,'\\|')) as category
        |group by category, age
        |""".stripMargin
    df.write.csv(s"hdfs://localhost:9000/analyse/age-$t.csv")
  }

  def byOccupation(): Unit = {
    val df=spark sql
      """
        |select category, occupation, avg(rating) as rate_avg from wtd lateral view explode(split(genres,'\\|')) as category
        |group by category, occupation
        |""".stripMargin
    df.write.csv(s"hdfs://localhost:9000/analyse/occupation-$t.csv")
  }

  def byZipCode(): Unit = {
    val df=spark sql
      """
        |select category, zipCode, avg(rating) as rate_avg from wtd lateral view explode(split(genres,'\\|')) as category
        |group by category, zipCode
        |""".stripMargin
    df.write.csv(s"hdfs://localhost:9000/analyse/zipcode-$t.csv")
  }
}
