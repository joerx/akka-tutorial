package home.jhenning.akkapi.actors;

import akka.actor.UntypedActor;
import home.jhenning.akkapi.messages.Result;
import home.jhenning.akkapi.messages.Work;

/**
 * Created with IntelliJ IDEA.
 * User: jhenning
 * Date: 11/26/12
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Worker extends UntypedActor {
  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Work) {
      Work work = (Work) message;
      double result = calculatePiFor(work.getStart(), work.getNrOfElements());
      getSender().tell(new Result(result), getSelf());
    } else {
      unhandled(message);
    }
  }

  private double calculatePiFor(int start, int nrOfElements) {
    double acc = 0.0;
    for (int i = start * nrOfElements; i <= ((start + 1) * nrOfElements - 1); i++) {
      acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
    }
    return acc;
  }
}
