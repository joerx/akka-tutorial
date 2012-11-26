package home.jhenning.akkapi.messages;

/**
 * Created with IntelliJ IDEA.
 * User: jhenning
 * Date: 11/26/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Result {

  private final double value;

  public Result(double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }
}
