#!/usr/bin/env amm

import $file.docker
import $file.db

def peskerForPositiveResponse(prompt: String): Boolean = {
  // Java/Scala Consoles fail in Cygwin
  val reader = org.jline.reader.LineReaderBuilder.builder().terminal(
    org.jline.terminal.TerminalBuilder.builder().build()
  ).build()

  @annotation.tailrec
  def readResponse(): Boolean = reader.readLine() match {
    case "yes" | "y" => true
    case "no" | "n" => false
    case null => throw new NullPointerException("input closed")
    case _ =>
      Console.print("Please type yes or no: ")
      readResponse()
  }

  Console.print(prompt)
  Console.print(" ")
  readResponse()
}

@main def main(): Unit = {
  if (docker.containerStatus() != docker.Status.Down) {
    docker.status() // describe the status
    println("This will bring docker container down and remove all data.")
    if (!peskerForPositiveResponse("Proceed?")) return
  }

  docker.restart()
  Thread.sleep(2000)
  db.init()
  db.upload()
}
