package view;

import global.Configuration;

public class ErrorMessage {

  public static void coordsNotEqualsBoatSize() {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println(
          "Les coordonnées ne correspondent pas à la taille du bateau !"
        );
        break;
      default:
        System.out.println(
          "The coordinates do not correspond to the size of the warship!"
        );
        break;
    }
  }

  public static void boatSuperposition() {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println(
          "Un bateaux se situe déjà à cette endroit !"
        );
        break;
      default:
        System.out.println(
          "A warship is already located at this place!"
        );
        break;
    }
  }

  public static void nonCorrectAnswer() {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("Veuillez saisir une réponse correcte !");
        break;

      default:
        System.out.println("Please enter a correct answer!");
        break;
    }
  }

  public static void coordsOutofGrid() {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("Les coordonnées sont hors de la grille !");
        break;
      default:
        System.out.println("Coordinates are outside the grid!");
        break;
    }
  }

  public static void incorrectCoords() {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("Veuillez saisir des coordonnées correctes !");
        break;
      default:
        System.out.println("Please enter correct cordinates !");
        break;
    }
  }

  public static void numberNotPresentInList() {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println("Selectionnez un numéro présent dans la liste ci-dessus !");
        break;
      default:
        System.out.println("Please enter a number in the list above!");
        break;
    }
  }

  public static void diagonallyBoatPlaced() {
    switch (Configuration.instance().lang()) {
      case "fr":
        System.out.println(
          "Les bateaux ne peuvent pas être placé en diagonales !"
        );
        break;
      default:
        System.out.println("Boats cannot be placed diagonally!");
        break;
    }
  }
}
