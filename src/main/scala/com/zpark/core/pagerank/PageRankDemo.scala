package com.zpark.core.pagerank

import java.io

import com.zpark.core.partition.DomainNamePartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object PageRankDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("PageRankDemo")
    val sc = new SparkContext(conf)
    val linkstemp: RDD[(String, List[String])] = sc.parallelize(List(
      ("http://www.baidu.com/aaa",List("http://www.sina.com","http://www.youdao.com")),
      ("http://www.sina.com",List("http://www.baidu.com/aaa","http://www.youdao.com")),
      ("http://www.youdao.com",List("http://www.baidu.com/aaa","http://www.sina.com","http://www.jd.com")),
      ("http://www.jd.com",List("http://www.youdao.com"))
    )).partitionBy(new HashPartitioner(100)).persist()

    val links: RDD[(String, List[String])] = linkstemp.partitionBy(new DomainNamePartitioner(100)).persist()
    var ranks: RDD[(String, Double)] = links.mapValues(v=>1.0)
    for(i<-0 until 10) {
      val joined: RDD[(String, (List[String], Double))] = links.join(ranks)
      val contributions: RDD[(String, Double)] = joined.flatMap {
        case (pageId, (linkss, rank)) => linkss.map(dest => (dest, rank / linkss.size))
      }
      ranks = contributions.reduceByKey((x, y) => x + y).mapValues(v => 0.15 + 0.85 * v)
    }
    ranks.foreach(println(_))
  }
}
