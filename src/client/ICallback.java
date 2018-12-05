import java.rmi.RemoteException;

public interface ICallback extends java.rmi.Remote {
  public void NewCallback(String serviceName, String[] data) throws RemoteException;
  public void NewCallback(String serviceName, byte[][] data) throws RemoteException;
  public void NewExceptionCallback(String serviceName, Exception exception) throws RemoteException;
}