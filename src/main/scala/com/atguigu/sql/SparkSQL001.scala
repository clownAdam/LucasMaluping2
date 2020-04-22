package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL001 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL001")
    //    val sc: SparkContext = new SparkContext(conf)
    val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val frame: DataFrame = sparkSession.read.json("json/user.json")
    frame.show()
    sparkSession.stop()
  }
}
