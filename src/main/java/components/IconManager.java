package components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Map;

/**
 * @Description
 *              This amazing piece of code loads svg paths from json file. Glory
 *              to json
 */
public class IconManager {

    private static Map<String, String> icons;

    static {
        try (InputStream is = IconManager.class.getResourceAsStream("/icons/icons.json")) {
            ObjectMapper mapper = new ObjectMapper();
            icons = mapper.readValue(is, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPath(String iconName) {
        return icons.get(iconName);
    }
}