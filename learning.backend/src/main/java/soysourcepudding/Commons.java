package soysourcepudding;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.io.File.createTempFile;

/**
 * @author mao.instantlife at gmail.com
 */
public class Commons {
    public static File FULL_TOKEN_FILE;

    public static List UNUSABLE_SPEECHLEVEL = Arrays.asList(
            "記号",
            "助動詞",
            "助詞",
            "連体詞",
            "接頭詞",
            "副詞"
    );

    static {
        try {
            FULL_TOKEN_FILE = createTempFile("full_token_", ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
