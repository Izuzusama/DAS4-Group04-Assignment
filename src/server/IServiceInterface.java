import java.rmi.RemoteException;

public interface IServiceInterface extends java.rmi.Remote {
  public String[] RunService(String service, String[] data) throws RemoteException;
  public boolean RunServiceAsync(String service, String[] data) throws RemoteException;
  public byte[][] RunService(String service, byte[][] data, String[] data2) throws RemoteException;
  public boolean RunServiceAsync(String service, byte[][] data, String[] data2) throws RemoteException;
}