package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SparkSQL002 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL002")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    // create rdd
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(List((1, "zhangsan", 20), (2, "lisi", 30), (3, "zhangsan", 40)))
    //transform df
    import spark.implicits._
    val df: DataFrame = rdd.toDF("id", "name", "age")
    //transform ds
    val ds: Dataset[User] = df.as[User]
    //transform df
    val frame: DataFrame = ds.toDF()
    //transform rdd
    val rdd1: RDD[Row] = df.rdd
    rdd1.foreach(row => {
      println(row.get(2))
    })
    spark.stop()
  }
}
case class User(id:Int,name:String,age:Int)
