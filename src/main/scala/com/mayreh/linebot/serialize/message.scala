package com.mayreh.linebot.serialize

import com.mayreh.linebot.model._
import spray.json.{JsObject, JsNumber, JsValue, RootJsonFormat}
import spray.json.DefaultJsonProtocol._

object message {
  implicit lazy val contentType: RootJsonFormat[ContentType] = new RootJsonFormat[ContentType] {
    def read(json: JsValue): ContentType = ContentType.valueOf(json.convertTo[Short]).get
    def write(obj: ContentType): JsValue = JsNumber(obj.value)
  }

  implicit lazy val opType: RootJsonFormat[OpType] = new RootJsonFormat[OpType] {
    def read(json: JsValue): OpType = OpType.valueOf(json.convertTo[Short]).get
    def write(obj: OpType): JsValue = JsNumber(obj.value)
  }

  private[this] lazy val contactMetadata: RootJsonFormat[ContentMetadata.ContactMetadata] = jsonFormat2(ContentMetadata.ContactMetadata)
  private[this] lazy val stickerMetadata: RootJsonFormat[ContentMetadata.StickerMetadata] = jsonFormat4(ContentMetadata.StickerMetadata)
  private[this] lazy val audioMetadata: RootJsonFormat[ContentMetadata.AudioMetadata] = jsonFormat1(ContentMetadata.AudioMetadata)
  private[this] lazy val messageContent: RootJsonFormat[Content.MessageContent] = jsonFormat11(Content.MessageContent)
  private[this] lazy val operationContent: RootJsonFormat[Content.OperationContent] = jsonFormat3(Content.OperationContent)

  implicit lazy val contentMetadata: RootJsonFormat[ContentMetadata] = new RootJsonFormat[ContentMetadata] {
    def read(json: JsValue): ContentMetadata = json match {
      case js: JsObject if js.fields.contains("mid") => contactMetadata.read(json)
      case js: JsObject if js.fields.contains("AUDLEN") => audioMetadata.read(json)
      case _ => stickerMetadata.read(json)
    }
    def write(obj: ContentMetadata): JsValue = obj match {
      case d: ContentMetadata.StickerMetadata => stickerMetadata.write(d)
      case d: ContentMetadata.ContactMetadata => contactMetadata.write(d)
      case d: ContentMetadata.AudioMetadata => audioMetadata.write(d)
    }
  }

  implicit lazy val content: RootJsonFormat[Content] = new RootJsonFormat[Content] {
    def read(json: JsValue): Content = json match {
      case js: JsObject if js.fields.contains("opType") => operationContent.read(json)
      case _ => messageContent.read(json)
    }
    def write(obj: Content): JsValue = obj match {
      case d: Content.OperationContent => operationContent.write(d)
      case d: Content.MessageContent => messageContent.write(d)
    }
  }

  implicit lazy val location: RootJsonFormat[Location] = jsonFormat4(Location)
  implicit lazy val messageResult: RootJsonFormat[MessageResult] = jsonFormat8(MessageResult)
  implicit lazy val message: RootJsonFormat[Message] = jsonFormat1(Message)
  implicit lazy val messageRequest: RootJsonFormat[MessageRequest] = jsonFormat4(MessageRequest)
}
