import java.rmi.Naming;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ServiceVideoAnalytics implements IService {
  public String[] run(String[] data) throws Exception {
    throw new Exception("This is not implemented. Please use byte[][] variant.");
  }

  // data[0] = video file
  // data2[0] = filename
  public byte[][] run(byte[][] data, String[] data2) throws Exception {
    // Setup all service nodes first before executing
    ArrayList<IServiceInterface> videoSplitNode = GetServices("VideoSplit", 1);
    ArrayList<IServiceInterface> imageAnalyticsNode = GetServices("ImageAnalytics", 4);
    ArrayList<IServiceInterface> imageAnalyticsGraphNode = GetServices("ImageAnalyticsGraph", 1);
    // If one of the service have no node, tell throw error
    if (videoSplitNode == null || imageAnalyticsNode == null || imageAnalyticsGraphNode == null) {
      System.err.println("Unable to find one of the node.");
      throw new Exception("Unable to find one of the node.");
    }
    byte[][] fileBytes = GenerateVideoThumb(data[0], data2[0], videoSplitNode.get(0));
    byte[] nameToOccurance = ImageAnalytics(fileBytes, imageAnalyticsNode);
    byte[] graphImg = GraphIt(nameToOccurance, imageAnalyticsGraphNode.get(0));
    return new byte[][]{graphImg};
  }

  // Call the split video service node
  private byte[][] GenerateVideoThumb(byte[] data, String filename, IServiceInterface service) throws Exception {
    byte[][] fileBytes;
    try {
      fileBytes = service.RunService("VideoSplit", new byte[][] { data }, new String[] { filename });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return fileBytes;
  }

  // Split the task to (max of 4) nodes 
  private byte[] ImageAnalytics(byte[][] files, ArrayList<IServiceInterface> services) throws Exception {
    ArrayList<byte[]>[] splitedFiles = SplitFiles(files, services.size());
    Thread[] threads = new Thread[services.size()];
    byte[][] result = new byte[services.size()][];
    for (int i = 0; i < services.size(); i++) {
      final int index = i;
      final ArrayList<byte[]> filesToRun = splitedFiles[i];
      final IServiceInterface serviceToRunAt = services.get(i);
      // Create threads for each service to call. so can simultaneous calls
      Thread t = new Thread(() -> {
        byte[][] bts = new byte[filesToRun.size()][];
        for (int y = 0; y < filesToRun.size(); y++) {
          try {
            bts[y] = filesToRun.get(y);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        try {
          System.out.println("Running service with T: " + Thread.currentThread().getId());
          byte[][] r = serviceToRunAt.RunService("ImageAnalytics", bts, null);
          result[index] = r[0];
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      t.start();
      threads[i] = t;
    }
    // Wait for all nodes to finish
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
    // Combine the result of all nodes
    StringBuilder sb = new StringBuilder();
    Map<String, Integer> nameToOccurance = new HashMap<>();
    for (int i = 0; i < result.length; i++) {
      byte[] csvB = result[i];
      if(csvB == null){
        continue;
      }
      String csvContent = new String(csvB);
      csvContent = csvContent.replace("\r", "");
      String[] newLineSplit = csvContent.split("\n");
      for (String s : newLineSplit) {
        if (!s.equals("")) {
          String name = s.split(",")[0];
          if (nameToOccurance.get(name) == null) {
            nameToOccurance.put(name, 0);
          }
          nameToOccurance.put(name, nameToOccurance.get(name) + 1);
        }
      }
    }
    if(nameToOccurance.keySet().size() == 0){
      throw new Exception("No result from Image Analytics.");
    }
    for (String key : nameToOccurance.keySet()) {
      sb.append(key).append(",").append(nameToOccurance.get(key)).append("\n");
    }
    System.out.println("CSV Return: " + sb.toString());
    return sb.toString().getBytes();
  }

  // Call the Graph node
  private byte[] GraphIt(byte[] nameToOccuranceCsv, IServiceInterface service) throws Exception{
    byte[][] fileBytes;
    try {
      fileBytes = service.RunService("ImageAnalyticsGraph", new byte[][] { nameToOccuranceCsv }, null);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return fileBytes[0];
  }

  // Split files to nodes
  private ArrayList<byte[]>[] SplitFiles(byte[][] files, int parts) {
    ArrayList<byte[]>[] splitedFiles = new ArrayList[parts];
    for (int i = 0; i < parts; i++) {
      splitedFiles[i] = new ArrayList<byte[]>();
    }
    for (int i = 0; i < files.length; i++) {
      splitedFiles[i % parts].add(files[i]);
    }
    return splitedFiles;
  }

  private ArrayList<IServiceInterface> GetServices(String serviceName, int max) {
    ArrayList<IServiceInterface> services = new ArrayList<>();
    try {
      IServiceNode[] sn = (IServiceNode[])ServerService.t.GetMeService(serviceName);
      for (IServiceNode s : sn) {
        try {
          services.add((IServiceInterface) Naming
              .lookup(MessageFormat.format("rmi://{0}/Service", s.getIp() + ":" + s.getPort())));
          if (services.size() == max) {
            break;
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println(MessageFormat.format("Cant get service with ip {0}. Next.", s.getIp()));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    if (services.size() == 0) {
      System.err.println("Unable to fine any node named " + serviceName);
      return null;
    }
    return services;
  }
}