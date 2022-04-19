package global;

import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.InputStream;

public class Configuration {
  private static Configuration instance = null;
  Properties prop;

  public static InputStream load(String name) {
    return ClassLoader.getSystemClassLoader().getResourceAsStream(name);
  }

  private Configuration() {
    prop = new Properties();
    try {
      InputStream propIn = load("default.cfg");
      prop.load(propIn);
      if (prop.isEmpty()) {
        //* Get home file configuration
        String home = System.getProperty("user.home");
        FileInputStream f = new FileInputStream(home + "/.bataille-naval");
        prop = new Properties(prop);
        prop.load(f);
        f.close();
      }
    } catch (Exception e) {
      System.err.println("Impossible de charger le fichier de configuration");
    }
  }

  public static Configuration instance() {
    if (instance == null)
      instance = new Configuration();
    return instance;
  }

  public String lang() {
    return prop.getProperty("Lang");
  }

  public String read(String key) {
    String res = prop.getProperty(key);
    if (res == null)
      throw new NoSuchElementException("Property " + key + " not defined!");
    return res;
  }

  public Logger logger() {
    Logger log = Logger.getLogger("BatailleNaval.Logger");
    log.setLevel(Level.parse(read("LogLevel")));
    return log;
  }
}
