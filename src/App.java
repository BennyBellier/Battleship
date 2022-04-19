import global.Configuration;
import view.TerminalInterface;

public class App {
    public static void main(String[] args) throws Exception {
      switch (Configuration.instance().read("Interface")) {
        case "Terminal":
          TerminalInterface.start();
          break;
        case "Graphic":

          break;
        default:
          Configuration.instance().logger().severe("Interface not recognized");
          break;
      }

    }
}
