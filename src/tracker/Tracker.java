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
    // Load the config
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
        // If cant load config
        FileInputStream in = new FileInputStream("tracker.config.properties");
        p.load(in);
      } catch (Exception e) {
        System.err.println("Unable to open file tracker.config.properties");
        e.printStackTrace();
        return;
      }
    }
    try {
      // Parse RMI Registry port
      int port = Integer.parseInt(p.get("rmi_registry_port").toString());
      // Create a local registry so user does not need to
      r = LocateRegistry.createRegistry(port);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    // Start tracker server
    new TrackerServer(p).start();
  }
}
