package com.github.aborg0.rxala

sealed trait Cardinality

trait UpperBounded extends Cardinality

trait LowerBounded extends Cardinality

sealed trait Empty extends UpperBounded with AtMostOne

final class CanOnlyFail private() extends Empty

final class EmptyCompletion private() extends Empty

trait NonEmpty extends LowerBounded

trait AtMostOne extends UpperBounded

final class ExactlyOne private() extends NonEmpty with AtMostOne