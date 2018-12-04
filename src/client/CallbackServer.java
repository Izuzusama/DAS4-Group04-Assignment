import java.rmi.Naming;
import java.text.MessageFormat;
import java.util.Properties;

public class CallbackServer {
  ICallback cb;
  Properties p;
  CallbackEvent cbe;
  public CallbackServer(Properties p, CallbackEvent cbe){
    this.p = p;
    this.cbe = cbe;
  }
  public void start() {
    try {
      cb = new Callback(this.cbe);
      System.out.println("Server Ready");
      Naming.rebind(MessageFormat.format("rmi://0.0.0.0:{0}/Callback", p.get("rmi_registry_port")), cb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}