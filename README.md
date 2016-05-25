# http4s Magic Endpoints

A little macro which allows the composition of HttpService values at compile time:

```scala
import org.http4s._
import org.http4s.dsl._
import com.rhyskeepence.endpoint._

object FooBarEndpoint extends Endpoint {
  val foo = HttpService {
    case GET -> Root / "foo" => Ok("foo")
  }

  val bar = HttpService {
    case GET -> Root / "bar" => Ok("bar")
  }
}

object WhizBangEndpoint extends Endpoint {
  val whiz = HttpService {
    case GET -> Root / "whiz" => Ok("whiz")
  }

  val bang = HttpService {
    case GET -> Root / "bang" => Ok("bang")
  }
}

object Endpoints extends Endpoint {
  val foobar = FooBarEndpoint
  val whizbang = WhizBangEndpoint
}


val service = toEndpoint(Endpoints)
```

The result is a HttpService which is a composition of all Endpoints, using Service.withFallback.
