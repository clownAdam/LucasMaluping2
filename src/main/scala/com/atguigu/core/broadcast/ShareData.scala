package com.atguigu.core.broadcast

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}

object ShareData {
  def main(args: Array[String]): Unit = {
    val config: SparkConf = new SparkConf().setMaster("local").setAppName("ShareData")
    val sc: SparkContext = new SparkContext(config)
    val dataRDD: RDD[String] = sc.makeRDD(List("hadoop","hive","hbase","scala","spark"), 2)
    var sum = 0
    val accumulator: LongAccumulator = sc.longAccumulator

    val acc: CordAccumulator = new CordAccumulator()
    sc.register(acc)
    dataRDD.foreach{
      case i => {
        acc.add(i)
      }
    }
//    dataRDD.foreach(i => sum+=i)
    println(acc.value)
    sc.stop()
  }
}
class CordAccumulator extends AccumulatorV2[String,util.ArrayList[String]]{
  val list = new util.ArrayList[String]()
  override def isZero: Boolean = list.isEmpty

  override def copy(): AccumulatorV2[String, util.ArrayList[String]] = {
    new CordAccumulator()
  }

  override def reset(): Unit = list.clear()

  override def add(v: String): Unit = {
    if(v.contains("h")){
      list.add(v)
    }
  }

  override def merge(other: AccumulatorV2[String, util.ArrayList[String]]): Unit = {
    list.addAll(other.value)
  }

  override def value: util.ArrayList[String] = list
}
