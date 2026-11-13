## Group Activity on Concurrency using Scala Futures

In this group activity, we'll experiment with Scala futures. 

The `WebServiceClient` is a small wrapper for creating a number of concurrent web requests and observing progress from the client's perspective.   

For more details, see group activity 4 in Sakai.

```
❯ sbt console
...
scala> val c = new WebServiceClient(2, 1000000, 1001000)
using service https://primenumbers-http4s-scala-latest.onrender.com/
submitted requests
defined direct results
defined additional futures
ready for observation
val c: WebServiceClient = WebServiceClient@4d1ca627

scala> c.printProgress()
186
186

scala> c.printResults()
Some(Failure(java.net.ConnectException: handshake timed out after 10000ms))
None

scala> c.printProgress()
793
793

scala> all requests completed
75
75

scala> c.printResults()
Some(Failure(java.net.ConnectException: handshake timed out after 10000ms))
Some(Success(75))

scala> c.printProgress()
989
1001
```