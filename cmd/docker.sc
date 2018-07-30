#!/usr/bin/env amm
import $exec.modules.predef

import ammonite.ops._
import ammonite.ops.ImplicitWd._

val container = "amm-demo-db"

sealed trait Status
object Status {
  case object Up extends Status
  case object Exists extends Status
  case object Down extends Status
}

def containerStatus(): Status = {
  val out: String = {
    import scala.sys.process._
    val sb = new StringBuilder
    s"docker inspect $container --format={{.State.Status}}" ! ProcessLogger(
      fout = sb ++= _,
      ferr = _ => ()
    )
    sb.result()
  }

  out.trim match {
    case "running" => Status.Up
    case "" => Status.Down
    case _ => Status.Exists
  }
}

@main def status(): Unit = {
  val description = containerStatus() match {
    case Status.Up => "is up and running"
    case Status.Exists => "exists, but is not running"
    case Status.Down => "is down"
  }

  println(s"Docker container $description")
}

@main def up(): Unit = {
  if (containerStatus == Status.Down) {
    %%docker("run", "--name=amm-demo-db", "-d", "-p", "127.0.0.1:5432:5432", "postgres:11")
  }
  status()
}

@main def down(): Unit = %%docker("rm", "-f", "amm-demo-db")

@main def restart(): Unit = {
  if (containerStatus() != Status.Down) down()
  up()
}
