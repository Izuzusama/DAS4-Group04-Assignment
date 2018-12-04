import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.rmi.Naming;
import java.text.MessageFormat;

public class ClientJobVideoAnalytics {
  TrackerHandler trackerHandler;
  CallbackEvent cbe;

  public ClientJobVideoAnalytics(TrackerHandler trackerHandler, CallbackEvent cbe) {
    this.trackerHandler = trackerHandler;
    this.cbe = cbe;
  }

  public void run() {
    cbe.addListener((x) -> {
      callBackFromVideoAnalytics(x);
    });
    IServiceNode[] sn = trackerHandler.Query("ImageAnalytics");
    if (sn == null) {
      System.err.println("Cant find any service with name {ImageAnalytics} quit.");
      return;
    }
    IServiceInterface service = null;
    for (IServiceNode s : sn) {
      try {
        for (String str : Naming.list("rmi://" + s.getIp() + ":" + s.getPort())) {
          System.out.println(str);
        }
        service = (IServiceInterface) Naming
            .lookup(MessageFormat.format("rmi://{0}/Service", s.getIp() + ":" + s.getPort()));
        break;
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(MessageFormat.format("Cant get service with ip {0}. Next.", s.getIp()));
      }
    }
    if (service == null) {
      System.err.println("No service found! Quit.");
      return;
    }
    try {
      File f = new File("temp/1.png");
      File f2 = new File("temp/2.png");
      byte[] fileContent = Files.readAllBytes(f.toPath());
      byte[] fileContent2 = Files.readAllBytes(f2.toPath());
      byte[][] result = service.RunService("ImageAnalytics", new byte[][] { fileContent, fileContent2 },
          new String[] { "Video.mp4" });
      if (result == null) {
        System.err.println("Unable to submit job!");
      }
      new File("temp_client/").mkdirs();
      try (FileOutputStream fos = new FileOutputStream("temp_client/" + "data.csv")) {
        fos.write(result[0]);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void callBackFromVideoAnalytics(EventArgs args) {
    if (!args.serviceName.equals("videoAnalytics")) {
      return;
    }
    System.out.println(args.data);
  }
}