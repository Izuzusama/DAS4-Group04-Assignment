import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class ServiceImageAnalytics implements IService {
  public String[] run(String[] data) {
    return null;
  }
  public byte[][] run(byte[][] data, String[] data2) {
    if(Server.p.getProperty("image_analytics_simulate") == "0"){
      return ExecuteImageAnalytics(data);
    }
    return ExecuteImageAnalyticsSimulate();
  }
  private byte[][] ExecuteImageAnalytics(byte[][] data){
    // Generate Files
    File csvFile = new File("out.csv");
    ArrayList<String> cmdA = new ArrayList<>();
    cmdA.add("java");
    cmdA.add("-jar");
    cmdA.add("detect-object.jar");
    cmdA.add(Server.p.getProperty("image_analytics_model_dir"));
    cmdA.add(Server.p.getProperty("image_analytics_label"));
    cmdA.add(csvFile.toString());
    try {
      File basePath = new File("image_analytics");
      basePath.mkdir();
      File dir = Files.createTempDirectory(basePath.toPath(), "image_analytics").toFile();
      for (int i = 0; i < data.length; i++) {
        File f = new File(dir, i + ".png");
        byte[] b = data[i];
        try (FileOutputStream fos = new FileOutputStream(f)) {
          fos.write(b);
        }
        cmdA.add(f.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    try {
      String[] cmd = new String[cmdA.size()];
      cmdA.toArray(cmd);
      System.out.println(Arrays.toString(cmd));
      ProcessBuilder ps = new ProcessBuilder(cmd);
      ps.redirectErrorStream(true);
      Process pr = ps.start();
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
          System.out.println(line);
      }
      pr.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    try {
      return new byte[][] { Files.readAllBytes(csvFile.toPath()) };
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  private byte[][] ExecuteImageAnalyticsSimulate(){
    return new byte[][]{"cat,15\ndog,11\nturtle,22\nbob,100\norrange,3".getBytes()};
  }
}