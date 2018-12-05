import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class Server {
  static Registry r;
  static Properties p;
  public static void main(String[] args) {
   Properties p = new Properties();
   Server.p = p;
   // Load the config
   File file = new File("server.config.properties");
   if (!file.exists()) {
     try {
       System.out.println("Cant find properties file. Writing default.");
       FileOutputStream out = new FileOutputStream("server.config.properties");
       p.put("tracker", "localhost:1099");
       p.put("services", "VideoAnalytics,VideoSplit,ImageAnalytics,ImageAnalyticsGraph");
       p.put("rmi_registry_host", "localhost");
       p.put("rmi_registry_port", "1000");
       p.put("image_analytics_model_dir", "models/ssd_inception_v2_coco_2017_11_17/saved_model");
       p.put("image_analytics_label", "labels/mscoco_label_map.pbtxt");
       p.put("image_analytics_simulate", "0");
       p.put("ffmpeg_command", "./ffmpeg");
       p.store(out, null);
     } catch (Exception e) {
       System.err.println("Unable to save file server.config.properties");
       e.printStackTrace();
       return;
     }
   } else {
     try {
      // If cant load config
       FileInputStream in = new FileInputStream("server.config.properties");
       p.load(in);
     } catch (Exception e) {
       System.err.println("Unable to open file server.config.properties");
       e.printStackTrace();
       return;
     }
   }
   try {
    // Set hostname of registry to {hostname} or not it will use first network interface and cause problems
     System.setProperty("java.rmi.server.hostname",p.getProperty("rmi_registry_host"));
     // Parse RMI Registry port
     int port = Integer.parseInt(p.get("rmi_registry_port").toString());
     // Create a local registry so user does not need to
     r = LocateRegistry.createRegistry(port);
   } catch (Exception e) {
     e.printStackTrace();
     return;
   }
   // Start server
   new ServerService(p).run();
  }
}
