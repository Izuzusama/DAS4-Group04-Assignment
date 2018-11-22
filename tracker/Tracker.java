import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class Tracker {
  static Registry r;
  public static void main(String[] args) {
    Properties p = new Properties();
    File file = new File("tracker.config.properties");
    if (!file.exists()) {
      try {
        System.out.println("Cant find properties file. Writing default.");
        FileOutputStream out = new FileOutputStream("tracker.config.properties");
        p.put("rmi_registry_port", "1099");
        p.store(out, null);
      } catch (Exception e) {
        System.err.println("Unable to save file tracker.config.properties");
        e.printStackTrace();
        return;
      }
    } else {
      try {
        FileInputStream in = new FileInputStream("tracker.config.properties");
        p.load(in);
      } catch (Exception e) {
        System.err.println("Unable to open file tracker.config.properties");
        e.printStackTrace();
        return;
      }
    }
    try {
      int port = Integer.parseInt(p.get("rmi_registry_port").toString());
      r = LocateRegistry.createRegistry(port);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    new TrackerServer(p).start();
  }
}
