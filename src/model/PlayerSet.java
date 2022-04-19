package model;

import view.ErrorMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlayerSet {

  private CaseType[][] shipSet;
  private CaseType[][] ennemySet;
  int nbShipAlive;
  ArrayList<CaseType> shipAlive = new ArrayList<>(Arrays.asList(CaseType.AIRCRAFTCARRIER, CaseType.BATTLESHIP, CaseType.CRUISER, CaseType.SUBMARINE, CaseType.DESTROYER, CaseType.DESTROYER, CaseType.AIRCRAFT, CaseType.AIRCRAFT));

  /**
   * Constructor of the PlayerSet class.
   */
  public PlayerSet() {
    shipSet = new CaseType[10][10];
    ennemySet = new CaseType[10][10];
    nbShipAlive = 0;
  }

  /**
   * Sets the ship at the given coordinates.
   *
   * @param ship The ship to set.
   * @param x1   The x coordinate of the first point.
   * @param y1   The y coordinate of the first point.
   * @param x2   The x coordinate of the second point.
   * @param y2   The y coordinate of the second point.
   * @return Whether the ship was successfully set.
   */
  public boolean setShip(CaseType ship, int x1, int y1, int x2, int y2) {
    if (x1 == x2) {
      return setShipOnPos(ship, x1, y1, y2, true);
    } else if (y1 == y2) {
      return setShipOnPos(ship, y1, x1, x2, false);
    }
    return false;
  }

  /**
   * Sets the ship on the given coordinates.
   *
   * @param ship      The ship to set.
   * @param cst       The coordinate to set the ship on.
   * @param p1        The first coordinate of the ship.
   * @param p2        The second coordinate of the ship.
   * @param verticaly Whether the ship is vertical or not.
   * @return Whether the ship was set or not.
   */
  boolean setShipOnPos(CaseType ship, int cst, int p1, int p2, boolean verticaly) {
    if ((Math.abs(p2 - p1) + 1) != ship.ShipSize()) {
      ErrorMessage.coordsNotEqualsBoatSize();
      return false;
    }
    if (!noSuperPosShip(cst, p1, p2, verticaly)) {
      ErrorMessage.boatSuperposition();
      return false;
    }

    if ((Math.abs(p2 - p1) + 1) == ship.ShipSize() && coordInGrid(p1, p2)) {
      // Place the ship on the grid
      for (int i = Math.min(p1, p2); i <= Math.max(p1, p2); i++) {
        if (verticaly) shipSet[i][cst] = ship; else shipSet[cst][i] = ship;
      }
      return true;
    }
    return false;
  }

  /**
   * Checks if the coordinates are within the grid.
   *
   * @param pos1 The first coordinate.
   * @param pos2 The second coordinate.
   * @return Whether the coordinates are within the grid.
   */
  public boolean coordInGrid(int pos1, int pos2) {
    return (pos1 < 10 && pos1 >= 0) && (pos2 < 10 && pos2 >= 0);
  }

  /**
  * Checks if there is a ship at the given coordinates.
  * @param cst the column of the ship's head
  * @param p1 the row of the ship's head
  * @param p2 the row of the ship's tail
  * @param verticaly whether the ship is vertical or not
  * @return whether there is a ship at the given coordinates.
   */
  boolean noSuperPosShip(int cst, int p1, int p2, boolean verticaly) {
    for (int i = Math.min(p1, p2); i <= Math.max(p1, p2); i++) {
      if (verticaly) {
        if (shipSet[i][cst] != null)
          return false;
      }
      else {
        if (shipSet[cst][i] != null)
          return false;
      }
    }
    return true;
  }

  /**
   * Returns the type of ship at the given coordinates.
   *
   * @param x The x coordinate of the ship.
   * @param y The y coordinate of the ship.
   * @return The type of ship at the given coordinates.
   */
  public CaseType isShip(int x, int y) {
    return shipSet[x][y];
  }

  /**
   * Returns whether all the ships are sunk.
   * @return whether all the ships are sunk.
   */
  public boolean isAllShipSunk() {
    return nbShipAlive == 0;
  }

  /**
   * Returns the number of ships still alive.
   * @return the number of ships still alive.
   */
  public int nbShipStillAlive() {
    return nbShipAlive;
  }

  /**
   * Sets the ship at the given position to hited.
   *
   * @param x The x coordinate of the ship.
   * @param y The y coordinate of the ship.
   */
  public void shipHit(int x, int y) {
    try {
      shipSet[y][x] = shipSet[y][x].hited();
    } catch (NullPointerException e) {
      throw new NullPointerException(
        "There is no ship at position (" + x + ", " + y + ")"
      );
    }
  }

  /**
   * Sets the ennemy at the given position to be hit.
   *
   * @param x The x position of the ennemy.
   * @param y The y position of the ennemy.
   */
  public void ennemyHit(int x, int y) {
    ennemySet[y][x] = CaseType.HIT;
  }

  /**
   * Sets the case at the given coordinates to MISS.
   *
   * @param x The x coordinate of the case.
   * @param y The y coordinate of the case.
   */
  public void ennemyMiss(int x, int y) {
    ennemySet[y][x] = CaseType.MISS;
  }

  /**
   * Sets the ennemySet[y][x] to SUNK if the coordinates are in the grid.
   *
   * @param x1 the x coordinate of the first point
   * @param y1 the y coordinate of the first point
   * @param x2 the x coordinate of the second point
   * @param y2 the y coordinate of the second point
   */
  public void ennemySunk(int x1, int y1, int x2, int y2) {
    if (x1 == x2) {
      if (coordInGrid(y1, y2)) {
        for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
          ennemySet[i][x1] = CaseType.SUNK;
        }
      }
    } else if (y1 == y2) {
      if (coordInGrid(x1, x2)) {
        for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
          ennemySet[y1][i] = CaseType.SUNK;
        }
      }
    }
  }

  /**
   * Creates a copy of the given list.
   * @param list the list to copy
   * @return a copy of the list
   */
  public ArrayList<CaseType> CopyOfShipAlive() {
    ArrayList<CaseType> clone = new ArrayList<>();
    for (CaseType item : shipAlive) {
      clone.add(item);
    }
    return clone;
  }

  public boolean notAllShipsPlaced() {
    return nbShipAlive != shipAlive.size();
  }

  /**
  * Resets the ship set to null.
   */
  public void resetShipSet() {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        shipSet[i][j] = null;
      }
    }
    nbShipAlive = 0;
  }

  public int nbShipAlive() {
    return nbShipAlive;
  }

  public void incrementNbShipAlive() {
    ++nbShipAlive;
  }

  /**
  * Resets the fire set to null.
   */
  public void resetFireSet() {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        ennemySet[i][j] = null;
      }
    }
  }

  /**
  * Randomly places the ships on the grid.
   */
  public void randomShipPlacement() {
    Random rand = new Random();
    ArrayList<CaseType> shipListSelector = CopyOfShipAlive();
    boolean coordsCorrect;

    while (nbShipAlive != shipAlive.size()) {
      CaseType ship = shipListSelector.get(rand.nextInt(shipListSelector.size()));
      coordsCorrect = false;

      while (!coordsCorrect) {
        //* first coords
        int x1 = rand.nextInt(10);
        int y1 = rand.nextInt(10);

        int orientation = rand.nextInt(4);
        int x2 = -1, y2 = -1;

        switch (orientation) {
          case 0:
            y2 = y1 - (ship.ShipSize() - 1);
            if (coordInGrid(x1, y2) && noSuperPosShip(x1, y1, y2, true)) {
              if (setShip(ship, x1, y1, x1, y2)) {
                coordsCorrect = true;
              }
            }
            break;
          case 1:
            x2 = x1 + (ship.ShipSize() - 1);
            if (coordInGrid(x2, y1) && noSuperPosShip(y1, x1, x2, false)) {
              if (setShip(ship, x1, y1, x2, y1)) {
                coordsCorrect = true;
              }
            }
            break;
          case 2:
            y2 = y1 + (ship.ShipSize() - 1);
            if (coordInGrid(x1, y2) && noSuperPosShip(x1, y1, y2, true)) {
              if (setShip(ship, x1, y1, x1, y2)) {
                coordsCorrect = true;
              }
            }
            break;
          case 3:
            x2 = x1 - (ship.ShipSize() - 1);
            if (coordInGrid(x2, y1) && noSuperPosShip(y1, x1, x2, false)) {
              if (setShip(ship, x1, y1, x2, y1)) {
                coordsCorrect = true;
              }
            }
            break;
        }
      }
      shipListSelector.remove(ship);
      ++nbShipAlive;
    }
  }

  /**
   * Converts an integer to a letter.
   *
   * @param i the integer to convert to a letter.
   * @return the letter corresponding to the integer.
   */
  String intToLetter(int i) {
    switch (i) {
      case 0:
        return " A |";
      case 1:
        return " B |";
      case 2:
        return " C |";
      case 3:
        return " D |";
      case 4:
        return " E |";
      case 5:
        return " F |";
      case 6:
        return " G |";
      case 7:
        return " H |";
      case 8:
        return " I |";
      case 9:
        return " J |";
      default:
        return " Z |";
    }
  }

  static final String ANSI_RESET = "\u001B[0m";
  static final String ANSI_GREEN = "\u001B[32m";

  /**
   * Returns a string representation of the board.
   *
   * @return The string representation of the board.
   */
  public String toString() {
    StringBuilder build = new StringBuilder();
    build.append(
      "+---+---+---+---+---+---+---+---+---+---+---+ +---+---+---+---+---+---+---+---+---+---+---+\n" +
      "|   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| |   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10|\n"
    );
    for (int i = 0; i < 10; i++) {
      build.append(
        "+---+---+---+---+---+---+---+---+---+---+---+ +---+---+---+---+---+---+---+---+---+---+---+\n|"
      );

      for (int j = -1; j < 10; j++) {
        if (j == -1) {
          build.append(intToLetter(i));
        } else {
          if (ennemySet[i][j] == null) {
            build.append("   |");
          } else {
            build.append(ennemySet[i][j].toString() + "|");
          }
        }
      }

      build.append(" |");

      for (int k = -1; k < 10; k++) {
        if (k == -1) {
          build.append(intToLetter(i));
        } else {
          if (shipSet[i][k] == null) {
            build.append("   |");
          } else {
            build.append(
              ANSI_GREEN + shipSet[i][k].toString() + ANSI_RESET + "|"
            );
          }
        }
      }

      build.append("\n");
    }
    build.append(
      "+---+---+---+---+---+---+---+---+---+---+---+ +---+---+---+---+---+---+---+---+---+---+---+\n"
    );
    return build.toString();
  }

  /**
   * Prints the ship set to the console.
   */
  public void printShipSet() {
    StringBuilder build = new StringBuilder();
    build.append( "+---+---+---+---+---+---+---+---+---+---+---+\n" + "|   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10|\n");
    for (int i = 0; i < 10; i++) {
      build.append(
        "+---+---+---+---+---+---+---+---+---+---+---+\n|"
      );
      for (int k = -1; k < 10; k++) {
        if (k == -1) {
          build.append(intToLetter(i));
        } else {
          if (shipSet[i][k] == null) {
            build.append("   |");
          } else {
            build.append(
              ANSI_GREEN + shipSet[i][k].toString() + ANSI_RESET + "|"
            );
          }
        }
      }
      build.append("\n");
    }
    build.append(
      "+---+---+---+---+---+---+---+---+---+---+---+\n"
    );
    System.out.println(build);
  }

  /**
  * Prints the fire set in a readable format.
   */
  public void printFireSet() {
    StringBuilder build = new StringBuilder();
    build.append( "+---+---+---+---+---+---+---+---+---+---+---+\n" + "|   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10|\n");
    for (int i = 0; i < 10; i++) {
      build.append("+---+---+---+---+---+---+---+---+---+---+---+\n|");

      for (int j = -1; j < 10; j++) {
        if (j == -1) {
          build.append(intToLetter(i));
        } else {
          if (ennemySet[i][j] == null) {
            build.append("   |");
          } else {
            build.append(ennemySet[i][j].toString() + "|");
          }
        }
      }
      build.append("\n");
    }
    build.append("+---+---+---+---+---+---+---+---+---+---+---+\n");
    System.out.println(build);
  }
}
