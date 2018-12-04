import java.rmi.Naming;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

public class ServerService {
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
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          try {
            System.out.println("Removing myself from tracker");
            ServerService.t.RemoveMe(properties.getProperty("rmi_registry_host"));
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      t = (TrackerInterface) Naming.lookup(MessageFormat.format("rmi://{0}/TrackerService", properties.get("tracker")));
      t.RegisterMeAsService(services, Integer.parseInt(properties.get("rmi_registry_port").toString()), properties.getProperty("rmi_registry_host"));
      System.out.println("Connected to tracker");
      IServiceInterface service = new ServiceImpl();
      Naming.rebind(MessageFormat.format("rmi://{0}/Service", properties.getProperty("rmi_registry_host") + ":" + properties.getProperty("rmi_registry_port")), service);
      System.out.println("Service Published");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}