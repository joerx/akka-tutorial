package home.jhenning.akkapi.messages;

import akka.util.Duration;

/**
 * Created with IntelliJ IDEA.
 * User: jhenning
 * Date: 11/26/12
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class PiApproximation {

  private final double pi;
  private final Duration duration;

  public PiApproximation(double pi, Duration duration) {
    this.pi = pi;
    this.duration = duration;
  }

  public double getPi() {
    return pi;
  }

  public Duration getDuration() {
    return duration;
  }
}
