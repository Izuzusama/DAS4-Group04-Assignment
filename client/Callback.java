import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class Callback extends java.rmi.server.UnicastRemoteObject implements ICallback {
  
  Callback() throws RemoteException {
    super();
  }
  public void NewCallback(String data) throws RemoteException{
    System.out.println(data);
  }
}