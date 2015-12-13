/******************************************************************************
 * \
 * Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.               *
 * Leap Motion proprietary and confidential. Not for distribution.              *
 * Use subject to the terms of the Leap Motion SDK Agreement available at       *
 * https://developer.leapmotion.com/sdk_agreement, or another agreement         *
 * between Leap Motion and you, your company or other organization.             *
 * \
 ******************************************************************************/

import java.io.IOException;
import java.lang.Math;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class VoteListener extends Listener {

  private int framesCount = 0;
  private final double ROLL_THRESHOLD = 37.5;

  private java.text.DecimalFormat df = new java.text.DecimalFormat("#.####");

  public void onInit(Controller controller) {
    System.out.println("Initialized");
    df.setRoundingMode(java.math.RoundingMode.HALF_UP);
  }

  public void onConnect(Controller controller) {
    System.out.println("Connected");
  }

  public void onDisconnect(Controller controller) {
    //Note: not dispatched when running in a debugger.
    System.out.println("Disconnected");
  }

  public void onExit(Controller controller) {
    System.out.println("Exited");
  }

  public void onFrame(Controller controller) {
    if (framesCount < 100) {
      framesCount++;
      return;
    }
    framesCount = 0;

    Frame frame = controller.frame();

    printData(frame);

    System.out.println(getVote(frame));


  }

  public Vote getVote(Frame frame) {
    for (Hand hand : frame.hands()) {

      double roll = Math.toDegrees(hand.palmNormal().roll());
      if (hand.isRight()) {
        roll *= -1;
      }

      if (roll > ROLL_THRESHOLD) {
        return Vote.LIKE;
      } else if (roll < -ROLL_THRESHOLD) {
        return  Vote.DISLIKE;
      } else {
        return Vote.NEUTRAL;
      }
    }
    return null;
  }

  public void printData(Frame frame) {
    //Get hands
    for (Hand hand : frame.hands()) {

      // Get the hand's normal vector and direction
      Vector normal = hand.palmNormal();
      Vector direction = hand.direction();

      System.out.print((hand.isLeft() ? "L" : "R") + "   -   ");

      System.out.print(df.format(normal.getX())
          + ",  " + df.format(normal.getY())
          + ",  " + df.format(normal.getZ())
          + "   -   ");

      System.out.println(df.format(direction.getX())
          + ",  " + df.format(direction.getY())
          + ",  " + df.format(direction.getZ()));

      System.out.println(//"  pitch: " + df.format(Math.toDegrees(direction.pitch())) + " degrees, " +
           "roll: " + df.format(Math.toDegrees(normal.roll())) + " degrees, ");
          //+ "yaw: " + df.format(Math.toDegrees(direction.yaw())) + " degrees");

    }
  }
}

