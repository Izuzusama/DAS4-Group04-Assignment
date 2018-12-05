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

  // Get the service to run via Reflection
  private IService getServiceClass(String serviceName)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Class serviceClass = Class.forName("Service" + serviceName);
    return (IService) serviceClass.newInstance();
  }

  // Register client callback
  private void RegisterCallback(String host, String clientPort)
      throws RemoteException, NotBoundException, MalformedURLException {
    if (this.callbackHandler != null)
      return;
    ICallback callback = (ICallback) Naming.lookup(MessageFormat.format("rmi://{0}:{1}/Callback", host, clientPort));
    this.callbackHandler = new CallBackHandler(callback);
  }

  // Run the service (String variant). This function is synchronous.
  public String[] RunService(String service, String[] data) throws RemoteException {
    try {
      return getServiceClass(service).run(data);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Unable to run service", e);
    }
  }

  // Run the service (String variant). This function is asynchronous.
  public boolean RunServiceAsync(String service, String[] data, String callbackPort) throws RemoteException {
    try {
      // Get connecting client IP
      String clientHost = getClientHost();
      // Create new callback
      RegisterCallback(clientHost, callbackPort);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Failed to register callback. Unable to get host", e);
    }
    // Run execution in another thread so that the client does not need to wait
    Thread t = new Thread(() -> {
      try {
        // Run Service
        String[] result = RunService(service, data);
        // Call client callback when execution finished
        callbackHandler.ExecuteCallback(service, result);
      } catch (Exception e) {
        e.printStackTrace();
        // Got an exception, tell client got exception via callback
        callbackHandler.ExecuteExceptionCallback(service, e);
      }
    });
    t.start();
    return true;
  }

  // Run the service (Byte[][] variant). This function is synchronous.
  public byte[][] RunService(String service, byte[][] data, String[] data2) throws RemoteException {
    try {
      return getServiceClass(service).run(data, data2);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Unable to run service", e);
    }
  }

  // Run the service (Byte[][] variant). This function is asynchronous.
  public boolean RunServiceAsync(String service, byte[][] data, String[] data2, String callbackPort)
      throws RemoteException {
    try {
      // Get connecting client IP
      String clientHost = getClientHost();
      // Create new callback
      RegisterCallback(clientHost, callbackPort);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RemoteException("Failed to register callback. Unable to get host", e);
    }
    // Run execution in another thread so that the client does not need to wait
    Thread t = new Thread(() -> {
      try {
        // Run Service
        byte[][] result = RunService(service, data, data2);
        // Call client callback when execution finished
        callbackHandler.ExecuteCallback(service, result);
      } catch (Exception e) {
        e.printStackTrace();
        // Got an exception, tell client got exception via callback
        callbackHandler.ExecuteExceptionCallback(service, e);
      }
    });
    t.start();
    return true;
  }
}