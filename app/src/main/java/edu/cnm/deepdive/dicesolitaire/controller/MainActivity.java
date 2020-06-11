package edu.cnm.deepdive.dicesolitaire.controller;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import edu.cnm.deepdive.dicesolitaire.R;
import edu.cnm.deepdive.dicesolitaire.model.Game;
import edu.cnm.deepdive.dicesolitaire.model.Roll;
import java.text.NumberFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private static final String DICE_FACE_ID_FORMAT = "face_%d";

  private TextView[] setLabels;
  private ProgressBar[] setCounts;
  private TextView[] setPoints;
  private TextView[] scratchLabels;
  private ProgressBar[] scratchCounts;
  private ImageView[] diceImages;
  private Drawable[] diceFaces;
  private Button roller;
  private Random rng = new Random();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
  }

  private void setupUI() {
    setContentView(R.layout.activity_main);
    Resources res = getResources();
    NumberFormat formatter = NumberFormat.getInstance();
    LayoutInflater inflater = getLayoutInflater();
    setupSetControls(inflater, res, formatter);
    setupDiceDisplay(inflater, res);
    setupPlayControls(res);
    setupScratchControls(inflater, res, formatter);
  }

  private void setupSetControls(LayoutInflater inflater, Resources res, NumberFormat formatter) {
    ViewGroup tallies = findViewById(R.id.set_tallies);
    int numTallies = Game.MAX_SET_VALUE - Game.MIN_SET_VALUE + 1;
    setLabels = new TextView[numTallies];
    setCounts = new ProgressBar[numTallies];
    setPoints = new TextView[numTallies];
    for (int i = 0; i < numTallies; i++) {
      View tally = inflater.inflate(R.layout.set_tally, tallies, false);
      TextView value = tally.findViewById(R.id.value);
      value.setText(formatter.format(i + Game.MIN_SET_VALUE));
      setLabels[i] = value;
      ProgressBar count = tally.findViewById(R.id.count);
      count.setMax(Game.MAX_SET_COUNT);
      count.setProgress(1 + rng.nextInt(Game.MAX_SET_COUNT));
      setCounts[i] = count;
      TextView points = tally.findViewById(R.id.points);
      points.setText(formatter.format(0));
      setPoints[i] = value;
      tallies.addView(tally);
    }
  }

  private void setupScratchControls(LayoutInflater inflater, Resources res, NumberFormat formatter) {
    ViewGroup tallies = findViewById(R.id.scratch_tallies);
    scratchLabels = new TextView[Game.NUM_FACES];
    scratchCounts = new ProgressBar[Game.NUM_FACES];
    for (int i = 0; i < Game.NUM_FACES; i++) {
      View tally = inflater.inflate(R.layout.scratch_tally, tallies, false);
      TextView value = tally.findViewById(R.id.value);
      value.setText(formatter.format(i + 1));
      scratchLabels[i] = value;
      ProgressBar count = tally.findViewById(R.id.count);
      count.setMax(Game.MAX_SCRATCH_COUNT);
      count.setProgress(1 + rng.nextInt(Game.MAX_SCRATCH_COUNT));
      scratchCounts[i] = count;
      tallies.addView(tally);
    }
  }

  private void setupDiceDisplay(LayoutInflater inflater, Resources res) {
    ViewGroup dice = findViewById(R.id.dice_roll);
    diceImages = new ImageView[Game.NUM_DICE];
    for (int i = 0; i < Game.NUM_DICE; i++) {
      ImageView die = (ImageView) inflater.inflate(R.layout.die_display, dice, false);
      die.setImageDrawable(getDrawable(R.drawable.face_6));
      diceImages[i] = die;
      dice.addView(die);
    }
    diceFaces = new Drawable[Game.NUM_FACES];
    for (int i = 0; i < Game.NUM_FACES; i++) {
      String idString = String.format(DICE_FACE_ID_FORMAT, i + 1);
      int id = res.getIdentifier(idString, "drawable", getPackageName());
      diceFaces[i] = getDrawable(id);
    }
  }

  private void setupPlayControls(Resources res) {
    roller = findViewById(R.id.roller);
    roller.setOnClickListener((v) -> {
      roller.setEnabled(false);
      new DiceAnimator().start();
    });
  }

  private void displayDiceFace(int die, int value) {
    diceImages[die].setImageDrawable(diceFaces[value - 1]);
  }

  private class DiceAnimator extends Thread {

    private static final int ANIMATION_STEPS = 10;
    private static final int MILLISECONDS_PER_STEP = 20;

    @Override
    public void run() {
      Roll roll = new Roll(rng);
      for (int i = 0; i < Game.NUM_DICE; i++) {
        for (int j = 0; j < ANIMATION_STEPS; j++) {
          int animationFace = 1 + rng.nextInt(Game.NUM_FACES);
          displayFace(i, animationFace);
          try {
            sleep(MILLISECONDS_PER_STEP);
          } catch (InterruptedException expected) {
            // Ignore exception and get on with life.
          }
        }
        final int value = roll.getDice()[i];
        displayFace(i, value);
      }
      runOnUiThread(() -> roller.setEnabled(true));
    }

    private void displayFace(final int dieIndex, final int value) {
      runOnUiThread(() -> displayDiceFace(dieIndex, value));
    }

  }

}
