package home.jhenning.akkapi;

import static akka.pattern.Patterns.ask;

import akka.actor.*;
import akka.dispatch.Await;
import akka.dispatch.Future;
import akka.util.Duration;
import akka.util.Timeout;
import home.jhenning.akkapi.actors.Master;
import home.jhenning.akkapi.messages.Calculate;
import home.jhenning.akkapi.messages.PiApproximation;

import java.util.concurrent.TimeUnit;


/**
 * Actor based PI calculation using Akka. Main class, starts the calculation of PI. Based on the getting started guide
 * on the Typesafe website (http://typesafe.com/resources/tutorials/getting-started-with-akka-java.html).
 *
 * This version has been improved to use Futures instead of direct Actor-messages to return the result. One of the
 * advantages of this approach is, that the Master worker is now stateless.
 */
public class Pi {

  public static void main(String[] args) throws Exception {
    Pi pi = new Pi();
    pi.calculate(4, 10000, 10000);
  }

  public void calculate(
      final int nrOfWorkers,
      final int nrOfElements,
      final int nrOfMessages) throws Exception {
    final ActorSystem system = ActorSystem.create("PiSystem");
    final ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
      @Override
      public Actor create() throws Exception {
        return new Master(nrOfWorkers, nrOfMessages, nrOfElements);
      }
    }), "master");

    Timeout t = new Timeout(Duration.create(1, TimeUnit.SECONDS));
    Future<Object> f = ask(master, new Calculate(), t);
    PiApproximation approx = (PiApproximation) Await.result(f, t.duration());

    System.out.println(String.format("\n\tPI Approximation: " +
        "\t\t%s\n\tCalculation time: \t%s",
        approx.getPi(),
        approx.getDuration()
    ));

    system.shutdown();
  }
}
