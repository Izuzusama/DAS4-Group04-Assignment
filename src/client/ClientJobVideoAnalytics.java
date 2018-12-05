import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ClientJobVideoAnalytics {
  TrackerHandler trackerHandler;
  CallbackEvent cbe;
  long startTime;
  public ClientJobVideoAnalytics(TrackerHandler trackerHandler, CallbackEvent cbe) {
    this.trackerHandler = trackerHandler;
    this.cbe = cbe;
  }

  public void run() {
    cbe.addListener((x) -> {
      callBackFromVideoAnalytics(x);
    });
    startTime = System.currentTimeMillis();
    IServiceNode[] sn = trackerHandler.Query("VideoAnalytics");
    if (sn == null) {
      System.err.println("Cant find any service with name {VideoAnalytics} quit.");
      return;
    }
    IServiceInterface service = null;
    for (IServiceNode s : sn) {
      try {
        Registry cr = LocateRegistry.getRegistry(s.getIp(), s.getPort());
        System.out.println(Arrays.toString(cr.list()));
        service = (IServiceInterface) cr.lookup("Service");
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
      File f = new File("Video.mp4");
      byte[] fileContent = Files.readAllBytes(f.toPath());
      service.RunServiceAsync("VideoAnalytics", new byte[][] { fileContent },
          new String[] { "Video.mp4" }, Client.p.getProperty("rmi_registry_port"));
    } catch (Exception e) {
      System.err.println("Unable to submit job.");
      e.printStackTrace();
    }
    System.out.println("Job submitted. Waiting for callback.");
  }

  public void callBackFromVideoAnalytics(EventArgs args) {
    if (args.serviceName.equals("VideoAnalytics")) {
      if(args.exception != null){
        System.err.println("Error Callback from VideoAnalytics Service from " + args.ipAddress);
        args.exception.printStackTrace();
        return;
      }
      System.out.println("Callback from VideoAnalytics service from " + args.ipAddress);
      try {
        File imgFile = new File("graph.png");
        try (FileOutputStream fos = new FileOutputStream(imgFile)) {
          fos.write(args.byteData[0]);
        }
        Desktop.getDesktop().open(imgFile);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("It took: " + elapsedTime + "ms");
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        System.out.println("AKA: " + minutes + " minutes.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}