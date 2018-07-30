import $file.data

import java.nio.file.{Files, Paths, Path}
import scala.io.Source

import upickle.{default => upickled}

class LocalData[T: upickled.ReadWriter](val path: Path) {
  def load(): T =
    upickled.read[T](Source.fromFile(path.toFile).mkString)

  def store(data: T): Unit =
    Files.write(path, upickled.write(data, indent = 4).getBytes("UTF-8"))
}

object Employees extends LocalData[Seq[data.Employee]](
  path = Paths get "data/employees.json"
)

object Customers extends LocalData[Seq[data.Customer]](
  path = Paths get "data/customers.json"
)

object Orders extends LocalData[Seq[data.Order]](
  path = Paths get "data/orders.json"
)
