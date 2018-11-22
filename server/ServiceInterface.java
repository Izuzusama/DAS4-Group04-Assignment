import java.rmi.RemoteException;

public interface ServiceInterface extends java.rmi.Remote {
  public String RunService(String service, String data) throws RemoteException;
}