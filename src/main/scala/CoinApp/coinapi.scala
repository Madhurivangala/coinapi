package CoinApp
import CoinApp.CaseClassesandMethods._
import org.apache.spark
import scalaj.http._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql._
import pureconfig._
import pureconfig.generic.auto._
import play.api.libs.json.Reads.of
import play.api.libs.json._
import org.apache.spark.sql.functions._
import java.util.Properties

import org.apache.spark.sql.types.{StringType, StructField, StructType}

object coinapi extends App {

  val spark: SparkSession = SparkSession.
    builder().
    master("local[2]").
    enableHiveSupport().
    enableHiveSupport().
    getOrCreate()

  import spark.implicits._

  val config = ConfigSource.file("application.conf").loadOrThrow[ConfigClass]

  val jdbcProps = new Properties()
  jdbcProps.setProperty("Driver", config.sqlparams.driver)
  jdbcProps.setProperty("ConnectionString", config.sqlparams.url)
  jdbcProps.setProperty("user", config.sqlparams.user)
  jdbcProps.setProperty("password", config.sqlparams.password)


//get all the assets(cryptodetails) from API.
val allassets = requestAPI(config.api.assetendpoint, config.api.key)
allassets._2 match{
  case 200 => Json.parse(allassets._1).as[Seq[Asset]].toDF().write.mode("overwrite").jdbc(config.sqlparams.url,s"""public.asset""",jdbcProps)
}

//get all the assets(cryptodetails) from API.

  val gethistData = for (elem <- config.chosencur.toList) {
    for(elem2 <- config.chosencur.toList){
   val gethistData = requestAPI(config.api.histendpoint.replace("{asset_id_base}",elem).replace("{asset_id_quote}",elem2), config.api.key)
    gethistData._2 match{
    case 200 => Json.parse(gethistData._1).as[Seq[HistData]].toDF().write.mode("append").jdbc(config.sqlparams.url,s"""public.HistoricalData""",jdbcProps)
    case is4xx => ""
  }
  }
  }



}
