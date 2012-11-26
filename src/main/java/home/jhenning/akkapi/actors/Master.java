package home.jhenning.akkapi.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import home.jhenning.akkapi.messages.Calculate;
import home.jhenning.akkapi.messages.PiApproximation;
import home.jhenning.akkapi.messages.Result;
import home.jhenning.akkapi.messages.Work;

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

  private double pi;
  private int nrOfResults;
  private final long start = System.currentTimeMillis();

  private final ActorRef listener;
  private final ActorRef workerRouter;

  public Master(
      final int nrOfWorkers,
      int nrOfMessages,
      int nrOfElements,
      ActorRef listener)
  {
    this.nrOfMessages = nrOfMessages;
    this.nrOfElements = nrOfElements;
    this.listener = listener;

    workerRouter = this.getContext().actorOf(new Props(Worker.class).withRouter(
        new RoundRobinRouter(nrOfWorkers)), "workerRouter");
  }


  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Calculate) {
      for (int start = 0; start < nrOfMessages; start ++) {
        workerRouter.tell(new Work(start, nrOfElements), getSelf());
      }
    } else if (message instanceof Result) {
      Result result = (Result) message;
      pi += result.getValue();
      nrOfResults += 1;
      if (nrOfResults == nrOfMessages) {
        Duration duration = Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
        listener.tell(new PiApproximation(pi, duration), getSelf());
        getContext().stop(getSelf());
      }
    } else {
      unhandled(message);
    }
  }
}
