import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

class ServiceVideoSplit implements IService {
  public String[] run(String[] data) {
    return null;
  }

  // data = video file
  // data2 = filename
  public byte[][] run(byte[][] data, String[] data2) {
    // Save the file
    try {
      new File("temp/").mkdirs();
      new File("temp/" + data2[0] + "out/").mkdirs();
      try (FileOutputStream fos = new FileOutputStream("temp/" + data2[0])) {
        fos.write(data[0]);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    // split file
    try {
      final String[] cmd = { "ffmpeg", "-i", "temp/" + data2[0], "-vf", "\"scale=720:-1,fps=4\"",
          "temp/" + data2[0] + "out/out%d.png" };
      System.out.println(Arrays.toString(cmd));
      Process proc = Runtime.getRuntime().exec(cmd);
      InputStream in = proc.getErrorStream();
      int c;
      while ((c = in.read()) != -1) {}
      in.close();
      proc.waitFor();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    
    File dir = new File("temp/" + data2[0] + "out/");
    File[] files = dir.listFiles();
    byte[][] bytes = new byte[files.length][];
    for (int i = 0; i < files.length; i++) {
      try {
        bytes[i] = Files.readAllBytes(files[i].toPath());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return bytes;
  }
}