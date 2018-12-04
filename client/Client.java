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
  public static void main(String[] args) {
    // Setup callback
    Properties p = new Properties();
    File file = new File("client.config.properties");
    if (!file.exists()) {
      try {
        System.out.println("Cant find properties file. Writing default.");
        FileOutputStream out = new FileOutputStream("client.config.properties");
        p.put("trackers", "localhost");
        p.put("rmi_registry_port", "1099");
        p.store(out, null);
      } catch (Exception e) {
        System.err.println("Unable to save file client.config.properties");
        e.printStackTrace();
        return;
      }
    } else {
      try {
        FileInputStream in = new FileInputStream("client.config.properties");
        p.load(in);
      } catch (Exception e) {
        System.err.println("Unable to open file client.config.properties");
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
    cbe = new CallbackEvent();
    new CallbackServer(p, cbe).start();
    trackerHandler = new TrackerHandler(p);
    if (!trackerHandler.Init()) {
      return;
    }
    new ClientJobVideoAnalytics(trackerHandler, cbe).run();
  }
}
