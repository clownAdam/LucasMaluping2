package com.zpark.core

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object HbaseDemo {
  def initSpark():SparkContext ={
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("HbaseDemo")
    val sc: SparkContext = new SparkContext(conf)
    sc
  }

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = initSpark()
    val hbaseConf: Configuration = HBaseConfiguration.create()
//    hbaseConf.set("hbase.zookeeper.quorum","hadoop131,hadoop13132,hadoop133")
//    hbaseConf.set("hbase.zookeeper,property.clientPort","2181")
    hbaseConf.set(TableInputFormat.INPUT_TABLE,"stu")
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]
    )
    hbaseRDD.cache()
    println(hbaseRDD.count())
    hbaseRDD.foreach{
      case (_,result) =>{
        val key: String = Bytes.toString(result.getRow)
        val name: String = Bytes.toString(result.getValue("cf1".getBytes, "name".getBytes))
        println(key+"\t"+name)
      }
    }
  }
}
