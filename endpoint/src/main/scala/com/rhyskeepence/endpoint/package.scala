package com.rhyskeepence

import com.rhyskeepence.endpoint.internal.EndpointMacro
import org.http4s.HttpService

import scala.language.experimental.macros

package object endpoint {
  def toEndpoint[T <: Endpoint](cnt: T): HttpService = macro EndpointMacro.toEndpoint[T]
}
