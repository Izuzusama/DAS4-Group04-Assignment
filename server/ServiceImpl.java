import java.rmi.RemoteException;

public class ServiceImpl extends java.rmi.server.UnicastRemoteObject implements ServiceInterface {
  ServiceImpl() throws RemoteException {
    super();
  }
  public String RunService(String service, String data) throws RemoteException {
    try {
      Class serviceClass = Class.forName("Service" + service.toUpperCase());
      Service serviceInstance = (Service) serviceClass.newInstance();
      return serviceInstance.run(data);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}