package com.atguigu.core.hbase

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object HbaseDemo {
  def main(args: Array[String]): Unit = {
    val config = new SparkConf().setMaster("local").setAppName("HbaseDemo")
    val sc: SparkContext = new SparkContext(config)
    //hbase config info
    val hbaseConf: Configuration = HBaseConfiguration.create()


    hbaseConf.set(TableInputFormat.INPUT_TABLE,"stu")

    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]
    )
    println(hbaseRDD.count())

    hbaseRDD.foreach{
      case (rowkey,result)=>{
        val cells: Array[Cell] = result.rawCells()
        for (cell <- cells){
          println(Bytes.toString(CellUtil.cloneValue(cell)))
        }
      }
    }
    println("--------------------------")
    val dataRDD: RDD[(String, String)] = sc.makeRDD(List(("1002", "zhangchun"), ("1003", "clown"), ("1004", "Serendipity")))
    val putRDD: RDD[(ImmutableBytesWritable, Put)] = dataRDD.map {
      case (rowkey, name) => {
        val put = new Put(Bytes.toBytes(rowkey))
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(name))
        (new ImmutableBytesWritable(Bytes.toBytes(rowkey)), put)
      }
    }
    val jobConf = new JobConf(hbaseConf)
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE,"student")
    putRDD.saveAsHadoopDataset(jobConf)

    sc.stop()
  }
}
