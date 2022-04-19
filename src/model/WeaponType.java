package model;

import global.Configuration;

public enum WeaponType {
  MISSILE, TORPEDO, RADAR;

  static final int missileRadius = 1;
  static final int torpedoRadius = 1;
  static final int radarRadius = 2;

  public int weaponRadius() {
    switch (this) {
      case MISSILE:
        return missileRadius;
      case TORPEDO:
        return torpedoRadius;
      case RADAR:
        return radarRadius;

      default:
        return 0;
    }
  }

  public String toString() {
    switch (Configuration.instance().read("Lang")) {
      case "fr":
        switch (this) {
          case MISSILE:
            return "Missile";
          case TORPEDO:
            return "Torpille";
          case RADAR:
            return "Radar";

          default:
            return " ";
        }

      default:
        switch (this) {
          case MISSILE:
            return "Missile";
          case TORPEDO:
            return "Torpedo";
          case RADAR:
            return "Radar";


          default:
            return " ";
        }
    }
  }

}
