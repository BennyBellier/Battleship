import global.Configuration;
import model.GameMaster;

public class App {
    public static void main(String[] args) throws Exception {
      switch (Configuration.instance().read("Lang")) {
        case "fr":
          System.out.println("Battaile Navale!");
          break;

        default:
          System.out.println("Battleship!");
          break;
      }

      GameMaster gm = new GameMaster(1);
    }
}
