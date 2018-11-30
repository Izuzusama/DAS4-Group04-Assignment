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
  private String clientHost(){
    try {
      return getClientHost();
    } catch (ServerNotActiveException e) {
      e.printStackTrace();
      return "UNKNOWN";
    }
  }
  public Boolean RegisterMeAsService(String[] services) throws RemoteException {
    try {
      getClientHost();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Unable to get host ip.");
      return false;
    }
    Log(MessageFormat.format("Request: {0}", clientHost()));
    for (IServiceNode sn : serviceNodes) {
      if (sn.getIp().equals(clientHost())) {
        Log(MessageFormat.format("IP: {0} already added", clientHost()));
        return false;
      }
    }
    ServiceNode serviceNode = new ServiceNode();
    serviceNode.Ip = clientHost();
    serviceNode.Services = services;
    for (String s : services) {
      ArrayList<IServiceNode> sn = serviceDict.get(s);
      if (sn == null) {
        sn = new ArrayList<IServiceNode>();
        serviceDict.put(s, sn);
      }
      sn.add(serviceNode);
    }
    Log(MessageFormat.format("IP: {0} added with services {1}", clientHost(), Arrays.toString(services)));
    serviceNodes.add(serviceNode);
    return true;
  }

  public IServiceNode[] GetMeService(String service) throws RemoteException{
    Log(MessageFormat.format("Request: {0} querying {1}", clientHost(), service));
    ArrayList<IServiceNode> sn = serviceDict.get(service);
    if(sn == null) return new IServiceNode[0];
    return sn.toArray(new IServiceNode[sn.size()]);
  }

  public IServiceNode[] GetMeServices(String[] services) throws RemoteException {
    Log(MessageFormat.format("Request: {0} querying {1}", clientHost(), services));
    ArrayList<IServiceNode> sns = new ArrayList<>();
    for (String s : services) {
      ArrayList<IServiceNode> sn = serviceDict.get(s);
      if (sn != null) {
        sns.addAll(sn);
      }
    }
    return MergeServiceNodes(sns);
  }

  public Boolean RemoveMe() throws RemoteException, ServerNotActiveException {
    try {
      getClientHost();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Unable to get host ip.");
      return false;
    }
    IServiceNode removeNode = null;
    for (IServiceNode sn : serviceNodes) {
      if (sn.getIp() != clientHost()) {
        return false;
      } else {
        removeNode = sn;
        break;
      }
    }
    Log(MessageFormat.format("IP: {0} removed", clientHost()));
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
  
  // public void PrintInfo(){
  //   System.out.print("\033[H\033[2J");
  //   for (IServiceNode sn : serviceNodes) {
  //     System.out.println(MessageFormat.format("Server {0}", sn.Ip));
  //     System.out.println("===");
  //     for (String service : sn.Services) {
  //       System.out.print(service);
  //     }
  //     System.out.println();
  //     System.out.println("===");
  //   }
  // }
}