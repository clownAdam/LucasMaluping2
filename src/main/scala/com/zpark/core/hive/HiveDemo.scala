package com.zpark.core.hive

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object HiveDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("HiveDemo")
    val sc: SparkContext = new SparkContext(conf)
    val hiveCtx: HiveContext = new HiveContext(sc)
    /*hive*/
    val rows: DataFrame = hiveCtx.sql("select name,age from zpark.users")
    rows.show()
    /*json*/
    println("---------------")
    val frame: DataFrame = hiveCtx.read.json("json/user.json")
    frame.createTempView("user")
    val res: DataFrame = hiveCtx.sql("select * from user")
    res.show()
  }
}
