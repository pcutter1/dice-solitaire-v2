package edu.cnm.deepdive.dicesolitaire.model;

public enum DieState {

  AVAILABLE,
  UNAVAILABLE {
    @Override
    public boolean isSelectable() {
      return false;
    }
  },
  SCRATCH,
  PAIR;

  public boolean isSelectable() {
    return true;

  }

}
