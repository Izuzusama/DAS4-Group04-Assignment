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
  public String[] run(String[] data) throws Exception {
    throw new Exception("This is not implemented. Please use byte[][] variant.");
  }
  // data[0] Array of bytes
  // data[0][0] bytes of Images
  // data2 not used
  public byte[][] run(byte[][] data, String[] data2) throws Exception {
    // If Simulate is on, use hard coded value
    if(Server.p.getProperty("image_analytics_simulate").equals("0")){
      return ExecuteImageAnalytics(data);
    }
    return ExecuteImageAnalyticsSimulate();
  }
  // Execute tensor flow detect object
  private byte[][] ExecuteImageAnalytics(byte[][] data) throws Exception{
    // Generate CSV file
    File csvFile = new File("out.csv");
    ArrayList<String> cmdA = new ArrayList<>();
    cmdA.add("java");
    cmdA.add("-jar");
    cmdA.add("detect-object.jar");
    cmdA.add(Server.p.getProperty("image_analytics_model_dir"));
    cmdA.add(Server.p.getProperty("image_analytics_label"));
    cmdA.add(csvFile.toString());
    try {
      // Write the image to disk
      File basePath = new File("image_analytics");
      basePath.mkdir();
      File dir = Files.createTempDirectory(basePath.toPath(), "image_analytics").toFile();
      for (int i = 0; i < data.length; i++) {
        File f = new File(dir, i + ".png");
        byte[] b = data[i];
        try (FileOutputStream fos = new FileOutputStream(f)) {
          fos.write(b);
        }
      // Add to command
        cmdA.add(f.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    try {
      String[] cmd = new String[cmdA.size()];
      cmdA.toArray(cmd);
      System.out.println(Arrays.toString(cmd));
      // Call the tensor flow jar file to process images
      ProcessBuilder ps = new ProcessBuilder(cmd);
      ps.redirectErrorStream(true);
      Process pr = ps.start();
      // Read the output stream
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
          System.out.println(line);
      }
      in.close();
      pr.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    try {
      // Read the csv file and return it
      return new byte[][] { Files.readAllBytes(csvFile.toPath()) };
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
  private byte[][] ExecuteImageAnalyticsSimulate(){
    return new byte[][]{"cat,15\ndog,11\nturtle,22\nbob,100\norrange,3".getBytes()};
  }
}