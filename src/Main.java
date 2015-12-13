public class Main {

  private static int lastID;

  private static float voteScale = 0f;
  private static final int SCALE_MAX = 110;

  private static int freqCounter = 0;
  private static final int FREQUENCY = 1;
  private static final int QUICK_CHANGE = 2;
  private static final int CHANGE = 1;
  private static final int QUICK_THRESHOLD = 15;

  private static Counter likeCounter;
  private static Counter dislikeCounter;


  public static void main(String[] args) {

    ListenerHandler listener = new ListenerHandler();
    likeCounter = new Counter();
    dislikeCounter = new Counter();

  }

  public static void noHands() {
    voteNeutral();
    voteNeutral();
    lastID = 0;
  }

  public static void tick(Vote vote, int id) {
    freqCounter++;
    if (freqCounter < FREQUENCY) {
      return;
    }
    freqCounter = 0;

    if (!(lastID == id)) {
      voteScale = 0;
    }

    switch (vote) {
      case DISLIKE:
        if (voteScale > QUICK_THRESHOLD) {
          voteScale -= 2 * QUICK_CHANGE;
        } else {
          voteScale -= CHANGE;
        }
        break;
      case NEUTRAL:
        voteNeutral();
        break;
      case LIKE:
        if (voteScale < -QUICK_THRESHOLD) {
          voteScale += 2 * QUICK_CHANGE;
        } else {
          voteScale += CHANGE;
        }
        break;
      default:
        break;
    }

    validateVote();
  }

  private static void voteNeutral() {
    if (voteScale < -2) {
      if (voteScale < SCALE_MAX + QUICK_THRESHOLD) {
        voteScale += 2 * QUICK_CHANGE;
      } else {
        voteScale += QUICK_CHANGE;
      }
    } else if (voteScale > 2) {
      if (voteScale > SCALE_MAX - QUICK_THRESHOLD) {
        voteScale -= 2 * QUICK_CHANGE;
      } else {
        voteScale -= QUICK_CHANGE;
      }
    } else {
      voteScale = 0;
    }
  }

  private static void validateVote() {
    if (voteScale > SCALE_MAX) {
      likeCounter.tick();
      voteScale = 0;
    } else if (voteScale < -SCALE_MAX) {
      dislikeCounter.tick();
      voteScale = 0;
    }
  }
}
