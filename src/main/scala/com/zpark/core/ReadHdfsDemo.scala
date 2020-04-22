package com.zpark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ReadHdfsDemo {
  var hdfsFilePath:String = "hdfs://hadoop131:8020/tmp.txt"
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = initSpark
    val lines: RDD[String] = sc.textFile(hdfsFilePath)
    lines.foreach(println)

  }
  def initSpark: SparkContext = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("ReadHdfsDemo")
    val sc: SparkContext = new SparkContext(conf)
    sc
  }
}
