#!/usr/bin/env amm

import $file.modules.dependencies

@main def main() = {
  val pom =
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

      <modelVersion>4.0.0</modelVersion>

      <groupId>ammonite-command-demo</groupId>
      <artifactId>ammonite-command-demo</artifactId>
      <version>1.0.0-SNAPSHOT</version>

      <dependencies>
      {
        dependencies.dependencies.map { d =>
          <dependency>
            <groupId>{d.module.organization}</groupId>
            <artifactId>{d.module.name}</artifactId>
            <version>{d.version}</version>
          </dependency>
        }
      }
      </dependencies>

      <repositories>
      {
        val credentials = coursierplusauth.credentials
        coursierplusauth.repofile.map { r =>
          val (id, url) = r match {
            case credentials.PublicRepo(url) => (url, url)
            case credentials.PrivateRepo(id, pre, post) => (id, pre + post)
          }
          <repository>
            <id>{id}</id>
            <url>{url}</url>
          </repository>
        }
      }
      </repositories>

    </project>

  val path = "cmd/pom.xml"
  scala.xml.XML.save(path, pom)
  println(s"Generated and saved POM to: $path")
}
