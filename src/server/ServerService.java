import java.rmi.Naming;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

public class ServerService {
  class ShutdownThread extends Thread{
    public TrackerInterface t;
    public Properties properties;
    @Override
    public void run() {
      // Function that delist itself from tracker
      try {
        System.out.println("Removing myself from tracker.");
        t.RemoveMe(properties.getProperty("rmi_registry_host"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  Properties properties;
  String[] services;
  public static TrackerInterface t;

  ServerService(Properties p) {
    properties = p;
    p.list(System.out);
    services = p.get("services").toString().split(",");
  }

  void run() {
    try {
      // Create a new shutdown thread
      ShutdownThread st = new ShutdownThread();
      st.properties = properties;
      // Add shutdown thread to hook incase user use ctrl+c to stop the app
      Runtime.getRuntime().addShutdownHook(st);
      // Look for the tracker
      t = (TrackerInterface) Naming.lookup(MessageFormat.format("rmi://{0}/TrackerService", properties.get("tracker")));
      st.t = t;
      // Advertise itself to the tracker
      t.RegisterMeAsService(services, Integer.parseInt(properties.get("rmi_registry_port").toString()), properties.getProperty("rmi_registry_host"));
      System.out.println("Connected to tracker");
      IServiceInterface service = new ServiceImpl();
      // Create new service instance and bind it
      Naming.rebind(MessageFormat.format("rmi://{0}/Service", properties.getProperty("rmi_registry_host") + ":" + properties.getProperty("rmi_registry_port")), service);
      System.out.println("Service Published");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}