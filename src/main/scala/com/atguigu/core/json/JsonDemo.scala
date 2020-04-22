package com.atguigu.core.json

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSON

object JsonDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("JsonDemo")
    val sc: SparkContext = new SparkContext(conf)
    val json: RDD[String] = sc.textFile("json/user.json")
    val result: RDD[Option[Any]] = json.map(JSON.parseFull)
    result.foreach(println)
  }
}
