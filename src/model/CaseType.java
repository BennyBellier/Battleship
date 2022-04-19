package model;

import global.Configuration;

public enum CaseType {
  AIRCRAFTCARRIER, AIRCRAFTCARRIER_HIT, BATTLESHIP, BATTLESHIP_HIT, CRUISER, CRUISER_HIT, DESTROYER, DESTROYER_HIT, SUBMARINE, SUBMARINE_HIT, AIRCRAFT, AIRCRAFT_HIT, HIT, SUNK, MISS;

  static final int AircraftCarrierSize = 5;
  static final int BattleShipSize = 4;
  static final int CruiserSize = 3;
  static final int SubmarineSize = 3;
  static final int DestroyerSize = 2;
  static final int AircraftSize = 2;

  static final String ANSI_RESET = "\u001B[0m";
  static final String ANSI_RED = "\u001B[31m";
  static final String ANSI_BLUE = "\u001B[34m";
  static final String ANSI_PURPLE = "\u001B[35m";

  public int ShipSize() {
    switch (this) {
      case AIRCRAFTCARRIER:
        return AircraftCarrierSize;
      case BATTLESHIP:
        return BattleShipSize;
      case CRUISER:
        return CruiserSize;
      case DESTROYER:
        return DestroyerSize;
      case SUBMARINE:
        return SubmarineSize;
      case AIRCRAFT:
        return AircraftSize;

      default:
        return -1;
    }
  }

  public CaseType hited() {
    switch (this) {
      case AIRCRAFTCARRIER:
        return AIRCRAFTCARRIER_HIT;
      case BATTLESHIP:
        return BATTLESHIP_HIT;
      case CRUISER:
        return CRUISER_HIT;
      case DESTROYER:
        return DESTROYER_HIT;
      case SUBMARINE:
        return SUBMARINE_HIT;
      case AIRCRAFT:
        return AIRCRAFT_HIT;

      default:
        return this;
    }
  }

  public String toString() {
    switch (Configuration.instance().read("Lang")) {
      case "fr":
        switch (this) {
          case AIRCRAFTCARRIER:
            return " P ";
          case AIRCRAFTCARRIER_HIT:
            return ANSI_RED + " P " + ANSI_RESET;
          case BATTLESHIP:
            return " C ";
          case BATTLESHIP_HIT:
            return ANSI_RED + " C " + ANSI_RESET;
          case CRUISER:
            return " O ";
          case CRUISER_HIT:
            return ANSI_RED + " O " + ANSI_RESET;
          case DESTROYER:
            return " D ";
          case DESTROYER_HIT:
            return ANSI_RED + " D " + ANSI_RESET;
          case SUBMARINE:
            return " S ";
          case SUBMARINE_HIT:
            return ANSI_RED + " S " + ANSI_RESET;
          case AIRCRAFT:
            return " A ";
          case AIRCRAFT_HIT:
            return ANSI_RED + " A " + ANSI_RESET;
          case HIT:
            return ANSI_RED + " X " + ANSI_RESET;
          case SUNK:
            return ANSI_PURPLE + " X " + ANSI_RESET;
          case MISS:
            return ANSI_BLUE + " X " + ANSI_RESET;

          default:
            return " ";
        }

      default:
        switch (this) {
          case AIRCRAFTCARRIER:
            return " T ";
          case AIRCRAFTCARRIER_HIT:
            return ANSI_RED + " T " + ANSI_RESET;
          case BATTLESHIP:
            return " B ";
          case BATTLESHIP_HIT:
            return ANSI_RED + " B " + ANSI_RESET;
          case CRUISER:
            return " C ";
          case CRUISER_HIT:
            return ANSI_RED + " C " + ANSI_RESET;
          case DESTROYER:
            return " D ";
          case DESTROYER_HIT:
            return ANSI_RED + " D " + ANSI_RESET;
          case SUBMARINE:
            return " S ";
          case SUBMARINE_HIT:
            return ANSI_RED + " S " + ANSI_RESET;
          case AIRCRAFT:
            return " A ";
          case AIRCRAFT_HIT:
            return ANSI_RED + " A " + ANSI_RESET;
          case HIT:
            return ANSI_RED + " X " + ANSI_RESET;
          case SUNK:
            return ANSI_PURPLE + " X " + ANSI_RESET;
          case MISS:
            return ANSI_BLUE + " X " + ANSI_RESET;

          default:
            return " ";
        }
    }
  }

  public String fullName() {
    switch (Configuration.instance().read("Lang")) {
      case "fr":
        switch (this) {
          case AIRCRAFTCARRIER:
            return "Porte avion";
          case BATTLESHIP:
            return "Cuirassé";
          case CRUISER:
            return "Croiseur";
          case DESTROYER:
            return "Destroyer";
          case SUBMARINE:
            return "Sous-marin";
          case AIRCRAFT:
            return "Avion";

          default:
            return " ";
        }

      default:
        switch (this) {
          case AIRCRAFTCARRIER:
            return "Carrier";
          case BATTLESHIP:
            return "BattleShip";
          case CRUISER:
            return "Cruiser";
          case DESTROYER:
            return "Destroyer";
          case SUBMARINE:
            return "Submarine";
          case AIRCRAFT:
            return "Aircraft";

          default:
            return " ";
        }
    }
  }
}
