package compiler.compiletime;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Wrappery pro konzistentni chovani parsovani
 */
public class DataTypeParseUtils {

    public static Integer getIntegerOrDefault(String input) {
        try {
            return NumberUtils.createInteger(input);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Float getFloatOrDefault(String input) {
        try {
            return NumberUtils.createFloat(input);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Boolean getBooleanOrDefault(String input) {
        return BooleanUtils.toBooleanObject(input);
    }


}
