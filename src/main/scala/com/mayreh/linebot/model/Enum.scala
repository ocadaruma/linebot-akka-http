package com.mayreh.linebot.model

trait Enum {
  type ValueType
  val value: ValueType
}
trait ShortEnum extends Enum { type ValueType = Short }
trait StringEnum extends Enum { type ValueType = String }

trait EnumSupport[A <: Enum] {
  val members: Set[A]
  final def valueOf(a: A#ValueType): Option[A] = members.find(_.value == a)
}
