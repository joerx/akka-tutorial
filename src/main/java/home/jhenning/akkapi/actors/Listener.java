package home.jhenning.akkapi.actors;

import akka.actor.UntypedActor;
import home.jhenning.akkapi.messages.PiApproximation;

/**
 * Created with IntelliJ IDEA.
 * User: jhenning
 * Date: 11/26/12
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Listener extends UntypedActor {

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof PiApproximation) {
      PiApproximation approx = (PiApproximation) message;
      System.out.println(String.format("\n\tPI Approximation: " +
        "\t\t%s\n\tCalculation time: \t%s",
          approx.getPi(),
          approx.getDuration()
      ));
      getContext().system().shutdown();
    } else {
      unhandled(message);
    }
  }
}
