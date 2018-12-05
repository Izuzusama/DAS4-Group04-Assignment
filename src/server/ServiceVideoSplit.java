import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;

class ServiceVideoSplit implements IService {
  public String[] run(String[] data) throws Exception {
    throw new Exception("This is not implemented. Please use byte[][] variant.");
  }

  // data[0] = video file
  // data2[0] = filename
  public byte[][] run(byte[][] data, String[] data2) throws Exception {
    try {
      // Save the video file
      new File("temp/").mkdirs();
      new File("temp/" + data2[0] + "out/").mkdirs();
      try (FileOutputStream fos = new FileOutputStream("temp/" + data2[0])) {
        fos.write(data[0]);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    // split file
    try {
      // Setup cmd
      String[] cmd = { Server.p.getProperty("ffmpeg_command"), "-i", "temp/" + data2[0], "-vf", "scale=720:-1,fps=4",
          "temp/" + data2[0] + "out/out%d.png" };
      System.out.println(Arrays.toString(cmd));
      ProcessBuilder ps = new ProcessBuilder(cmd);
      ps.redirectErrorStream(true);
      // Call ffmpeg to split video 4 frames per seconds
      Process pr = ps.start();
      // Read the output stream
      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }
      in.close();
      pr.waitFor();
      if (pr.exitValue() != 0)
        throw new Exception("ffmpeg return non 0 exit code.");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    // Read the output image files as byte
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