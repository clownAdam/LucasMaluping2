package com.atguigu.sql.udf

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}

object UdafDemo {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local").setAppName("UdafDemo")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    val udaf: MyAgeAvgFunction = new MyAgeAvgFunction()
    spark.udf.register("avgAge",udaf)
    val frame: DataFrame = spark.read.json("json/user.json")
    frame.createTempView("user")
    spark.sql("select avgAge(age) from user").show()
    spark.stop()
  }
}

//声明自定义聚合函数
class MyAgeAvgFunction extends UserDefinedAggregateFunction {
  //input structtype
  override def inputSchema: StructType = {
    new StructType().add("age", LongType)
  }

  override def bufferSchema: StructType = {
    new StructType().add("sum", LongType).add("count", LongType)
  }

  override def dataType: DataType = DoubleType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 0L
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }
}

