import java.rmi.Naming;
import java.text.MessageFormat;
import java.util.Properties;

public class CallbackServer {
  ICallback cb;
  Properties p;
  CallbackEvent cbe;

  public CallbackServer(Properties p, CallbackEvent cbe) {
    this.p = p;
    this.cbe = cbe;
  }

  public void start() {
    try {
      // Startup the RMI callback server
      cb = new Callback(this.cbe);
      System.out.println("Server Ready");
      System.setProperty("java.rmi.server.hostname", p.getProperty("rmi_registry_host"));
      Naming.rebind(MessageFormat.format("rmi://{0}:{1}/Callback", p.getProperty("rmi_registry_host"),
          p.getProperty("rmi_registry_port")), cb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}