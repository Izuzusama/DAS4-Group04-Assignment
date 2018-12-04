import java.rmi.RemoteException;

public class ServiceImpl extends java.rmi.server.UnicastRemoteObject implements IServiceInterface {
  ServiceImpl() throws RemoteException {
    super();
  }

  private IService getServiceClass(String serviceName) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
    Class serviceClass = Class.forName("Service" + serviceName);
    return (IService) serviceClass.newInstance();
  }
  public String[] RunService(String service, String[] data) throws RemoteException {
    try {
      return getServiceClass(service).run(data);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public boolean RunServiceAsync(String service, String[] data) throws RemoteException {
    return false;
  }
  public byte[][] RunService(String service, byte[][] data, String[] data2) throws RemoteException {
    try {
      return getServiceClass(service).run(data, data2);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  public boolean RunServiceAsync(String service, byte[][] data, String[] data2) throws RemoteException {
    return false;
  }
}