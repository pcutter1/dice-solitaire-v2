package edu.cnm.deepdive.dicesolitaire.model;

import java.util.Arrays;
import java.util.Random;

public class Roll {

  private final int[] dice;

  public Roll(Random rng) {
    dice = new int[Game.NUM_DICE];
    for (int i = 0; i < dice.length; i++) {
      dice[i] = 1 + rng.nextInt(Game.NUM_FACES);
    }
    Arrays.sort(dice);
  }

  public int[] getDice() {
    return Arrays.copyOf(dice, dice.length);
  }

}
