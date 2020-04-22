package com.zpark.core.file

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TextDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = initSpark
    val lines: RDD[String] = sc.textFile("input")
    val lines1: RDD[(String, String)] = sc.wholeTextFiles("input")
    lines.foreach(println)
    println("-------------")
    lines1.foreach(println)

  }

  private def initSpark: SparkContext = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("TextDemo")
    val sc = new SparkContext(conf)
    sc
  }
}
