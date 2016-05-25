package com.rhyskeepence.endpoint.internal

import com.rhyskeepence.endpoint.Endpoint
import org.http4s.HttpService

import scala.reflect.macros.whitebox

private[rhyskeepence] object EndpointMacro {
  def toEndpoint[T <: Endpoint : c.WeakTypeTag](c: whitebox.Context)(cnt: c.Expr[T]): c.Expr[T] = {
    import c.universe._

    def symbolResultType(s: Symbol): Type = s match {
      case x if x.isMethod => x.asMethod.returnType
      case x => x.typeSignature
    }

    def filterApplicableTerms(t: c.universe.Type) =
      t.members
        .filter(x => x.isTerm && x.isPublic && !x.isSynthetic)
        .map(_.asTerm)
        .filter{
          case x if x.isMethod =>
            val ms = x.asMethod
            !ms.isConstructor && (ms.returnType <:< c.weakTypeOf[Endpoint] | ms.returnType <:< c.weakTypeOf[HttpService])
          case x if !x.isMethod => x.typeSignature <:< c.weakTypeOf[Endpoint] | x.typeSignature <:< c.weakTypeOf[HttpService]
          case _ => false
        }

    def extract(t: c.universe.Type, context: Tree): List[Tree] = {
      filterApplicableTerms(t).toList.flatMap{
        case x if symbolResultType(x) <:< c.weakTypeOf[Endpoint] => extract(symbolResultType(x), q"$context.${x.name.toTermName}")
        case x => List(q"$context.${x.name.toTermName}")
      }
    }

    val v = extract(c.weakTypeOf[T], q"$cnt").foldLeft(q"": Tree)((a, b) => a match {
      case q"" => q"$b"
      case x => q"Service.withFallback($b)($x)"
    }) match {
      case q"" => c.abort(c.enclosingPosition, "Endpoint must contain at least one Endpoint or HttpsService.")
      case x => x
    }

    val code = q"""
        import org.http4s._
        import com.rhyskeepence.endpoint._
        $v
      """

    c.Expr(code)
  }
}