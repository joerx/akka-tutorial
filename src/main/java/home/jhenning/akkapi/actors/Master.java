package home.jhenning.akkapi.actors;

import static akka.pattern.Patterns.ask;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Future;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnSuccess;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import akka.util.Timeout;
import home.jhenning.akkapi.messages.Calculate;
import home.jhenning.akkapi.messages.PiApproximation;
import home.jhenning.akkapi.messages.Result;
import home.jhenning.akkapi.messages.Work;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: jhenning
 * Date: 11/26/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Master extends UntypedActor {

  private final int nrOfMessages;
  private final int nrOfElements;
  private final long start = System.currentTimeMillis();

  private final ActorRef workerRouter;

  private final List<Future<Object>> listOfFutureResults;

  public Master(
      final int nrOfWorkers,
      int nrOfMessages,
      int nrOfElements)
  {
    this.nrOfMessages = nrOfMessages;
    this.nrOfElements = nrOfElements;

    workerRouter = this.getContext().actorOf(new Props(Worker.class).withRouter(
        new RoundRobinRouter(nrOfWorkers)), "workerRouter");

    listOfFutureResults = new ArrayList<Future<Object>>(nrOfElements);
  }


  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Calculate) {

      final ActorRef sender = getSender();

      for (int start = 0; start < nrOfMessages; start ++) {
        listOfFutureResults.add(
            ask(workerRouter, new Work(start, nrOfElements), new Timeout(Duration.create(5, TimeUnit.SECONDS)))
        );
      }
      Futures.sequence(listOfFutureResults, getContext().dispatcher()).map(new Mapper<Iterable<Object>, PiApproximation>()
      {
        @Override
        public PiApproximation apply(Iterable<Object> parameter) {
          double pi = 0d;
          for (Object o: parameter) {
            Result r = (Result) o;
            pi += r.getValue();
          }
          return new PiApproximation(pi, Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS));
        }
      }).onSuccess(new OnSuccess<PiApproximation>()
      {
        @Override
        public void onSuccess(PiApproximation piApproximation) throws Throwable {
          sender.tell(piApproximation);
          getContext().stop(getSelf());
        }
      });
    } else {
      unhandled(message);
    }
  }
}
