import $file.dependencies

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.PostgresProfile.api._

val database = Database.forURL(
  "jdbc:postgresql://localhost/",
  user = "postgres",
  driver = "slick.jdbc.PostgresProfile$"
)

def await[T](ft: Future[T]): T = Await.result(ft, 2.seconds)

def run[T](action: DBIO[T]): Future[T] = database.run(action)

def exec[T](action: DBIO[T]): T = await(database.run(action))
