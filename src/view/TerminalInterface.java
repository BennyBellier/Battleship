package view;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

import global.Configuration;
import model.CaseType;
import model.GameMaster;
import model.PlayerSet;

public class TerminalInterface {
  GameMaster gm;
  PlayerSet p1;
  PlayerSet p2;
  int nbPlayer;
  Scanner scan;

  TerminalInterface() {
    p1 = new PlayerSet();
    p2 = new PlayerSet();
    scan = new Scanner(System.in);
    gm = new GameMaster(p1, p2);
  }

  public static void start() {
    TerminalInterface i = new TerminalInterface();
    switch (Configuration.instance().read("Lang")) {
      case "fr":
        System.out.println("Battaile Navale!");
        break;

      default:
        System.out.println("Battleship!");
        break;
    }
    i.initGame();
    // i.executeGame();
  }

  void initGame() {
    try {
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.println("1) 1 joueur\t2) 2 joueurs");
          break;

        default:
          System.out.println("1) 1 player\t2) 2 players");
          break;
      }
      System.out.print("> ");
      String line = scan.nextLine();
      switch (Integer.parseInt(line)) {
        case 1:
          nbPlayer = 1;
          shipPlacement(p1);
          p2.randomShipPlacement();
          break;
        case 2:
          nbPlayer = 2;
          clearConsole();
          System.out.println("Joueur 1:\n---------");
          shipPlacement(p1);
          clearConsole();
          System.out.println("Joueur 2:\n---------");
          shipPlacement(p2);
          break;

        default:
          ErrorMessage.nonCorrectAnswer();
          break;
      }
      gm.setNbPlayer(nbPlayer);
    } catch (Exception e) {
      ErrorMessage.nonCorrectAnswer();
    }
  }

  /**
   * Generates a random ship placement for the player.
   * @param set The player's ship set.
   */
  void shipPlacement(PlayerSet set) {
    String choice = userChoiceGeneration(scan);

    if (choice.equals("random")) {
      boolean usernotOk = true;

      while (usernotOk) {
        set.randomShipPlacement();
        set.printShipSet();

        switch (Configuration.instance().lang()) {
          case "fr":
            System.out.print("Choississez vous ce modèle (O/n) ? ");
            break;

          default:
            System.out.print("Do you choose this pattern (Y/n) ? ");
            break;
        }
        String line = scan.nextLine();
        if (line.equals("n") || line.equals("N")) {
          set.resetShipSet();
          usernotOk = true;
        }
        else
          usernotOk = false;
      }
    }
    else {
      manuallySetShips(set);
    }
  }

  /**
   * Manually set the ships of a player.
   *
   * @param set The player set.
   */
  void manuallySetShips(PlayerSet set) {
    ArrayList<CaseType> shipListSelector = set.CopyOfShipAlive();
      while (set.notAllShipsPlaced()) {
        CaseType curShip = shipSelector(set, shipListSelector, scan);

        // * first coords
        int[] coords = userInputFirstPlacementCoords(set, curShip, scan);
        int x1 = coords[0];
        int y1 = coords[1];

        // * 2nd coords
        switch (Configuration.instance().lang()) {
          case "fr":
            System.out.println(
              "\nVeuillez saisir les coordonnées de l'autre extrémités du " +
              curShip.fullName()
            );
            System.out.println(
              "La taille du " +
              curShip.fullName() +
              " est de " +
              curShip.ShipSize() +
              " cases"
            );
            break;
          default:
            System.out.println("\nPlease enter the coordinates of the other end of " + curShip.fullName());
            System.out.println( "the size of " + curShip.fullName() + " is " + curShip.ShipSize() + " squares");
            break;
        }
        int x2 = -1, y2 = -1;

        while (!set.setShip(curShip, x1, y1, x2, y2)) {
          coords = userInputSecondPlacementCoords(set, curShip, scan, x1, y1);
          x2 = coords[0];
          y2 = coords[1];
        }

        shipListSelector.remove(curShip);
        set.incrementNbShipAlive();
        set.printShipSet();
      }
  }

    /**
   * Prompts the user to enter the coordinates of the first placement of a ship.
   *
   * @param ship The ship to be placed.
   * @param scan The scanner to use for user input.
   * @return The coordinates of the first placement of the ship.
   */
  int[] userInputFirstPlacementCoords(PlayerSet set, CaseType ship, Scanner scan) {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println(
          "\nVeuillez saisir les coordonnées de l'une des extrémités de la position du" +
          ship.fullName() +
          " (saisissez la lettre puis le numéro comme suivant: \"H5\")"
        );
        break;
      default:
        System.out.println(
          "\nPlease enter the coordinates of one end of " +
          ship.fullName() +
          " position (enter the letter and then the number as follows: \"H5\")"
        );
        break;
    }

    int x = -1, y = -1;
    while (!set.coordInGrid(x, y)) {
      /**
       * Converts a line of text to an array of coordinates.
       * @param line The line of text to convert.
       * @return The array of coordinates.
       */
      int[] coords = lineToCoords(set, scan);
      x = coords[0];
      y = coords[1];
    }
    return new int[] { x, y };
  }

  /**
   * Reads the user input for the placement of a ship.
   * @param ship The ship to place.
   * @param scan The scanner to read the user input.
   * @param x1 The x coordinate of the first cell of the ship.
   * @param y1 The y coordinate of the first cell of the ship.
   * @return The coordinates of the second cell of the ship.
   */
  int[] userInputSecondPlacementCoords(PlayerSet set, CaseType ship, Scanner scan, int x1, int y1) {
    int x, y;
    int[] coords = lineToCoords(set, scan);
    x = coords[0];
    y = coords[1];

    if (x == -1 && y == -1) {
      return coords;
    }

    if (x != x1 && y != y1) {
      ErrorMessage.diagonallyBoatPlaced();
      return new int[] { -1, -1 };
    }
    return coords;
  }

  /**
   * Converts a line of text to a coordinate.
   * @param scan The scanner to read the line from.
   * @return The coordinate, or {-1, -1} if the line is invalid.
   */
  int[] lineToCoords(PlayerSet set, Scanner scan) {
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
        if (
          line.length() == 3 && Integer.parseInt(line.charAt(2) + "") == 0
        ) x = 9; else if (line.length() == 2) x = 1; // if user type text like 'A10'
      }
      if (line.length() == 2) { // if user type text like 'H5'
        x = Integer.parseInt(line.charAt(1) + "") - 1;
      }

      // Print error message if coordinates are not in the grid
      if (!set.coordInGrid(x, y)) {
        ErrorMessage.coordsOutofGrid();
        return new int[] { -1, -1 };
      }
    } catch (Exception e) { // if user input isn't correct
      ErrorMessage.incorrectCoords();
      return new int[] { -1, -1 };
    }

    return new int[] { x, y };
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
   * Selects a ship from a list of ships.
   * @param ships The list of ships to select from.
   * @param scan The scanner to use to get the user input.
   * @return The selected ship.
   */
  CaseType shipSelector(PlayerSet set, ArrayList<CaseType> ships, Scanner scan) {
    int i = 1;
    Iterator<CaseType> it = ships.iterator();

    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("Liste des batiments à placer");
        break;
      default:
        System.out.println("List of warships to place");
        break;
    }
    // Display of the ships name and the number of ships reamaining to be placed
    while (it.hasNext()) {
      System.out.print(i + ") " + it.next().fullName() + "\t");
      if (i % 3 == 0) {
        System.out.println();
      }
      ++i;
    }

    if (i % 3 != 0) {
      System.out.println("");
    }
    int shipNumber = 0;
    while (shipNumber < 1 || shipNumber > ships.size() - set.nbShipAlive()) {
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.print("Entrez un numéro > ");
          break;
        default:
          System.out.print("Select ship number > ");
          break;
      }
      try {
        String line = scan.nextLine();
        shipNumber = Integer.parseInt(line);

        if (shipNumber < 1 || shipNumber > ships.size()) {
          ErrorMessage.numberNotPresentInList();
          shipNumber = 0;
        }
      } catch (Exception e) {
        ErrorMessage.numberNotPresentInList();
        shipNumber = 0;
      }
    }

    CaseType shipSelected = ships.get(shipNumber - 1);
    return shipSelected;
  }

  /**
  * Asks the user to choose between manually filling the grid or choosing a random placement.
  * @param scan The scanner used to get the user's input.
  * @return The user's choice.
   */
  String userChoiceGeneration(Scanner scan) {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("Voulez-vous remplir la grille manuellement ou non ?\n1) Manuellement\t2) Choisir un placement aléatoire");
        break;

      default:
        System.out.println("Do you want to fill the grid manually or not?\n1) Manually\t2) Choose a random placement");
        break;
    }

    while (true) {
      switch (Configuration.instance().lang()) {
        case "fr":
          System.out.print("Choix > ");
          break;

        default:
          System.out.print("Choice > ");
          break;
      }
      try {
        String line = scan.nextLine();

        if (Integer.parseInt(line) == 1)
          return "manually";
        else if (Integer.parseInt(line) == 2)
          return "random";
        else {
          ErrorMessage.nonCorrectAnswer();
        }
      }
      catch (Exception e) {
        ErrorMessage.nonCorrectAnswer();
      }
    }
  }

  public void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

}
