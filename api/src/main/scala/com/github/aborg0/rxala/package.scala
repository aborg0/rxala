package com.github.aborg0

package object rxala {

  // region Type fun

  // From: https://gist.github.com/milessabin/c9f8befa932d98dcc7a4
  // Encoding for "A is not a subtype of B"
  trait <:!<[A, B]

  // Uses ambiguity to rule out the cases we're trying to exclude
  implicit def nsub[A, B]: A <:!< B = null

  implicit def nsubAmbig1[A, B >: A]: A <:!< B = null

  implicit def nsubAmbig2[A, B >: A]: A <:!< B = null

  // endregion

  // region Cardinality helpers

  sealed trait CardinalityFlatMapResult[C <: Cardinality, CI <: Cardinality] {
    type C <: Cardinality
  }

  private[rxala] trait CardinalityFlatMapResultLowPrio {
    implicit def anyAny: CardinalityFlatMapResult[Cardinality, Cardinality] = new CardinalityFlatMapResult[Cardinality, Cardinality] {
      type C <: Cardinality
    }
  }

  object CardinalityFlatMapResult extends CardinalityFlatMapResultLowPrio {
    implicit def atMostOneAtMostOne: CardinalityFlatMapResult[AtMostOne, AtMostOne] = new CardinalityFlatMapResult[AtMostOne, AtMostOne] {
      type C <: AtMostOne
    }

    implicit def oneOne: CardinalityFlatMapResult[ExactlyOne, ExactlyOne] = new CardinalityFlatMapResult[ExactlyOne, ExactlyOne] {
      type C <: ExactlyOne
    }

    implicit def emptyAny: CardinalityFlatMapResult[Empty, Cardinality] = new CardinalityFlatMapResult[Empty, Cardinality] {
      type C <: Empty
    }

    implicit def failAny: CardinalityFlatMapResult[CanOnlyFail, Cardinality] = new CardinalityFlatMapResult[CanOnlyFail, Cardinality] {
      type C <: CanOnlyFail
    }

    implicit def realEmptyAny: CardinalityFlatMapResult[EmptyCompletion, Cardinality] = new CardinalityFlatMapResult[EmptyCompletion, Cardinality] {
      type C <: EmptyCompletion
    }

    implicit def nonEmptyEmpty: CardinalityFlatMapResult[NonEmpty, Empty] = new CardinalityFlatMapResult[NonEmpty, Empty] {
      type C <: EmptyCompletion
    }

    implicit def nonEmptyFail: CardinalityFlatMapResult[NonEmpty, CanOnlyFail] = new CardinalityFlatMapResult[NonEmpty, CanOnlyFail] {
      type C <: CanOnlyFail
    }

    implicit def nonEmptyRealEmpty: CardinalityFlatMapResult[NonEmpty, EmptyCompletion] = new CardinalityFlatMapResult[NonEmpty, EmptyCompletion] {
      type C <: EmptyCompletion
    }
  }

  // endregion
}
