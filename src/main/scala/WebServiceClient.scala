import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.sys.process.stderr
import scala.util.Success

val system = ActorSystem("wsplay-example")
given ActorSystem = system
val wsClient = StandaloneAhcWSClient()

val urls = (1500450000 to 1500455000).map: n =>
  "http://linode5.cs.luc.edu:8080/" + n
  
//  "https://primechecker.azurewebsites.net/api/isPrime?number=" + n

class WebServiceClient:

  // Seq[Future[...]]
  val requests = urls.map(u => wsClient.url(u).get())

  System.err.nn.println("submitted request")

  val results = requests.map: f =>
    f.map: r =>
      r.status == 200
  val resultsFailsafe = results.map: f =>
    f.recover: ex =>
      false

  System.err.nn.println("defined direct results")

  // Future[Seq[...]]
  val f1 = Future.sequence(results)
  val f2 = f1.map(r => r.count(identity))

  // how are these two different?
  val f3 = Future.sequence(resultsFailsafe)
  val f4 = f3.map(r => r.count(identity))

  System.err.nn.println("defined other futures")
  System.err.nn.println("ready for observation")

  // *** section 3 ***

  // print final result
  f2.foreach(println)
  f4.foreach(println)

  f4.foreach(_ => System.err.nn.println("all requests completed"))

  // *** section 4 ***

  // meanwhile, can check repeatedly how many requests have completed
  def printProgress() =
    println(results.count(f => f.isCompleted))
    println(resultsFailsafe.count(f => f.isCompleted))

  // you can also (repeatedly) print the wrapped result in the foreground
  def printResults() =
    println(f2.value)
    println(f4.value)
