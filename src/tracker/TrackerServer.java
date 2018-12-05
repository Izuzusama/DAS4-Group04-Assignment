import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.text.MessageFormat;
import java.util.Properties;

public class TrackerServer {
  TrackerImpl t;
  Properties p;
  public TrackerServer(Properties p){
    this.p = p;
  }
  public void start() {
    try {
      t = new TrackerImpl();
      System.out.println("Server Ready");
      Naming.rebind(MessageFormat.format("rmi://localhost:{0}/TrackerService", p.get("rmi_registry_port")), t);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}