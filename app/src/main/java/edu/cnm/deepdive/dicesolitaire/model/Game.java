package edu.cnm.deepdive.dicesolitaire.model;

import java.util.Arrays;
import java.util.Random;

public class Game {

  public static final int SET_SIZE = 2;
  public static final int NUM_SETS = 2;
  public static final int NUM_SCRATCHES = 1;
  public static final int NUM_DICE = NUM_SCRATCHES + NUM_SETS * SET_SIZE;
  public static final int NUM_FACES = 6;
  public static final int MIN_SET_VALUE = SET_SIZE;
  public static final int MAX_SET_VALUE = SET_SIZE * NUM_FACES;
  public static final int MAX_SET_COUNT = 10;
  public static final int MAX_SCRATCH_COUNT = 8;

  private final Random rng;
  private final int[] setCounts = new int[MAX_SET_VALUE - MIN_SET_VALUE + 1];
  private final int[] scratchCounts = new int[NUM_FACES];
  private final DieState[] dieStates = new DieState[NUM_DICE];

  private GameState gameState;
  private RollState rollState;
  private Roll roll;

  public Game(Random rng) {
    this.rng = rng;
  }

  public boolean incrementSetCount(int setValue) {
    int count = setCounts[setValue - MIN_SET_VALUE];
    if (count < MAX_SET_COUNT) {
      count++;
      setCounts[setValue - MIN_SET_VALUE] = count;
      return true;
    } else {
      return false;
    }
  }

  public boolean incrementScratchCount(int scratchValue) {
    int count = scratchCounts[scratchValue - 1];
    if (count < MAX_SCRATCH_COUNT) {
      count++;
      setCounts[scratchValue - 1] = count;
      return true;
    } else {
      return false;
    }
  }

  public DieState[] getDieStates() {
    return Arrays.copyOf(dieStates, dieStates.length);
  }

  public GameState getGameState() {
    return gameState;
  }

  public RollState getRollState() {
    return rollState;
  }

  public void roll() {
    //TODO Check current rollState and gameState.
    roll = new Roll(rng);
  }

  public Roll getRoll() {
    return roll;
  }

  public void setSelected(int die, boolean selected) {
    //TODO Check current dieStates and rollState.
    if (dieStates[die] != DieState.UNAVAILABLE) {
      if (dieStates[die].isSelectable() && selected) {

      } else if (!selected) {
        //TODO Check current rollState
        dieStates[die] = DieState.AVAILABLE;
      }
    }
  }
}
