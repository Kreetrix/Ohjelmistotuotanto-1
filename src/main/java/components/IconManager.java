package components;

 import com.fasterxml.jackson.core.type.TypeReference;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import java.io.InputStream;
 import java.util.Map;
 import java.util.logging.Logger;

/**
 * Manages SVG icons by loading their paths from a JSON resource file.
 * <p>
 * Uses Jackson to deserialize the <code>/icons/icons.json</code> file into a map,
 * where the key is the icon name and the value is the SVG path.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 *     String svgPath = IconManager.getPath("home");
 * </pre>
 */
 public class IconManager {

     private static final Logger logger = Logger.getLogger(IconManager.class.getName());

     private IconManager(){}

     private static Map<String, String> icons;

     static {
         try (InputStream is = IconManager.class.getResourceAsStream("/icons/icons.json")) {
             ObjectMapper mapper = new ObjectMapper();
             icons = mapper.readValue(is, new TypeReference<Map<String, String>>() {
             });
         } catch (Exception e) {
            logger.severe("Failed to load icons.json: " + e.getMessage());

         }
     }

     /**
      * Returns the SVG path for the given icon name.
      *
      * @param iconName the name of the icon
      * @return the SVG path, or null if not found
      */
     public static String getPath(String iconName) {
         return icons.get(iconName);
     }
 }