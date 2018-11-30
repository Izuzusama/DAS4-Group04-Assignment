import java.rmi.Naming;
import java.text.MessageFormat;
import java.util.Properties;

public class CallbackServer {
  Callback cb;
  Properties p;
  public CallbackServer(Properties p){
    this.p = p;
  }
  public void start() {
    try {
      cb = new Callback();
      System.out.println("Server Ready");
      Naming.rebind(MessageFormat.format("rmi://localhost:{0}/Callback", p.get("rmi_registry_port")), cb);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}