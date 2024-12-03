## Group Activity on Concurrency

In this group activity, we'll experiment with Scala futures. 

The `WebServiceClient` is a small wrapper for creating a number of concurrent web requests and observing progress from the client's perspective.   

For more details, see group activity 4 in Sakai.

```
❯ sbt console
...
scala> val c = new WebServiceClient
submitted request
defined direct results
defined other futures
ready for observation
val c: WebServiceClient = WebServiceClient@4d1ca627

scala> c.printProgress()
186
186

scala> c.printResults()
Some(Failure(java.net.ConnectException: handshake timed out after 10000ms))
None

scala> c.printProgress()
2293
2293

scala> all requests completed
108

scala> c.printResults()
Some(Failure(java.net.ConnectException: handshake timed out after 10000ms))
Some(Success(92))

scala> c.printProgress()
5001
5001
```