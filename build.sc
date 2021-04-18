import mill._
import mill.api.Loose
import mill.define.Target
import mill.scalajslib.ScalaJSModule
import mill.scalalib._
import mill.scalalib.api.CompilationResult

val projectScalaVersion = "3.0.0-RC2"

//noinspection ScalaFileName
object webApp extends JavaModule {

  object frontend extends ScalaJSModule {
    override def scalaVersion = projectScalaVersion
    override def scalaJSVersion = "1.5.1"

    // ("org.scala-js" %%% "scalajs-dom" % "1.1.0").cross(CrossVersion.for3Use2_13)
    override def ivyDeps: Target[Loose.Agg[Dep]] = super.ivyDeps() ++ Agg(
      ivy"org.scala-js::scalajs-dom::1.1.0".withDottyCompat(projectScalaVersion)
    )
    // TODO: is it possible to integrate https://github.com/lihaoyi/workbench
  }

  object backend extends MyScalaModule {

    override def ivyDeps: Target[Loose.Agg[Dep]] = super.ivyDeps() ++ Agg(
      ivy"com.lihaoyi::cask:0.7.9",
      ivy"com.lihaoyi::scalatags:0.9.4".withDottyCompat(projectScalaVersion),
    )

    override def compile: T[CompilationResult] = T {
      frontend.fastOpt.apply()
      super.compile.apply()
    }
  }

  object shared extends MyScalaModule {

  }

  trait MyScalaModule extends ScalaModule {
    override def scalaVersion = projectScalaVersion
  }

}

