import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Random testy okolo funkcionality StringUtils a NumberUtils
 */
public class StringUtilsTests {

    @Test
    public void numberUtilsTest1() {
        Assert.assertEquals(Float.valueOf(.12f), NumberUtils.createFloat(".12"));
    }
}
