package com.rhyskeepence.endpoint

import org.http4s.HttpService
import org.http4s.dsl._
import org.scalatest.{FlatSpec, Matchers}

class EndpointSpec extends FlatSpec with Matchers {
  it should "be able to extract a HttpService from the Endpoints" in {
    class A extends Endpoint {
      def this(bar: String) {
        this()
      }

      val ep1 = HttpService {
        case GET -> Root / "foo" => Ok()
      }

      val ep2 = HttpService {
        case GET -> Root / "bar" => Ok()
      }
    }

    object B extends Endpoint {
      val ep1 = HttpService {
        case GET -> Root / "foos" => Ok()
      }

      def ep2 = HttpService {
        case GET -> Root / "bars" => Ok()
      }
    }

    class C extends Endpoint {
      def this(foo: String) {
        this()
      }

      def c1 = new A
      val c2 = B
    }

    toEndpoint(new C)
  }

  it should "be able to extract a HttpService from a case class Endpoint" in {
    case class A(b: B) extends Endpoint

    class B extends Endpoint {
      val ep = HttpService {
        case GET -> Root / "foo" => Ok()
      }
    }

    toEndpoint(new A(new B))
  }

//  it should "fail with a compiler error when no HttpService is found" in {
//    object A extends Endpoint
//    toEndpoint(A)
//  }
}