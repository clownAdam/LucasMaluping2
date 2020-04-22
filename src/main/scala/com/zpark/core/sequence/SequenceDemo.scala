package com.zpark.core.sequence

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SequenceDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = initSpark
    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("clown", 21), ("sequential", 21)))
    sequence(sc, rdd)
    val result: RDD[Nothing] = sc.objectFile("output/object")
    result.foreach(println)

  }

  private def sequence(sc: SparkContext, rdd: RDD[(String, Int)]): Unit = {
//    rdd.saveAsSequenceFile("output/sequence")
    //    rdd.saveAsObjectFile("output/object")
    val secqu: RDD[(Text, IntWritable)] = sc.sequenceFile("output/sequence", classOf[Text], classOf[IntWritable])
    val result: RDD[Any] = secqu.map(
      line => line match {
        case (k: Text, v: IntWritable) => (k.toString, v.get())
      }
    )
    result.foreach(println)
  }

  def initSpark: SparkContext = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("SequenceDemo")
    val sc: SparkContext = new SparkContext(conf)
    sc
  }


}
