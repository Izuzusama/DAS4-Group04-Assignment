import java.io.Serializable;

public interface IServiceNode extends Serializable {
  String getIp();

  int getPort();

  String[] getService();
}