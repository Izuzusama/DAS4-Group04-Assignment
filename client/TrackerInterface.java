import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface TrackerInterface extends java.rmi.Remote {
  public Boolean RegisterMeAsService(String[] services) throws RemoteException, ServerNotActiveException;

  public ServiceNode[] GetMeServices(String[] services) throws RemoteException;

  public Boolean RemoveMe() throws RemoteException, ServerNotActiveException;
}