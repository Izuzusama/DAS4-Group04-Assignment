import java.rmi.RemoteException;

public class CallBackHandler{
  private ICallback callback;
  CallBackHandler(ICallback callback){
    this.callback = callback;
  }
  public boolean ExecuteCallback(String serviceName, String[] data){
    try {
      callback.NewCallback(serviceName, data);
    } catch (RemoteException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  public boolean ExecuteCallback(String serviceName, byte[][] data){
    try {
      callback.NewCallback(serviceName, data);
    } catch (RemoteException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  public boolean ExecuteExceptionCallback(String serviceName, Exception e){
    try {
      callback.NewExceptionCallback(serviceName, e);
    } catch (RemoteException ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }
}