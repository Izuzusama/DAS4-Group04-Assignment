import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface TrackerInterface extends java.rmi.Remote {
  public Boolean RegisterMeAsService(String[] services, int port, String ipAddr) throws RemoteException;

  public IServiceNode[] GetMeService(String service) throws RemoteException;
  
  public IServiceNode[] GetMeServices(String[] services) throws RemoteException;

  public Boolean RemoveMe(String ipAddr) throws RemoteException, ServerNotActiveException;
}