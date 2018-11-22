import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Tracker {
  static Registry r;
  public static void main(String[] args) {
    try {
      r = LocateRegistry.createRegistry(1099);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    new TrackerServer().start();
  }
}
