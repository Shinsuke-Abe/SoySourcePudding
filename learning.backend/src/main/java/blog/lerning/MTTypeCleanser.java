package blog.lerning;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mao.instantlife at gmail.com
 */
public class MTTypeCleanser {
    private String fileName;

    private static List<String> UNNECESESARY_HEADER = Arrays.asList(
            "AUTHOR:",
            "BASENAME:",
            "STATUS:",
            "ALLOW COMMENTS:",
            "CONVERT BREAKS:",
            "DATE:",
            "BODY:",
            "COMMENT:",
            "IP:",
            "URL:"
    );

    private static List<String> DEVIDE_LINE = Arrays.asList(
            "-----",
            "--------"
    );

    private static String TAG_PARSE_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";

    public MTTypeCleanser(String fileName) {
        this.fileName = fileName;
    }

    public File run() throws IOException {
        try(Stream<String> a = Files.lines(Paths.get(fileName))) {
            File tempFile = File.createTempFile("temporary.blog.text", ".txt");

            Files.write(
                    Paths.get(tempFile.toURI()),
                    a.map(line -> extractTitleString(line).replaceAll(TAG_PARSE_PATTERN, "").trim())
                            .filter(line -> isUnnecessaryLine(line) == false)
                            .collect(Collectors.toList()),
                    StandardOpenOption.APPEND
            );

            return tempFile;
        } catch(IOException e) {
            throw e;
        }
    }

    private boolean isUnnecessaryLine(String line) {
        if(line.isEmpty()) {
            return true;
        } else if(DEVIDE_LINE.contains(line)) {
            return true;
        } else if(UNNECESESARY_HEADER.stream().anyMatch(header -> line.startsWith(header))) {
            return true;
        } else {
            return false;
        }
    }

    private String extractTitleString(String line) {
        if(line.startsWith("TITLE:")) {
            return line.substring(6).trim();
        } else {
            return line;
        }
    }
}
