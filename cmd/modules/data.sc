import upickle.{default => upickled}

case class Employee(
  id: Int,
  firstName: String,
  lastName: String
)

object Employee extends ((Int, String, String) => Employee) {
  implicit val rw: upickled.ReadWriter[Employee] = upickled.macroRW
}

case class Customer(
  id: String,
  companyName: String,
  contactName: String,
  contactTitle: String
)

object Customer extends ((String, String, String, String) => Customer) {
  implicit val rw: upickled.ReadWriter[Customer] = upickled.macroRW
}

case class Order(
  id: Int,
  employeeId: Int,
  customerId: String,
  shipName: String,
  shipCity: String
)

object Order extends ((Int, Int, String, String, String) => Order) {
  implicit val rw: upickled.ReadWriter[Order] = upickled.macroRW
}
