package model;

public class GameMaster {
  int playerTurn;
  int nbPlayer;
  PlayerSet p1, p2;


  /**
   * Constructor for GameMaster.
   * @param p1 The first player set.
   * @param p2 The second player set.
   */
  public GameMaster(PlayerSet p1, PlayerSet p2) {
    this.p1 = p1;
    this.p2 = p2;
    playerTurn = 1;
  }

  public void setNbPlayer(int nop) {
    nbPlayer = nop;
  }

}
