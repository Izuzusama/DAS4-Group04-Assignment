import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;

public class TrackerHandler {
  Properties p;
  ArrayList<TrackerInterface> trackers;

  public TrackerHandler(Properties p) {
    this.p = p;
  }

  public Boolean Init() {
    String[] trackerHosts = p.get("trackers").toString().split(",");
    trackers = new ArrayList<>();
    for (int i = 0; i < trackerHosts.length; i++) {
      String tracker = trackerHosts[i];
      try {
        TrackerInterface ti = (TrackerInterface) Naming
            .lookup(MessageFormat.format("rmi://{0}/TrackerService", tracker));
        System.out.println(MessageFormat.format("Connected to tracker: {0}", tracker));
        trackers.add(ti);
      } catch (NotBoundException | RemoteException e) {
        e.printStackTrace();
        System.err.println(MessageFormat.format("Unable to connect to tracker with host: {0}.", tracker));
      } catch (MalformedURLException e) {
        e.printStackTrace();
        System.err.println(MessageFormat.format("The tracker host configured is not valid: {0}.", tracker));
      }
    }
    if (trackers.size() == 0) {
      System.err.println("No tracker is connected. Unable to continue.");
      return false;
    }
    return true;
  }

  public IServiceNode[] Query(String service) {
    for (TrackerInterface tracker : trackers) {
      try {
        IServiceNode[] serviceNodes = (IServiceNode[])tracker.GetMeService(service);
        if (serviceNodes.length != 0) {
          return serviceNodes;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}