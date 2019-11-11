import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

public class BloomFilterTest {
    public static void main(String[] args) {
        for (var j = 0; j < 20; j++) {
            var b = new BloomFilter(100000 * 8, j);
            for (var i = 0; i < 100000; i++)
                b.addElement(randomString());
            var count = 0;
            for (var i = 0; i < 100000; i++) {
                if (b.isElement(randomString())) count++;
            }
            System.out.println((double) count / 100000);
        }
    }

    public static String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 50;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
