package com.atguigu.core.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Demo02 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Demo02")
    val sc: SparkContext = new SparkContext(conf)
    //    val listRDD: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3)))
    //    listRDD.aggregateByKey()
    val rdd: RDD[(String, Int)] = sc.parallelize(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)
    rdd.saveAsTextFile("output")
    rdd.saveAsSequenceFile("output01")
    rdd.saveAsObjectFile("output02")
  }
}
