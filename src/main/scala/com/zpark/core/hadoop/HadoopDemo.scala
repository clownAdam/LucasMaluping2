package com.zpark.core.hadoop

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object HadoopDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = initSpark
    val job = new Job()
    val input = "json/user.json"
    val rdd: RDD[(Text, Text)] = sc.newAPIHadoopFile("json/user.json", classOf[KeyValueTextInputFormat], classOf[Text], classOf[Text], job.getConfiguration)
    rdd.foreach(println)
    rdd.saveAsNewAPIHadoopFile("output/hadoop/hadoop.json",classOf[Text],classOf[Text],classOf[TextOutputFormat[Text,Text]],job.getConfiguration)
  }

  def initSpark: SparkContext = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("HadoopDemo")
    val sc: SparkContext = new SparkContext(conf)
    sc
  }
}
