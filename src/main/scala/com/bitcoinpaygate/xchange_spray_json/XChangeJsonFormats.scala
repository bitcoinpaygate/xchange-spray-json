package com.bitcoinpaygate.xchange_spray_json

import java.time.{LocalDateTime, ZoneOffset}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.knowm.xchange.currency.{Currency, CurrencyPair}
import org.knowm.xchange.dto.Order.OrderType
import org.knowm.xchange.dto.account._
import org.knowm.xchange.dto.trade.{LimitOrder, UserTrade, UserTrades}
import spray.json._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

trait XChangeJsonFormats extends SprayJsonSupport with DefaultJsonProtocol {
  private case class CurrencyBalance(currency: Option[Currency], balance: Option[Balance])
  private case class AccountWallet(account: Option[String], wallet: Option[Wallet])

  private def currencyBalanceMapToScala(map: java.util.Map[Currency, Balance]): List[CurrencyBalance] = {
    map.asScala.map {
      case (currency, balance) =>
        CurrencyBalance(Option(currency), Option(balance))
    }.toList
  }

  private def accountWalletMapToScala(map: java.util.Map[String, Wallet]): List[AccountWallet] = {
    map.asScala.map {
      case (id, wallet) =>
        AccountWallet(Option(id), Option(wallet))
    }.toList
  }

  private def removeJsNulls(jsObject: JsObject) = {
    JsObject(jsObject.fields.filter {
      case (_, v) => v != JsNull
    })
  }

  private def javaBigDecimalToJson(bd: java.math.BigDecimal): JsValue = {
    Option(bd).map(BigDecimal(_)).toJson
  }

  private def stringToCurrencyPair(s: String): CurrencyPair = {
    Try {
      val x = s.split("/")
      new CurrencyPair(x(0), x(1))
    } match {
      case Success(parsed) => parsed
      case Failure(_)      => deserializationError("no parser for CurrencyPair defined " + s)
    }
  }

  /////////////////// XChange (un)unmarshallers

  implicit val CurrencyFormat = new JsonFormat[Currency] {
    override def write(x: Currency) = {
      JsString(x.getCurrencyCode)
    }

    override def read(value: JsValue): Currency = value match {
      case JsString(s) => new Currency(s)
      case x           => deserializationError("no parser for Currency defined " + x)
    }
  }

  implicit val CurrencyPairFormat = new JsonFormat[CurrencyPair] {
    override def write(x: CurrencyPair) = {
      JsString(s"${x.base}/${x.counter}")
    }

    override def read(value: JsValue): CurrencyPair = value match {
      case JsString(s) => stringToCurrencyPair(s)
      case x           => deserializationError("no parser for CurrencyPair defined " + x)
    }
  }

  implicit val BalanceFormat = new JsonFormat[Balance] {
    override def write(x: Balance) = {
      removeJsNulls(
        JsObject(
          Map(
            "total" -> javaBigDecimalToJson(x.getTotal),
            "available" -> javaBigDecimalToJson(x.getAvailable),
            "frozen" -> javaBigDecimalToJson(x.getFrozen),
            "loaned" -> javaBigDecimalToJson(x.getLoaned),
            "borrowed" -> javaBigDecimalToJson(x.getBorrowed),
            "withdrawing" -> javaBigDecimalToJson(x.getWithdrawing),
            "depositing" -> javaBigDecimalToJson(x.getDepositing),
            "availableForWithdrawal" -> javaBigDecimalToJson(x.getAvailableForWithdrawal))))
    }

    override def read(value: JsValue): Balance = value match {
      case x => deserializationError("no parser for Balance defined " + x)
    }
  }

  private implicit val CurrencyBalanceFormat = jsonFormat2(CurrencyBalance)

  implicit val WalletFormat = new JsonFormat[Wallet] {
    override def write(x: Wallet) = {
      removeJsNulls(
        JsObject(
          Map(
            "id" -> Option(x.getId).toJson,
            "name" -> Option(x.getName).toJson,
            "balances" -> currencyBalanceMapToScala(x.getBalances).toJson)))
    }

    override def read(value: JsValue): Wallet = value match {
      case x => deserializationError("no parser for Wallet defined " + x)
    }
  }

  private implicit val AccountWalletFormat = jsonFormat2(AccountWallet)

  implicit val AccountInfoFormat = new JsonFormat[AccountInfo] {
    override def write(x: AccountInfo) = {
      removeJsNulls(
        JsObject(
          Map(
            "username" -> Option(x.getUsername).toJson,
            "tradingFee" -> javaBigDecimalToJson(x.getTradingFee),
            "wallets" -> Option(accountWalletMapToScala(x.getWallets)).toJson)))
    }

    override def read(value: JsValue): AccountInfo = value match {
      case x => deserializationError("no parser for AccountInfo defined " + x)
    }
  }

  implicit val OrderTypeFormat = new JsonFormat[OrderType] {
    override def write(x: OrderType) = JsString(x.toString)

    override def read(value: JsValue): OrderType = value match {
      case JsString(s) => OrderType.valueOf(s)
      case x           => deserializationError("no parser for OrderType defined " + x)
    }
  }

  implicit val JavaDateFormat = new JsonFormat[java.util.Date] {
    override def write(x: java.util.Date) = JsString(LocalDateTime.ofInstant(x.toInstant, ZoneOffset.UTC).toString)

    override def read(value: JsValue): java.util.Date = value match {
      case x => deserializationError("no parser for java.util.Date defined " + x)
    }
  }

  implicit val LimitOrderFormat = new JsonFormat[LimitOrder] {
    override def write(x: LimitOrder) = removeJsNulls(JsObject(Map(
      "price" -> javaBigDecimalToJson(x.getLimitPrice).toJson,
      "amount" -> javaBigDecimalToJson(x.getOriginalAmount).toJson,
      "type" -> x.getType.toJson,
      "id" -> Option(x.getId).toJson,
      "time" -> Option(x.getTimestamp).toJson)))

    override def read(value: JsValue): LimitOrder = value match {
      case x => deserializationError("no parser for LimitOrder defined " + x)
    }
  }

  implicit val UserTradeFormat = new JsonFormat[UserTrade] {
    override def write(x: UserTrade) = removeJsNulls(JsObject(Map(
      "orderId" -> Option(x.getOrderId).toJson,
      "feeAmount" -> javaBigDecimalToJson(x.getFeeAmount).toJson,
      "feeCurrency" -> Option(x.getFeeCurrency).toJson,
      "currencyPair" -> Option(x.getCurrencyPair).toJson,
      "type" -> Option(x.getType).toJson,
      "time" -> Option(x.getTimestamp).toJson,
      "price" -> javaBigDecimalToJson(x.getPrice).toJson,
      "amount" -> javaBigDecimalToJson(x.getOriginalAmount).toJson)))

    override def read(value: JsValue): UserTrade = value match {
      case x => deserializationError("no parser for UserTrade defined " + x)
    }
  }

  implicit val UserTradesFormat = new JsonFormat[UserTrades] {
    override def write(x: UserTrades) = removeJsNulls(JsObject(Map(
      "trades" -> Option(x.getUserTrades.asScala.toList).toJson)))

    override def read(value: JsValue): UserTrades = value match {
      case x => deserializationError("no parser for UserTrades defined " + x)
    }
  }
}
