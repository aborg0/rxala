package com.github.aborg0.rxala

sealed trait Temperature

final case class Cold private() extends Temperature

final case class Hot private() extends Temperature
