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

  public void NewCallback(String sn, String[] dt) throws RemoteException{
    cbe.broadcast(new EventArgs(){{
      serviceName = sn;
      data = dt;
      ipAddress = "";
    }});
  }
  public void NewCallback(String sn, byte[][] dt) throws RemoteException{
    cbe.broadcast(new EventArgs(){{
      serviceName = sn;
      byteData = dt;
      ipAddress = "";
    }});
  }
  public void NewExceptionCallback(String sn, Exception ex){
    cbe.broadcast(new EventArgs(){{
      serviceName = sn;
      ipAddress = "";
      exception = ex;
    }});
  }
}

class EventArgs {
  String serviceName;
  String ipAddress;
  String[] data;
  byte[][] byteData;
  Exception exception;
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