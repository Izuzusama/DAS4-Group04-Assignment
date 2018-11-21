public class ServiceNode {
  public String Ip;
  public String[] Services;

  @Override
  public int hashCode() {
    int hc = 0;
    for (String s : Services) {
      hc += s.hashCode();
    }
    return Ip.hashCode() + hc;
  }
}