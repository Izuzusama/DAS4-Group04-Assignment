import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class Server {
  static Registry r;

  public static void main(String[] args) {
    Properties p = new Properties();
    File file = new File("server.config.properties");
    if (!file.exists()) {
      try {
        System.out.println("Cant find properties file. Writing default.");
        FileOutputStream out = new FileOutputStream("server.config.properties");
        p.put("tracker", "localhost");
        p.put("rmi_registry", "localhost");
        p.put("services", "a,b,c,d,e");
        p.put("rmi_registry_port", "1000");
        p.store(out, null);
      } catch (Exception e) {
        System.err.println("Unable to save file server.config.properties");
        e.printStackTrace();
        return;
      }
    } else {
      try {
        FileInputStream in = new FileInputStream("server.config.properties");
        p.load(in);
      } catch (Exception e) {
        System.err.println("Unable to open file server.config.properties");
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
    new ServerService(p).run();
  }
}
