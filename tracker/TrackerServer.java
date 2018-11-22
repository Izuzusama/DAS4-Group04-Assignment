import java.rmi.Naming;

public class TrackerServer {
  TrackerImpl t;
  public void start() {
    try {
      t = new TrackerImpl();
      System.out.println("Server Ready");
      Naming.rebind("rmi://localhost/TrackerService", t);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}