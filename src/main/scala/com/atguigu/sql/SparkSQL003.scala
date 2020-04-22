package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}

object SparkSQL003 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL003")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._
    // create rdd
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(List((1, "zhangsan", 20), (2, "lisi", 30), (3, "zhangsan", 40)))
    //transform ds
    val userRdd: RDD[User] = rdd.map {
      case (id: Int, name: String, age: Int) => {
        User(id, name, age)
      }
    }
    val userDs: Dataset[User] = userRdd.toDS()
    val rdd1: RDD[User] = userDs.rdd
    rdd1.foreach(println)
    spark.stop()
  }
}
