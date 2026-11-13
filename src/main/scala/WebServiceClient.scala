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

val services = Seq(
  "http://linode5.cs.luc.edu:8080/", // might be down
  "http://laufer-luc-oci.privatedns.org:8080/", // might not handle load
  "https://primenumbers-http4s-scala-latest.onrender.com/" // slow to wake up
)

class WebServicesTest:

  services.foreach: s =>
    val u = s + "6007"
    val f = wsClient.url(u).get()
    f.onComplete:
      case Success(r) =>
        System.err.nn.println(s"request to $u returned ${r.status}")
      case _ =>
        System.err.nn.println(s"request to $u failed")


// TODO choose a working service based on preceding test
class WebServiceClient(index: Int = 2, start: Int = 6001, end: Int = 7000):

  System.err.nn.println(s"using service ${services(index)}")

  val urls = (start to end).map: n =>
    services(index) + n

  System.err.nn.println(s"creating requests between ${start} and ${end} (${end - start + 1} requests)")

  // Seq[Future[...]]
  val requests = urls.map(u => wsClient.url(u).get())

  System.err.nn.println("submitted requests")

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

  System.err.nn.println("defined additional futures")
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
