import java.rmi.Naming;

public class TrackerServer {
  public static void start() {
    try {
      TrackerImpl t = new TrackerImpl();
      System.out.println("Server Ready");
      Naming.rebind("rmi://localhost/TrackerService", t);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}