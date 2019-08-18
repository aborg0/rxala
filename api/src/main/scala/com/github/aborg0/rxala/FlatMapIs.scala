package com.github.aborg0.rxala

sealed trait FlatMapIs

object FlatMapIs {

  final case object ConcatMap extends FlatMapIs

  final case object MergeMap extends FlatMapIs

  final case object SwitchMap extends FlatMapIs

}