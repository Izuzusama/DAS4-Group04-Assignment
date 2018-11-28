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
  private Map<String, ArrayList<ServiceNode>> serviceDict;
  private ArrayList<ServiceNode> serviceNodes;

  TrackerImpl() throws RemoteException {
    super();
    serviceDict = new HashMap<String, ArrayList<ServiceNode>>();
    serviceNodes = new ArrayList<>();
  }

  private void Log(String log) {
    System.out.println(log);
  }

  @Override
  public Boolean RegisterMeAsService(String[] services) throws RemoteException, ServerNotActiveException {
    Log(MessageFormat.format("Request: {0}", getClientHost()));
    for (ServiceNode sn : serviceNodes) {
      if (sn.Ip.equals(getClientHost())) {
        Log(MessageFormat.format("IP: {0} already added", getClientHost()));
        return false;
      }
    }
    ServiceNode serviceNode = new ServiceNode();
    serviceNode.Ip = getClientHost();
    serviceNode.Services = services;
    for (String s : services) {
      ArrayList<ServiceNode> sn = serviceDict.get(s);
      if (sn == null) {
        sn = new ArrayList<ServiceNode>();
        serviceDict.put(s, sn);
      }
      sn.add(serviceNode);
    }
    Log(MessageFormat.format("IP: {0} added with services {1}", getClientHost(), Arrays.toString(services)));
    serviceNodes.add(serviceNode);
    return true;
  }

  @Override
  public IServiceNode[] GetMeService(String service) throws RemoteException{
    ArrayList<ServiceNode> sn = serviceDict.get(service);
    return sn.toArray(new ServiceNode[sn.size()]);
  }

  @Override
  public IServiceNode[] GetMeServices(String[] services) throws RemoteException {
    ArrayList<ServiceNode> sns = new ArrayList<>();
    for (String s : services) {
      ArrayList<ServiceNode> sn = serviceDict.get(s);
      if (sn != null) {
        sns.addAll(sn);
      }
    }
    return MergeServiceNodes(sns);
  }

  @Override
  public Boolean RemoveMe() throws RemoteException, ServerNotActiveException {
    ServiceNode removeNode = null;
    for (ServiceNode sn : serviceNodes) {
      if (sn.Ip != getClientHost()) {
        return false;
      } else {
        removeNode = sn;
        break;
      }
    }
    Log(MessageFormat.format("IP: {0} removed", getClientHost()));
    for (String service : removeNode.Services) {
      serviceDict.get(service).remove(removeNode);
    }
    serviceNodes.remove(removeNode);
    return true;
  }

  private ServiceNode[] MergeServiceNodes(ArrayList<ServiceNode> services) {
    Set<ServiceNode> set = new LinkedHashSet<>();
    set.addAll(services);
    services.clear();
    services.addAll(set);
    return services.toArray(new ServiceNode[services.size()]);
  }
  
  public void PrintInfo(){
    System.out.print("\033[H\033[2J");
    for (ServiceNode sn : serviceNodes) {
      System.out.println(MessageFormat.format("Server {0}", sn.Ip));
      System.out.println("===");
      for (String service : sn.Services) {
        System.out.print(service);
      }
      System.out.println();
      System.out.println("===");
    }
  }
}