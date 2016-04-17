package com.mayreh.linebot.model

sealed abstract class ContentType(val value: Short) extends ShortEnum
object ContentType extends EnumSupport[ContentType] {
  case object Text extends ContentType(1)
  case object Image extends ContentType(2)
  case object Video extends ContentType(3)
  case object Audio extends ContentType(4)
  case object Location extends ContentType(7)
  case object Sticker extends ContentType(8)
  case object Contact extends ContentType(10)

  val members: Set[ContentType] = Set(Text, Image, Video, Audio, Location, Sticker, Contact)
}

sealed abstract class OpType(val value: Short) extends ShortEnum
object OpType extends EnumSupport[OpType] {
  case object AddedAsFriend extends OpType(4)
  case object Blocked extends OpType(8)

  val members: Set[OpType] = Set(AddedAsFriend, Blocked)
}

sealed abstract class ContentMetadata
object ContentMetadata {
  case class StickerMetadata(
    STKPKGID: Option[String],
    STKID: Option[String],
    STKVER: Option[String],
    STKTXT: Option[String]
  ) extends ContentMetadata

  case class ContactMetadata(
    mid: Option[String],
    displayName: Option[String]
  ) extends ContentMetadata

  case class AudioMetadata(
    AUDLEN: Option[String]
  ) extends ContentMetadata
}

case class Location(
  title: Option[String],
  address: Option[String],
  latitude: Option[Double],
  longitude: Option[Double]
)

sealed abstract class Content
object Content {
  case class MessageContent(
    id: Option[String],
    contentType: Option[ContentType],
    from: Option[String],
    createdTime: Option[Long],
    to: List[String],
    toType: Option[Short],
    contentMetadata: Option[ContentMetadata],
    text: Option[String],
    originalContentUrl: Option[String],
    previewImageUrl: Option[String],
    location: Option[Location]
  ) extends Content

  case class OperationContent(
    revision: Int,
    opType: OpType,
    params: List[Option[String]]
  ) extends Content
}

case class MessageResult(
  from: Option[String],
  fromChannel: Option[Long],
  to: List[String],
  toChannel: Option[Long],
  createdTime: Option[Long],
  eventType: Option[String],
  id: Option[String],
  content: Option[Content])

case class Message(
  result: List[MessageResult]
)

case class MessageRequest(
  to: List[String],
  toChannel: Long = 1383378250,
  eventType: String = "138311608800106203",
  content: Option[Content]
)
