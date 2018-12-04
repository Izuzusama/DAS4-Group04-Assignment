import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Callback extends java.rmi.server.UnicastRemoteObject implements ICallback {
  CallbackEvent cbe;
  Callback(CallbackEvent cbe) throws RemoteException {
    super();
    this.cbe = cbe;
  }
  String getClientIp(){
    try {
      return getClientHost();
    } catch (Exception e) {
      return "UNKNOWN";
    }
  }
  public void NewCallback(String sn, String dt) throws RemoteException{
    cbe.broadcast(new EventArgs(){{
      serviceName = sn;
      data = dt;
      ipAddress = getClientIp();
    }});
  }
}

class EventArgs {
  String serviceName;
  String ipAddress;
  String data;
}

class CallbackEvent{
  private Set<Consumer<EventArgs>> listeners = new HashSet<>();

  public void addListener(Consumer<EventArgs> listener) {
    listeners.add(listener);
  }

  public void broadcast(EventArgs args) {
    listeners.forEach(x -> x.accept(args));
  }
}