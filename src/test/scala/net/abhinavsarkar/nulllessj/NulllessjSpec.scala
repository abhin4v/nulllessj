package net.abhinavsarkar.nulllessj

import org.scalatest.WordSpec
import org.scalatest.junit.{ShouldMatchersForJUnit,JUnitRunner}
import org.junit.runner.RunWith
import java.util.concurrent.Callable
import java.lang.String

@RunWith(classOf[JUnitRunner])
class NulllessjSpec extends WordSpec with ShouldMatchersForJUnit {

  "Nothing" should {
    "return true for isNothing " in {
      (new Nothing() isNothing) should be (true)
    }
    "return false for isSomething " in {
      (new Nothing() isSomething) should be (false)
    }
    "have same hashcode for all instances " in {
      (new Nothing() hashCode) should be (new Nothing() hashCode)
    }
    "have all instances equal " in {
      (new Nothing) should be (new Nothing)
    }
  }
  "Nothing" when {
    "getting value from it" should {
      "throw NothingException " in {
        evaluating { (new Nothing) get } should produce [NothingException]
      }
    }
    "getting value from it with default value" should {
      "return the default value " in {
        ((new Nothing) getOrElse "a") should be ("a")
      }
    }
  }

  "Just" should {
    "return false for isNothing " in {
      (new Just(new {}) isNothing) should be (false)
    }
    "return true for isSomething " in {
      (new Just(new {}) isSomething) should be (true)
    }
  }
  "Just" when {
    "created with a null value" should {
      "throw NothingException " in {
        evaluating { new Just(null) } should produce [NothingException]
      }
    }
    "getting value from it" should {
      "return the wrapped value " in {
        (new Just("a") get) should be ("a")
      }
    }
    "getting value from it with default value" should {
      "return the wrapped value " in {
        (new Just("a") getOrElse "b") should be ("a")
      }
    }
  }

  val callable = new Callable[String] { def call() = "abc" }
  val runnable = new Runnable { def run { throw new RuntimeException } }
  val contextualCallable = new ContextualCallable[Object, String] {
    def call() =  "abc"
  }
  val contextualRunnable = new ContextualRunnable[Object] {
    def run { throw new RuntimeException }
  }

  "Maybe" when {
    "created with a null value" should {
      "return a Nothing " in {
        Maybe.maybe(null).getClass should be (classOf[Nothing[_]])
      }
      "have 'NONE' kind " in {
        Maybe.maybe(null).kind should be (Maybe.MaybeKind.NONE)
      }
      "call the callable in 'callIfNothing' method " in {
        Maybe.maybe(null).callIfNothing(callable).get should be ("abc")
      }
      "run the runnable in 'runIfNothing' method " in {
        evaluating {
          Maybe.maybe(null).runIfNothing(runnable)} should
          produce [RuntimeException]
      }
      "not call the callable in 'callIfSomething' method " in {
        evaluating { Maybe.maybe(null)
          .callIfSomething(contextualCallable).get } should
          produce [NothingException]
      }
      "not run the runnable in 'runIfSomething' method " in {
        Maybe.maybe(null).runIfSomething(contextualRunnable)
      }
    }
    "created with a non-null value" should {
      "return a Just " in {
        Maybe.maybe(new {}).getClass should be (classOf[Just[_]])
      }
      "have 'SOME' kind " in {
        Maybe.maybe(new {}).kind should be (Maybe.MaybeKind.SOME)
      }
      "not call the callable in 'callIfNothing' method " in {
        evaluating {
          Maybe.maybe(new {}).callIfNothing(callable).get } should 
          produce [NothingException]
      }
      "not run the runnable in 'runIfNothing' method " in {
        Maybe.maybe(new {}).runIfNothing(runnable)
      }
      "call the callable in 'callIfSomething' method " in {
        Maybe.maybe(new {})
          .callIfSomething(contextualCallable).get should be ("abc")
      }
      "run the runnable in 'runIfSomething' method " in {
        evaluating { Maybe.maybe(new {})
          .runIfSomething(contextualRunnable) } should
          produce [RuntimeException]
      }
    }
  }
  
}
