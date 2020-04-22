package com.atguigu.core.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SerializableDemo {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local").setAppName("SerializableDemo")
    val sc: SparkContext = new SparkContext(config)
    val rdd: RDD[String] = sc.parallelize(Array("hadoop", "spark", "hive", "atguigu"))
    val search = new Search("h")
    //    val match1 = search.getMatch1(rdd)
    //    match1.collect().foreach(println)
    val bb: RDD[String] = search.getMatch2(rdd)
    bb.foreach(println)
    sc.stop()
  }
}
class Search(query:String) {
  //过滤出包含字符串的数据
  def isMatch(s:String):Boolean = {
    s.contains(query)
  }
  /*过滤出包含字符串的rdd*/
  def getMatch1(rdd:RDD[String]):RDD[String] = {
    rdd.filter(isMatch)
  }
  /*过滤出包含字符串的rdd*/
  def getMatch2(rdd:RDD[String]):RDD[String] = {
    val q =query
    rdd.filter(x => x.contains(q))
  }
}