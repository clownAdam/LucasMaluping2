package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL001_Transform {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL001_Transform")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    val frame: DataFrame = spark.read.json("json/user.json")
//    frame.show()
    //采用sql语法访问数据
    //df --> table
    frame.createOrReplaceTempView("user")
    spark.sql("select * from user").show()


    spark.stop()
  }
}
