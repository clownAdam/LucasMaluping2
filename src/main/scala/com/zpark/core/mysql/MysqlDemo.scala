package com.zpark.core.mysql

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.{SparkConf, SparkContext}

object MysqlDemo {
  var driver = "com.mysql.jdbc.Driver"
  var url = "jdbc:mysql://localhost:3306/rdd"
  var user: String = "root"
  var password: String = "root"

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = initSparkContext

    /*query*/
    select(sc)
    println("++++++++++++++++++++++++++")
    /*insert*/
    val jdbcRdd: RDD[(String, Int)] = sc.makeRDD(List(("zc", 21), ("csq", 21), ("cn", 12)), 2)
    insertForeach(jdbcRdd)
//    jdbcRdd.foreachPartition(jdbc =>{
//
//    })
  }

  private def insertForeach(jdbcRdd: RDD[(String, Int)]): Unit = {
    jdbcRdd.foreach {
      case (uname, uage) => {
        val connections: Connection = getConnections
        val sql: String = "insert into rddtable(name,age) values (?,?)"
        val statement: PreparedStatement = connections.prepareStatement(sql)
        statement.setString(1, uname)
        statement.setInt(2, uage)
        statement.executeUpdate()
        statement.close()
      }
    }
  }

  private def select(sc: SparkContext):Unit ={
    /*
    sc: SparkContext,
    getConnection: () => Connection,
    sql: String,
    lowerBound: Long,
    upperBound: Long,
    numPartitions: Int,
    mapRow: (ResultSet) => T = JdbcRDD.resultSetToObjectArray _)*/
    val selectJDBC: JdbcRDD[Unit] = new JdbcRDD(
      sc,
      getConnection,
      "select name,age from rddtable where id>=? && id<=?",
      1,
      3,
      2,
      result
    )
    selectJDBC.collect()
  }
  private def result: ResultSet => Unit = {
    (rs) => {
      println(rs.getString(1) + "," + rs.getInt(2))
    }
  }

  private def getConnection: () => Connection = {
    () => {
      Class.forName(driver)
      DriverManager.getConnection(url, user, password)
    }
  }
  private def getConnections: Connection = {
    val connection1: Connection = {
      Class.forName(driver)
      val connection: Connection = DriverManager.getConnection(url, user, password)
      connection
    }
    connection1
  }
  private def initSparkContext: SparkContext = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("MysqlDemo")
    val sc: SparkContext = new SparkContext(conf)
    sc
  }
}
