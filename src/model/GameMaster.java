package model;

import global.Configuration;

public class GameMaster {
  int playerTurn;
  PlayerSet p1, p2;

  /**
   *  Initiate the game
   * @param nop Number of player (1 o 2)
   */
  public GameMaster(int nop) {
    p1 = new PlayerSet();
    p2 = new PlayerSet();
    p1.shipPlacement();
    if(nop == 2) {
      // player palce ship
    }
    else if (nop == 1) {
      // Random placement
    }
    else {
      Configuration.instance().logger().warning("Game can only be play by one or two players");
    }
  }

}
