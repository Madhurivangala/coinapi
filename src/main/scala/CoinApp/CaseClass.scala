package CoinApp

import scalaj.http.Http
import play.api.libs.json._
import play.libs.Json._

object CaseClassesandMethods {

  case class SQLParams(url:String,
                       driver:String,
                       user:String,
                       password:String,
                       database:String)
  case class APIParams(
                        prodendpoint:String,
                        sandboxendpoint:String,
                        assetendpoint:String,
                        key:String,
                        symbolendpoint:String,
                      histendpoint:String
                      )
  case class ConfigClass(api:APIParams, sqlparams: SQLParams,chosencur:List[String])

  case class Asset( asset_id: String,
                             name: Option[String],
                             type_is_crypto: Option[Int],
                             data_start: Option[String],
                             data_end: Option[String],
                             data_quote_start: Option[String],
                             data_quote_end: Option[String],
                             data_orderbook_start: Option[String],
                             data_orderbook_end: Option[String],
                             data_trade_start: Option[String],
                             data_trade_end: Option[String],
                             data_symbols_count: Option[Double],
                             volume_1hrs_usd: Option[Double],
                             volume_1day_usd: Option[Double],
                             volume_1mth_usd: Option[Double],
                             price_usd: Option[Double]
                  ){
  }

  case class HistData(time_period_start: String,
  time_period_end: String,
  time_open: String,
  time_close: String,
  price_open: Double,
  price_high: Double,
  price_low: Double,
  price_close: Double,
  volume_traded: Double,
  trades_count:Int){}


  implicit val assetFormat = Json.format[Asset]
  implicit val HistFormat = Json.format[HistData]

  def requestAPI(api_path: String, key: String): Tuple2[String,Int]= {
    println("API Being acccessed "+ api_path)
    val dataResponse = Http(api_path).headers(Seq("X-CoinAPI-Key" -> key, "Accept" -> "application/json"))
      .method("Get").timeout(1000000, 1000000).asString
    //"Accept-Encoding" -> "deflate, gzip"

    dataResponse.code.toInt match {
      case 200 => (dataResponse.body, dataResponse.code)
      case 400 => (s"""Bad Request""", dataResponse.code)
      case 401 => (s"""API Key is wrong""", dataResponse.code)
      case 403 => (s"""Your API key doesnt’t have enough privileges to access this resource""", dataResponse.code)
      case 429 => (s"""Too many requests – You have exceeded your API key rate limits""", dataResponse.code)
      case 550 => (s"""No data – You requested specific single item that we don’t have at this moment""", dataResponse.code)
    }
  }
}
