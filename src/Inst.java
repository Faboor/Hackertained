import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.UnauthorizedException;
import com.sun.deploy.util.ArrayUtil;
import leapmhack.MessageSender;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Inst {


  private boolean recentlyValidated = false;
  private int lastID;

  private float voteScale = 0f;
  private final int SCALE_MAX = 70;

  private int freqCounter = 0;
  private final int FREQUENCY = 1;
  private final float QUICK_CHANGE = 2f;
  private final float CHANGE = 1f;
  private final float QUICK_THRESHOLD = 15f;
  private final ArrayList<Integer> MILESTONES = new ArrayList<Integer>(Arrays.asList(5, 10, 25, 50, 100, 200));

  private Counter likeCounter;
  private Counter dislikeCounter;
  private TransparentImage img;
  private MessageSender msgs;

  public Inst() {

    likeCounter = new Counter();
    dislikeCounter = new Counter();
    msgs = new MessageSender(new BigInteger("00447721389199"));
    img = TransparentImage.createAndShowUI();
  }

  public void noHands() {
    voteNeutral();
    voteNeutral();
  }

  public void tick(Vote vote, int id) {
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

  private void voteNeutral() {

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

  private void validateVote() {
    setAlpha();
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

    if(recentlyValidated) {
      testVote();
      SMS();
    }
  }

  private  void testVote() {
    System.out.println(likeCounter.getTicks() + " : " + dislikeCounter.getTicks());
  }

  private void setAlpha() {
    img.setAlpha(0.5f + (Math.max(-1.0f, Math.min(1.0f, (voteScale / SCALE_MAX)))) / 2.0f);
  }

  private void SMS() {
    if (MILESTONES.contains(new Integer(likeCounter.getTicks()))) {
      try {
        System.out.println("Sending message...");
        msgs.sendMsg(likeCounter.getTicks());
      } catch (UnauthorizedException e) {
        e.printStackTrace();
      } catch (GeneralException e) {
        e.printStackTrace();
      }
    }
  }
}
