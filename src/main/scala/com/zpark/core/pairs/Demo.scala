package com.zpark.core.pairs

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Demo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Demo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] = sc.parallelize(List(("panda", 0), ("pink", 3), ("pirate", 3), ("panda", 1), ("pink", 4)))
    val maps: RDD[(String, (Int, Int))] = rdd.mapValues(x => (x, 1))
    val value = maps.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
    value.foreach(println)
  }
}
