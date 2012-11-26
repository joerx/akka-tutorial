package home.jhenning.akkapi.messages;

/**
 * Created with IntelliJ IDEA.
 * User: jhenning
 * Date: 11/26/12
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Work {
  private final int start;
  private final int nrOfElements;

  public Work(int start, int nrOfElements) {
    this.start = start;
    this.nrOfElements = nrOfElements;
  }

  public int getStart() {
    return start;
  }

  public int getNrOfElements() {
    return nrOfElements;
  }
}
