package com.zpark.core.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSON

object JsonDemo {
  def main(args: Array[String]): Unit = {
    val sc: SparkContext = initSc
    val json: RDD[String] = sc.textFile("json/user.json")
//    val jsonDemo: RDD[Option[Any]] = json.map(line => JSON.parseFull(line))
    val result: RDD[Option[Any]] = json.map(JSON.parseFull)
//    jsonDemo.foreach(println)
//    matchJson(result)
//    filterJson(result)
    val jsonStr = "{\"name\":\"clown\",\"age\":21}"
//val jsonStr="{\"name\": \"123\",\"age\": 20}"
    val mapper: ObjectMapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val user: User = mapper.readValue(jsonStr, classOf[User])
    println(user)
  }

  private def filterJson(result: RDD[Option[Any]]): RDD[Option[Any]] = {
    val jsonFilter: RDD[Option[Any]] = result.filter {
      case Some(map: Map[String, Any]) => true
      case None => false
    }
    jsonFilter
  }

  private def matchJson(result: RDD[Option[Any]]): Unit = {
    result.foreach(
      r => r match {
        case Some(map: Map[String, Any]) => println(r)
        case None => println("not is json")
        case other => println("unknow error" + other)
      }
    )
  }

  private def initSc: SparkContext = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("JsonDemo")
    val sc = new SparkContext(conf)
    sc
  }
}
case class User(name:String,age:Integer)