package com.atguigu.sql.udf

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, SparkSession, TypedColumn}

object UdafClassDemo {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local").setAppName("UdafDemo")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._
    val udaf: MyAgeAvgClassFunction = new MyAgeAvgClassFunction
    //将聚合函数转换为查询列
    val frame: DataFrame = spark.read.json("json/user.json")
    val avgCol: TypedColumn[UserBean, Double] = udaf.toColumn.name("avgAge")
    val userDS: Dataset[UserBean] = frame.as[UserBean]
    userDS.select(avgCol).show()
    spark.stop()
  }
}

case class UserBean(name: String, age: BigInt)

case class AvgBuffer(var sum: BigInt, var count: Int)

//声明自定义聚合函数-->强类型
class MyAgeAvgClassFunction extends Aggregator[UserBean, AvgBuffer, Double] {
  override def zero: AvgBuffer = {
    AvgBuffer(0, 0)
  }

  override def reduce(b: AvgBuffer, a: UserBean): AvgBuffer = {
    b.sum = b.sum + a.age
    b.count = b.count + 1
    b
  }

  override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
    b1.sum = b1.sum + b2.sum
    b1.count = b1.count + b2.count
    b1
  }

  override def finish(reduction: AvgBuffer): Double = {
    reduction.sum.toDouble / reduction.count
  }

  override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

