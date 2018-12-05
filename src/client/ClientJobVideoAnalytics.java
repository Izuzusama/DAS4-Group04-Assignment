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
    // Register callback event
    cbe.addListener((x) -> {
      callBackFromVideoAnalytics(x);
    });
    // Record down start time
    startTime = System.currentTimeMillis();
    // Query for VideoAnalytics Service from tracker
    IServiceNode[] sn = trackerHandler.Query("VideoAnalytics");
    if (sn == null) {
      // If cant find any
      System.err.println("Cant find any service with name {VideoAnalytics} quit.");
      System.exit(1);
    }
    IServiceInterface service = null;
    for (IServiceNode s : sn) {
      try {
        // try and connect to server registry
        Registry cr = LocateRegistry.getRegistry(s.getIp(), s.getPort());
        service = (IServiceInterface) cr.lookup("Service");
        // Found 1 means no exception, enough break out.
        break;
      } catch (Exception e) {
        // Cant connect to that continue look for antoher server
        e.printStackTrace();
        System.out.println(MessageFormat.format("Cant get service with ip {0}. Next.", s.getIp()));
      }
    }
    // No server found
    if (service == null) {
      System.err.println("No service found! Quit.");
      System.exit(1);
    }
    try {
      // Open Video.mp4 file
      File f = new File("Video.mp4");
      // Read as bytes
      byte[] fileContent = Files.readAllBytes(f.toPath());
      // Call VideoAnalytics Service via the found server. This function is
      // asynchronous.
      service.RunServiceAsync("VideoAnalytics", new byte[][] { fileContent }, new String[] { "Video.mp4" },
          Client.p.getProperty("rmi_registry_port"));
    } catch (Exception e) {
      // Server cant do it
      System.err.println("Unable to submit job. Quit.");
      e.printStackTrace();
      System.exit(1);
    }
    // Server cant do it
    System.out.println("Job submitted. Waiting for callback.");
  }

  public void callBackFromVideoAnalytics(EventArgs args) {
    // A callback from the server with serviceName as VideoAnalytics
    if (args.serviceName.equals("VideoAnalytics")) {
      // If it is an exception, print it
      if (args.exception != null) {
        System.err.println("Error Callback from VideoAnalytics Service from " + args.ipAddress);
        args.exception.printStackTrace();
        System.exit(1);
      }
      // Else is result
      System.out.println("Callback from VideoAnalytics service from " + args.ipAddress);
      try {
        // Save the return byte[] as image file.
        File imgFile = new File("graph.png");
        try (FileOutputStream fos = new FileOutputStream(imgFile)) {
          fos.write(args.byteData[0]);
        }
        // Open the image file.
        Desktop.getDesktop().open(imgFile);
        // Print how long it takes
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("It took: " + elapsedTime + "ms");
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
        System.out.println("AKA: " + minutes + " minutes.");
        System.exit(0);
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }
}