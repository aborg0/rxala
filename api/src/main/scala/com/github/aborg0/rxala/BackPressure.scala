package com.github.aborg0.rxala

import scala.languageFeature.higherKinds

sealed trait BackPressure {
  type SubscriptionType[Family]
}

final case class WithBackPressure private() extends BackPressure

final case class NoBackPressure private() extends BackPressure