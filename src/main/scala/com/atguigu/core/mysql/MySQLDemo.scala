package com.atguigu.core.mysql

import java.sql.DriverManager

import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.{SparkConf, SparkContext}

object MySQLDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("MySQLDemo")
    val sc: SparkContext = new SparkContext(conf)
    //mysql prarm
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/rdd"
    val username = "root"
    val password = "root"
    /*val jdbc: JdbcRDD[Unit] = new JdbcRDD(
      sc,
      () => {
        Class.forName(driver)
        DriverManager.getConnection(url, username, password)
      },
//      "select name,age from rddtable where id>=? and id<=?",
      "select name,age from rddtable where id>=? and id<=?",
      1,
      3,
      2,
      (rs) => {
        println(rs.getString(1) +","+ rs.getInt(2))
      }
    )
    jdbc.collect()*/

    println("--------------------------------------")
    val jdbcRDD: RDD[(String, Int)] = sc.makeRDD(List(("zhangsan33", 20), ("clown33", 21), ("zhangchun33", 20)), 2)

    /*jdbcRDD.foreach {
      case (uname, age) => {
        val statement = connection.prepareStatement("insert into rddtable(name,age) values(?,?)")
        statement.setString(1, uname)
        statement.setInt(2, age)
        statement.executeUpdate()
        statement.close()
      }
    }*/
    jdbcRDD.foreachPartition(jdbc => {
      Class.forName(driver)
      val connection = DriverManager.getConnection(url, username, password)
      jdbc.foreach {
        case (uname, age) => {
          val statement = connection.prepareStatement("insert into rddtable(name,age) values(?,?)")
          statement.setString(1, uname)
          statement.setInt(2, age)
          statement.executeUpdate()
          statement.close()
        }
      }
      connection.close()
    })
  }
}
