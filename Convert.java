import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class Convert {

    //  GPX to CSV conversion
    public static void convertFile(String filename) throws FileNotFoundException, IOException {
        try {
          
            File gpxFile = new File(filename);

            // GPX file to Document object
            Document doc = parseGpxFile(gpxFile);

            //  <trkpt> elements
            NodeList trackPoints = doc.getElementsByTagName("trkpt");

            // Create a new CSV file to write the output
            BufferedWriter writer = new BufferedWriter(new FileWriter("triplog.csv"));

            // header for CSV file
            writer.write("Time,Latitude,Longitude\n");

         
            int timeInMinutes = 0;

            // Loop each <trkpt> element
            for (int i = 0; i < trackPoints.getLength(); i++) {
                Node node = trackPoints.item(i);

                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element trackPoint = (Element) node;

                    //latitude and longitude
                    String lat = trackPoint.getAttribute("lat");
                    String lon = trackPoint.getAttribute("lon");

                   
                    String cleanedLat = cleanCoordinate(lat);
                    String cleanedLon = cleanCoordinate(lon);

                    //Add data to CSV file
                    writer.write(timeInMinutes + "," + cleanedLat + "," + cleanedLon + "\n");

                 
                    timeInMinutes = timeInMinutes + 5; // Increment time by 5 minutes
                }
            }

            
            writer.close();

        } catch (Exception e) {
            // If something goes wrong, print the error
            System.out.println("Something went wrong!");
            e.printStackTrace();
        }
    }

   
    private static Document parseGpxFile(File gpxFile) throws Exception {
     
        FileInputStream fileStream = new FileInputStream(gpxFile);

        // Create a DocumentBuilderFactory to make a DocumentBuilder
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

       
        Document doc = dBuilder.parse(fileStream);
        return doc;
    }

    // coordinate string clean up
    private static String cleanCoordinate(String coordinate) {
        // question marks and space removal
        String cleaned = coordinate.replaceAll(" ", ""); //  spaces
        cleaned = cleaned.replaceAll("\\?", ""); //  question marks
        return cleaned;
    }
}