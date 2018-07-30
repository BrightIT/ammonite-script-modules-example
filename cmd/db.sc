#!/usr/bin/env amm
import $exec.modules.predef
import $file.modules.{tables, local}

import scala.util.{Try, Success, Failure}

val schema = Seq(
  tables.employees.schema,
  tables.customers.schema,
  tables.orders.schema
).reduce(_ ++ _)

@main def reset(): Unit = exec {
  schema.drop.asTry >> schema.create
}

@main def init(): Unit = exec {
  schema.create
}

@main def summary(): Unit = exec {
  import slick.lifted.AbstractTable

  def tryCount[T <: AbstractTable[_]](q: TableQuery[T]): DBIO[Try[Int]] =
    q.length.result.asTry

  def show(t: Try[Any]): String = t match {
    case Success(a) => a.toString
    case Failure(e) => s"${e.getClass}: ${e.getMessage}"
  }

  for {
    employeeNumTry <- tryCount(tables.employees)
    customerNumTry <- tryCount(tables.customers)
    orderNumTry <- tryCount(tables.orders)
    highestOrderTry <-
      tables.orders.map(_.id).max.result.map(_ getOrElse 0).asTry
  } yield {
    println(s"Employee #:\t${show(employeeNumTry)}")
    println(s"Customer #:\t${show(customerNumTry)}")
    println(s"Order #:\t${show(orderNumTry)}")
    println(s"Last order #:\t${show(highestOrderTry)}")
  }
}

@main def upload(): Unit = exec {
  val localEmployees = local.Employees.load()
  val localCustomers = local.Customers.load()
  val localOrders = local.Orders.load()

  for {
    employeeCount <- tables.employees ++= localEmployees
    customerCount <- tables.customers ++= localCustomers
    orderCount <- tables.orders ++= localOrders
  } yield {
    println("Inserted:")
    println(s"\t${employeeCount getOrElse 0} employees")
    println(s"\t${customerCount getOrElse 0} customers")
    println(s"\t${orderCount getOrElse 0} orders")
  }
}

@main def download(): Unit = exec {
  for {
    employees <- tables.employees.result
    customers <- tables.customers.result
    orders <- tables.orders.result
  } yield {
    local.Employees.store(employees)
    local.Customers.store(customers)
    local.Orders.store(orders)
  }
}
