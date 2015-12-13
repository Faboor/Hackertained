/**
 * Created by Acer on 13-Dec-15.
 */
public class Counter {

  private int ticks;

  public Counter() {
    ticks = 0;
  }

  public Counter(int ticks) {
    this.ticks = ticks;
  }

  public void tick() {
    ticks++;
  }

  public int getTicks() {
    return ticks;
  }

  @Override public String toString() {
    return "" + ticks;
  }
}
