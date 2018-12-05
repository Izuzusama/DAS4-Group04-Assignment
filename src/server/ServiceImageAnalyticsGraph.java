import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;

import java.io.IOException;

public class ServiceImageAnalyticsGraph implements IService {
  public String[] run(String[] data) {
    return null;
  }

  public byte[][] run(byte[][] data, String[] data2) {
    byte[] csvStringByte = data[0];
    String csvString = new String(csvStringByte);
    String[] splitNewLineCsv = csvString.split("\n");
    PieChart chart = new PieChartBuilder().width(1920).height(1080).title("Pie Chart").build();
    chart.getStyler().setCircular(false);
    for (String s : splitNewLineCsv) {
      String[] splitStr = s.split(",");
      chart.addSeries(splitStr[0], Integer.parseInt(splitStr[1]));
    }
    byte[] imageData;
    try {
      imageData = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return new byte[][]{imageData};
  }

}