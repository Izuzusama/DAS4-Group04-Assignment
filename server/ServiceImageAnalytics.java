import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ServiceImageAnalytics implements IService {
  public String[] run(String[] data) {
    return null;
  }

  public byte[][] run(byte[][] data, String[] data2) {
    // Generate Files
    StringBuilder sb = new StringBuilder();
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
        sb.append(f.toPath().toAbsolutePath()).append(" ");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    File csvFile = new File("out.csv");
    try {
      String[] cmd = { "java", "-jar", "detect-object.jar", "models/ssd_inception_v2_coco_2017_11_17/saved_model",
          "labels/mscoco_label_map.pbtxt", csvFile.getAbsolutePath(), sb.toString() };
      System.out.println(Arrays.toString(cmd));
      Process proc = Runtime.getRuntime().exec(cmd);
      InputStream in = proc.getErrorStream();
      int c;
      while ((c = in.read()) != -1) {
      }
      in.close();
      proc.waitFor();
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
}