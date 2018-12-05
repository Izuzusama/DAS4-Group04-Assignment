import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.MessageFormat;

public class ServiceImpl extends java.rmi.server.UnicastRemoteObject implements IServiceInterface {
  private CallBackHandler callbackHandler;
  ServiceImpl() throws RemoteException {
    super();
  }

  private IService getServiceClass(String serviceName) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
    Class serviceClass = Class.forName("Service" + serviceName);
    return (IService) serviceClass.newInstance();
  }

  private void RegisterCallback(String host, String clientPort) throws RemoteException, NotBoundException, MalformedURLException {
    if(this.callbackHandler != null) return;
    ICallback callback = (ICallback)Naming.lookup(MessageFormat.format("rmi://{0}:{1}/Callback", host, clientPort));
    this.callbackHandler = new CallBackHandler(callback);
  }

  public String[] RunService(String service, String[] data) throws RemoteException {
    try {
      return getServiceClass(service).run(data);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Unable to run service", e);
    }
  }

  public boolean RunServiceAsync(String service, String[] data, String callbackPort) throws RemoteException {
    try {
      String clientHost = getClientHost();
      RegisterCallback(clientHost, callbackPort);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Failed to register callback. Unable to get host", e);
    }
    Thread t = new Thread(() -> {
      try {
        String[] result = RunService(service, data);
        callbackHandler.ExecuteCallback(service, result);
      } catch (Exception e) {
        e.printStackTrace();
        callbackHandler.ExecuteExceptionCallback(service, e);
      }
    });
    t.start();
    return true;
  }

  public byte[][] RunService(String service, byte[][] data, String[] data2) throws RemoteException {
    try {
      return getServiceClass(service).run(data, data2);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Unable to run service", e);
    }
  }

  public boolean RunServiceAsync(String service, byte[][] data, String[] data2, String callbackPort) throws RemoteException {
    try {
      String clientHost = getClientHost();
      RegisterCallback(clientHost, callbackPort);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Failed to register callback. Unable to get host", e);
    }
    Thread t = new Thread(() -> {
      try {
        byte[][] result = RunService(service, data, data2);
        callbackHandler.ExecuteCallback(service, result);
      } catch (Exception e) {
        e.printStackTrace();
        callbackHandler.ExecuteExceptionCallback(service, e);
      }
    });
    t.start();
    return true;
  }
}