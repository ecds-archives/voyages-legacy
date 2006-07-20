
public class MapscriptLoader1
{
    static {
         try {
             System.loadLibrary("mapscript"); //load native library
             System.out.println(" * mapscript native library loaded *");
         } catch (Exception e) {
             System.err.println(" * error loading native library *");
             System.err.println("Error is: " + e);
             e.printStackTrace();
         }
    }
}
