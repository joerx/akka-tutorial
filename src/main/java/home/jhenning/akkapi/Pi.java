package home.jhenning.akkapi;

import akka.actor.*;
import home.jhenning.akkapi.actors.Listener;
import home.jhenning.akkapi.actors.Master;
import home.jhenning.akkapi.messages.Calculate;

/**
 * Created with IntelliJ IDEA.
 * User: jhenning
 * Date: 11/26/12
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pi {

  public static void main(String[] args) {
    Pi pi = new Pi();
    pi.calculate(4, 10000, 10000);
  }

  public void calculate(
      final int nrOfWorkers,
      final int nrOfElements,
      final int nrOfMessages)
  {
    ActorSystem system = ActorSystem.create("PiSystem");
    final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");

    final ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
      @Override
      public Actor create() throws Exception {
        return new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener);
      }
    }), "master");

    master.tell(new Calculate());
  }
}
