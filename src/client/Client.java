import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class Client {
  static Registry r;
  static TrackerHandler trackerHandler;
  static CallbackEvent cbe;
  static Properties p;

  public static void main(String[] args) {
    p = new Properties();
    File file = new File("client.config.properties");
    if (!file.exists()) {
      try {
        System.out.println("Cant find properties file. Writing default.");
        // Load default config
        FileOutputStream out = new FileOutputStream("client.config.properties");
        p.put("trackers", "localhost:1099");
        p.put("rmi_registry_host", "localhost");
        p.put("rmi_registry_port", "1088");
        p.store(out, null);
      } catch (Exception e) {
        System.err.println("Unable to save file client.config.properties");
        e.printStackTrace();
        return;
      }
    } else {
      try {
        // Load config
        FileInputStream in = new FileInputStream("client.config.properties");
        p.load(in);
      } catch (Exception e) {
        System.err.println("Unable to open file client.config.properties");
        e.printStackTrace();
        return;
      }
    }
    try {
      // Parse RMI Registry port
      int port = Integer.parseInt(p.get("rmi_registry_port").toString());
      // Create a local registry for callback so user does not need to
      r = LocateRegistry.createRegistry(port);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    // Setup callback
    cbe = new CallbackEvent();
    new CallbackServer(p, cbe).start();
    // Setup tracker connection
    trackerHandler = new TrackerHandler(p);
    if (!trackerHandler.Init()) {
      System.exit(1);
    }
    // Run job
    new ClientJobVideoAnalytics(trackerHandler, cbe).run();
  }
}
