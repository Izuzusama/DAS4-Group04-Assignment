import java.rmi.RemoteException;

public interface ICallback extends java.rmi.Remote {
  public void NewCallback(String serviceName, String data) throws RemoteException;
}