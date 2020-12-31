name := "rxala"

version := "0.1"

//scalaVersion := "2.12.8"

scalaVersion := "2.13.4"

crossScalaVersions := Seq("2.12.12", "2.13.4")

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.0.0"

enablePlugins(Example)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
lazy val root = project.in(file("."))
  .aggregate(api, wrapper, impl)
  //  .dependsOn(api, impl)
  .settings(moduleName := "rxala")

lazy val api = project.in(file("api")).settings(moduleName := "api")

lazy val wrapper = project.in(file("wrapper")).settings(moduleName := "wrapper").dependsOn(api)

lazy val impl = project.in(file("impl")).settings(moduleName := "impl",
  libraryDependencies += "io.reactivex.rxjava3" % "rxjava" % "3.0.9",
  libraryDependencies += "io.projectreactor" % "reactor-core" % "3.3.0.RELEASE",
  libraryDependencies += "io.projectreactor" % "reactor-test" % "3.3.0.RELEASE" % "test",
  libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.0",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  libraryDependencies += "org.junit.jupiter" % "junit-jupiter" % "5.5.1" % Test,
  //  libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"
).dependsOn(wrapper)