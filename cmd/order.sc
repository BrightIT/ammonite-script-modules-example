#!/usr/bin/env amm
import $exec.modules.predef
import $file.modules.{local, tables, data}

@main def compare(): Unit = {
  val localMax = local.Orders.load().iterator.map(_.id).max
  val remoteMax = exec {
    tables.orders.map(_.id).max.result
  }
  val diff = (remoteMax getOrElse 0) - localMax
  val desc =
    if (diff == 0) "up-to-date"
    else if (diff > 0) s"$diff order(s) behind"
    else s"${diff.abs} order(s) ahead"
  println(s"Last local order ($localMax) is $desc")
}

@main def last(): Unit = exec {
  for {
    lastOrder <- tables.orders.sortBy(_.id.desc).take(1).result.headOption
  } yield lastOrder match {
    case Some(order) =>
      println(s"Last order: $order")
    case None =>
      println("No orders.")
  }
}

@main def place(
  employee: Int,
  customer: String,
  shipName: String,
  shipCity: String
): Unit = exec {
  val action = for {
    lastOrder <- tables.orders.map(_.id).max.result
    _ <- tables.orders += data.Order(
      (lastOrder getOrElse 0) + 1,
      employee,
      customer,
      shipName,
      shipCity
    )
  } yield ()
  action.transactionally
}
