public class Main {

  private static boolean recentlyValidated = false;
  private static int lastID;

  private static float voteScale = 0f;
  private static final int SCALE_MAX = 70;

  private static int freqCounter = 0;
  private static final int FREQUENCY = 1;
  private static final float QUICK_CHANGE = 2f;
  private static final float CHANGE = 1f;
  private static final float QUICK_THRESHOLD = 15f;

  private static Counter likeCounter;
  private static Counter dislikeCounter;


  public static void main(String[] args) {

    likeCounter = new Counter();
    dislikeCounter = new Counter();
    ListenerHandler listener = new ListenerHandler();

  }

  public static void noHands() {
    voteNeutral();
    voteNeutral();
  }

  public static void tick(Vote vote, int id) {
    freqCounter++;
    if (freqCounter < FREQUENCY) {
      return;
    }
    freqCounter = 0;

    if (recentlyValidated) {
      if (lastID == id) {
        voteNeutral();
      } else {
        recentlyValidated = false;
      }
    }

    if (!(lastID == id)) {
      voteScale = 0;
      lastID = id;
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

    if (recentlyValidated) {
      return;
    }

    if (voteScale > SCALE_MAX) {
      likeCounter.tick();
      voteScale = 0;
      recentlyValidated = true;
    } else if (voteScale < -SCALE_MAX) {
      dislikeCounter.tick();
      voteScale = 0;
      recentlyValidated = true;
    }
  }

  private static void testVote(Vote vote) {
    System.out.println(vote);
    System.out.println(likeCounter.getTicks() + " : " + dislikeCounter.getTicks());
  }

  public static float getScale() {
    return voteScale;
  }
}
