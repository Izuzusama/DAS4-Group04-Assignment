import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;

import java.io.IOException;

public class ServiceImageAnalyticsGraph implements IService {
  public String[] run(String[] data) throws Exception {
    throw new Exception("This is not implemented. Please use byte[][] variant.");
  }

  // data[0] = csv content
  // data2 not used
  public byte[][] run(byte[][] data, String[] data2) throws Exception {
    byte[] csvStringByte = data[0];
    String csvString = new String(csvStringByte);
    // split the csv
    String[] splitNewLineCsv = csvString.split("\n");
    // New piechart builder
    PieChart chart = new PieChartBuilder().width(800).height(600).title("Pie Chart").build();
    chart.getStyler().setCircular(false);
    // Add the items
    for (String s : splitNewLineCsv) {
      String[] splitStr = s.split(",");
      chart.addSeries(splitStr[0], Integer.parseInt(splitStr[1]));
    }
    // Convert the image bitmap to byte
    byte[] imageData;
    try {
      imageData = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
    // return it
    return new byte[][] { imageData };
  }
}