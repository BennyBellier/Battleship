package model;

import java.util.Scanner;

import global.Configuration;

public class PlayerSet {
  private CaseType[][] shipSet;
  private CaseType[][] ennemySet;
  int nbShipAlive;
  CaseType[] shipAlive = { CaseType.AIRCRAFTCARRIER, CaseType.BATTLESHIP, CaseType.CRUISER, CaseType.SUBMARINE,
      CaseType.DESTROYER, CaseType.DESTROYER, CaseType.AIRCRAFT, CaseType.AIRCRAFT };

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
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.println("Les coordonnées ne correspondent pas à la taille du bateau !");
          return false;

        default:
          System.out.println("The coordinates do not correspond to the size of the warship!");
          return false;
      }
    }
    if ((Math.abs(p2 - p1) + 1) == ship.ShipSize() && coordInGrid(p1, p2)) {
      // Place the ship on the grid
      for (int i = Math.min(p1, p2); i <= Math.max(p1, p2); i++) {
        if (verticaly)
          shipSet[i][cst] = ship;
        else
          shipSet[cst][i] = ship;
      }
      System.out.println("ShipPlaced");
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
  boolean coordInGrid(int pos1, int pos2) {
    return (pos1 < 10 && pos1 >= 0) && (pos2 < 10 && pos2 >= 0);
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
   * Sets the ship at the given position to hited.
   *
   * @param x The x coordinate of the ship.
   * @param y The y coordinate of the ship.
   */
  public void shipHit(int x, int y) {
    try {
      shipSet[y][x] = shipSet[y][x].hited();
    } catch (NullPointerException e) {
      throw new NullPointerException("There is no ship at position (" + x + ", " + y + ")");
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

  public void shipPlacement() {
    CaseType[] shipListSelector = shipAlive.clone();
    Scanner scan = new Scanner(System.in);
    while (nbShipAlive != shipAlive.length) {
      CaseType curShip = shipSelector(shipListSelector, scan);

      // * first coords
      int[] coords = userInputFirstPlacementCoords(curShip, scan);
      int x1 = coords[0];
      int y1 = coords[1];

      // * 2nd coords
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.println("\nVeuillez saisir les coordonnées de l'autre extrémités du " + curShip.fullName());
          System.out.println("La taille du " + curShip.fullName() + " est de " + curShip.ShipSize() + " cases");
          break;

        default:
          System.out.println("\nPlease enter the coordinates of the other end of " + curShip.fullName());
          System.out.println("the size of " + curShip.fullName() + " is " + curShip.ShipSize() + " squares");
          break;
      }
      int x2 = -1, y2 = -1;

      while (!setShip(curShip, x1, y1, x2, y2)) {
        coords = userInputSecondPlacementCoords(curShip, scan, x1, y1);
        x2 = coords[0];
        y2 = coords[1];
      }

      shipListSelector = reduceShipListSelector(shipListSelector, curShip);

      ++nbShipAlive;
      System.out.println(toString());
    }
    scan.close();
  }

  /**
   * Reduces the list of ships by one, by removing the ship that was just
   * selected.
   *
   * @param ships the list of ships to reduce.
   * @param ship  the ship that was just selected.
   * @return the reduced list of ships.
   */
  CaseType[] reduceShipListSelector(CaseType[] ships, CaseType ship) {
    boolean reduce = false;
    for (int i = 0; i < ships.length; i++) {
      if (reduce) {
        if (i < ships.length - 1)
          ships[i] = ships[i + 1];
        else
          ships[i] = null;
      } else if (ships[i] == ship) {
        ships[i] = ships[i + 1];
        reduce = true;
      }
    }
    return ships;
  }

  /**
   * Prompts the user to enter the coordinates of the first placement of a ship.
   *
   * @param ship The ship to be placed.
   * @param scan The scanner to use for user input.
   * @return The coordinates of the first placement of the ship.
   */
  int[] userInputFirstPlacementCoords(CaseType ship, Scanner scan) {
    // display text
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("\nVeuillez saisir les coordonnées de l'une des extrémités de la position du"
            + ship.fullName() + " (saisissez la lettre puis le numéro comme suivant: \"H5\")");
        break;

      default:
        System.out.println("\nPlease enter the coordinates of one end of " + ship.fullName()
            + " position (enter the letter and then the number as follows: \"H5\")");
        break;
    }

    int x = -1, y = -1;
    while (!coordInGrid(x, y)) {
      int[] coords = lineToCoords(scan);
      x = coords[0];
      y = coords[1];
    }
    return new int[] {x, y};
  }

  int[] userInputSecondPlacementCoords(CaseType ship, Scanner scan, int x1, int y1) {
    int x, y;
    int[] coords = lineToCoords(scan);
    x = coords[0];
    y = coords[1];

    if (x == -1 && y == -1) {
      return coords;
    }

    if (x != x1 && y != y1) {
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.println("Les bateaux ne peuvent pas être placé en diagonales !");
          break;

        default:
          System.out.println("Boats cannot be placed diagonally!");
          break;
      }
      return new int[] {-1, -1};
    }
    return coords;
  }

  /**
   * Converts a line of text to a coordinate.
   * @param scan The scanner to read the line from.
   * @return The coordinate, or {-1, -1} if the line is invalid.
   */
  int[] lineToCoords(Scanner scan) {
    String line;
    int x = -1, y = -1;

    System.out.print("Coords> ");

    try {
      // Get user input
      line = scan.nextLine();

      // Get y coordinate
      y = letterToInt(line.charAt(0));

      // Get x coordinate
      if (Integer.parseInt(line.charAt(1) + "") == 1) {
        if (line.length() == 3 && Integer.parseInt(line.charAt(2) + "") == 0) // if user type text like 'A10'
          x = 9;
        else if (line.length() == 2)
          x = 1;
      }
      if (line.length() == 2) { // if user type text like 'H5'
        x = Integer.parseInt(line.charAt(1) + "") - 1;
      }

      // Print error message if coordinates are not in the grid
      if (!coordInGrid(x, y)) {
        switch (Configuration.instance().lang()) {
          case "fr":
            System.out.println("Les coordonnées sont hors de la grille !");
            break;

          default:
            System.out.println("Coordinates are outside the grid!");
            break;
        }
        return new int[] {-1, -1};
      }
    } catch (Exception e) { // if user input isn't correct
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.println("Veuillez saisir des coordonnées correctes !");
          break;

        default:
          System.out.println("Please enter correct cordinates !");
          break;
      }
      return new int[] {-1, -1};
    }

    return new int[] {x, y};
  }

  /**
   * Selects a ship from a list of ships.
   *
   * @param ships The list of ships to choose from.
   * @param scan  The scanner to use.
   * @return The selected ship.
   */
  CaseType shipSelector(CaseType[] ships, Scanner scan) {
    int i;

    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("\nListe des batiments à placer");
        break;

      default:
        System.out.println("\nList of warships to place");
        break;
    }

    // Display of the ships name and the number of ships reamaining to be placed
    for (i = 1; i <= ships.length - nbShipAlive; i++) {
      System.out.print(i + ") " + ships[i - 1].fullName() + "\t");
      if (i != 0 && i % 3 == 0) {
        System.out.println();
      }
    }

    if (i % 3 != 0) {
      System.out.println("");
    }
    int shipNumber = 0;
    while (shipNumber < 1 || shipNumber > ships.length - nbShipAlive) {
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.print("\nEntrez un numéro > ");
          break;

        default:
          System.out.print("\nSelect ship number > ");
          break;
      }
      try {
        String line = scan.nextLine();
        shipNumber = Integer.parseInt(line);

        if (shipNumber < 1 || shipNumber > ships.length) {
          switch (Configuration.instance().lang()) {
            case "fr":
              System.out.println("Selectionnez un numéro présent dans la liste ci-dessus !");
              break;

            default:
              System.out.println("Please enter a number in the list above!");
              break;
          }
        }
      } catch (Exception e) {
        switch (Configuration.instance().lang()) {
          case "fr":
            System.out.println("Selectionnez un numéro présent dans la liste ci-dessus !");
            break;

          default:
            System.out.println("Please enter a number in the list above!");
            break;
        }
        shipNumber = 0;
      }
    }

    CaseType shipSelected = ships[shipNumber - 1];
    return shipSelected;
  }

  /**
   * Converts a letter to an integer.
   *
   * @param letter the letter to convert.
   * @return the integer value of the letter.
   */
  int letterToInt(char letter) {
    switch (Character.toUpperCase(letter)) {
      case 'A':
        return 0;
      case 'B':
        return 1;
      case 'C':
        return 2;
      case 'D':
        return 3;
      case 'E':
        return 4;
      case 'F':
        return 5;
      case 'G':
        return 6;
      case 'H':
        return 7;
      case 'I':
        return 8;
      case 'J':
        return 9;

      default:
        return -1;
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
    build.append("+---+---+---+---+---+---+---+---+---+---+---+ +---+---+---+---+---+---+---+---+---+---+---+\n"
        + "|   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| |   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10|\n");
    for (int i = 0; i < 10; i++) {
      build.append("+---+---+---+---+---+---+---+---+---+---+---+ +---+---+---+---+---+---+---+---+---+---+---+\n|");

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
            build.append(ANSI_GREEN + shipSet[i][k].toString() + ANSI_RESET + "|");
          }
        }
      }

      build.append("\n");
    }
    build.append("+---+---+---+---+---+---+---+---+---+---+---+ +---+---+---+---+---+---+---+---+---+---+---+\n");
    return build.toString();
  }
}