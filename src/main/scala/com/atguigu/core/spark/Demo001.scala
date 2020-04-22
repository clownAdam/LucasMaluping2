package com.atguigu.core.spark

import org.apache.spark.{SparkConf, SparkContext}

object Demo001 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Demo001")
    val sc: SparkContext = new SparkContext(conf)

  }
}
class MyPartitioner(partitions : scala.Int) extends org.apache.spark.Partitioner{
  override def numPartitions: Int = {
    partitions
  }

  override def getPartition(key: Any): Int = {
    1
  }
}
