public class ServiceNode implements IServiceNode {
  public String Ip;
  public int Port; 
  public String[] Services;

  public String getIp(){
    return Ip;
  }
  public int getPort(){
    return Port;
  }
  public String[] getService(){
    return Services;
  }

  @Override
  public int hashCode() {
    int hc = 0;
    for (String s : Services) {
      hc += s.hashCode();
    }
    return Ip.hashCode() + hc;
  }
}