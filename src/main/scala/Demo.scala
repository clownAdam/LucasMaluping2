import org.apache.spark.{SparkConf, SparkContext}

object Demo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Demo")
    val sc = new SparkContext(conf)
    val value = sc.parallelize(List(1, 2))
    val ints = value.collect()
    println(ints)
  }
}
