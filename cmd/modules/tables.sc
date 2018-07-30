import $file.data

import slick.jdbc.PostgresProfile.api._

class EmployeesTable(tag: Tag)
extends Table[data.Employee](tag, "employees") {
  def id = column[Int]("employee_id", O.PrimaryKey)
  def firstName = column[String]("first_name")
  def lastName = column[String]("last_name")
  def * = (id, firstName, lastName).mapTo[data.Employee]
}

class CustomersTable(tag: Tag)
extends Table[data.Customer](tag, "customers") {
  def id = column[String]("customer_id", O.PrimaryKey, O.Length(5))
  def companyName = column[String]("company_name")
  def contactName = column[String]("contact_name")
  def contactTitle = column[String]("contact_title")
  def * = (id, companyName, contactName, contactTitle).mapTo[data.Customer]
}

class OrdersTable(tag: Tag)
extends Table[data.Order](tag, "orders") {
  def id = column[Int]("order_id", O.PrimaryKey)
  def employeeId = column[Int]("employee_id")
  def customerId = column[String]("customer_id", O.Length(5))
  def shipName = column[String]("ship_name")
  def shipCity = column[String]("ship_city")
  def * = (id, employeeId, customerId, shipName, shipCity).mapTo[data.Order]

  def fk_employee = foreignKey("fk_order_employee", employeeId, employees)(_.id)
  def fk_customer = foreignKey("fk_order_customer", customerId, customers)(_.id)
}

val employees = TableQuery[EmployeesTable]
val customers = TableQuery[CustomersTable]
val orders = TableQuery[OrdersTable]
