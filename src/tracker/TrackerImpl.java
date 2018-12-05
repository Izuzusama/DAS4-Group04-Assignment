import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.rmi.server.ServerNotActiveException;

class TrackerImpl extends java.rmi.server.UnicastRemoteObject implements TrackerInterface {
  private Map<String, ArrayList<IServiceNode>> serviceDict;
  private ArrayList<IServiceNode> serviceNodes;

  TrackerImpl() throws RemoteException {
    super();
    serviceDict = new HashMap<String, ArrayList<IServiceNode>>();
    serviceNodes = new ArrayList<>();
  }

  private void Log(String log) {
    System.out.println(log);
  }
  // Register a server as a service
  public Boolean RegisterMeAsService(String[] services, int port, String ipAddr) throws RemoteException {
    Log(MessageFormat.format("Request: {0}", ipAddr));
    // Loop trough all service current service node. If the ip already registered.
    for (IServiceNode sn : serviceNodes) {
      if (sn.getIp().equals(ipAddr)) {
        Log(MessageFormat.format("IP: {0} already added.", ipAddr));
        return false;
      }
    }
    // Create a new service node
    ServiceNode serviceNode = new ServiceNode();
    serviceNode.Ip = ipAddr;
    serviceNode.Services = services;
    serviceNode.Port = port;
    // Make Map
    for (String s : services) {
      ArrayList<IServiceNode> sn = serviceDict.get(s);
      if (sn == null) {
        sn = new ArrayList<IServiceNode>();
        serviceDict.put(s, sn);
      }
      sn.add(serviceNode);
    }
    Log(MessageFormat.format("IP: {0}:{1} added with services {2}", ipAddr, port, Arrays.toString(services)));
    serviceNodes.add(serviceNode);
    return true;
  }
  // Return all serviceNodes that matches the service requested
  public IServiceNode[] GetMeService(String service) throws RemoteException{
    ArrayList<IServiceNode> sn = serviceDict.get(service);
    if(sn == null) return new IServiceNode[0];
    return sn.toArray(new IServiceNode[sn.size()]);
  }

  // Return all serviceNodes that matches the service requested
  public IServiceNode[] GetMeServices(String[] services) throws RemoteException {
    ArrayList<IServiceNode> sns = new ArrayList<>();
    for (String s : services) {
      ArrayList<IServiceNode> sn = serviceDict.get(s);
      if (sn != null) {
        sns.addAll(sn);
      }
    }
    return MergeServiceNodes(sns);
  }

  // Delist the serviceNode that matches the ipAddress
  public Boolean RemoveMe(String ipAddr) throws RemoteException, ServerNotActiveException {
    IServiceNode removeNode = null;
    for (IServiceNode sn : serviceNodes) {
      if (sn.getIp().equals(ipAddr)) {
        removeNode = sn;
        break;
      }
    }
    if(removeNode == null){
      return false;
    }
    Log(MessageFormat.format("IP: {0} removed", ipAddr));
    for (String service : removeNode.getService()) {
      serviceDict.get(service).remove(removeNode);
    }
    serviceNodes.remove(removeNode);
    return true;
  }

  private IServiceNode[] MergeServiceNodes(ArrayList<IServiceNode> services) {
    Set<IServiceNode> set = new LinkedHashSet<>();
    set.addAll(services);
    services.clear();
    services.addAll(set);
    return services.toArray(new IServiceNode[services.size()]);
  }
}