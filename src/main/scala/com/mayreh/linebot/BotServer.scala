package com.mayreh.linebot

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.mayreh.linebot.model.{Content, Message, MessageRequest}
import com.mayreh.linebot.serialize.message._
import spray.json._

import scala.concurrent.ExecutionContext

object BotServer {
  val requestBase = HttpRequest(
    uri = "https://trialbot-api.line.me/v1/events",
    headers = List(
      RawHeader("X-Line-ChannelID", sys.env("LINE_CHANNEL_ID")),
      RawHeader("X-Line-ChannelSecret", sys.env("LINE_CHANNEL_SECRET")),
      RawHeader("X-Line-Trusted-User-With-ACL", sys.env("LINE_MID"))
    ),
    method = HttpMethods.POST
  )

  val healthcheck = path("") {
    get {
      println("healthcheck called.")
      complete(StatusCodes.OK, "It works!")
    }
  }

  def callback(implicit system: ActorSystem,
    materializer: ActorMaterializer,
    ec: ExecutionContext) = path("callback") {
    post {
      extractRequest { rawRequest =>
        println(rawRequest)

        entity(as[Message]) { message =>
          for {
            result <- message.result.headOption
          } {
            val messageJson = MessageRequest(
              to = result.content
                .collect { case m: Content.MessageContent => m.from }
                .flatten
                .toList,
              content = result.content).toJson

            println(messageJson.prettyPrint)
            val request = requestBase.withEntity(ContentTypes.`application/json`, messageJson.compactPrint)

            Http().singleRequest(request).foreach(println)
          }
          complete(StatusCodes.OK)
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val port = sys.props.getOrElse("http.port", "80").toInt

    val route = healthcheck ~ callback

    Http(system).bindAndHandle(route, "0.0.0.0", port)
  }
}
