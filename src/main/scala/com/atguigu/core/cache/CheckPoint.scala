package com.atguigu.core.cache

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CheckPoint {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local").setAppName("CheckPoint")
    val sc: SparkContext = new SparkContext(config)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    val mapRdd = rdd.map((_, 1))
    //设定检查点的保存目录
    sc.setCheckpointDir("check")
    val reduceRDD = mapRdd.reduceByKey(_ + _)
    reduceRDD.checkpoint()
    println(reduceRDD.toDebugString)
    reduceRDD.foreach(println)
    sc.stop()
  }
}
