package blog.lerning.tokenizer;

import com.atilika.kuromoji.ipadic.Token;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static soysourcepudding.Commons.FULL_TOKEN_FILE;
import static soysourcepudding.Commons.UNUSABLE_SPEECHLEVEL;

/**
 * @author mao.instantlife at gmail.com
 */
public class KuromojiIpadicTokenizer implements Tokenizer {
    private static com.atilika.kuromoji.ipadic.Tokenizer KUROMOJI_TOKENIZER = new com.atilika.kuromoji.ipadic.Tokenizer();
    private List<Token> tokens;
    private int index;
    private TokenPreProcess preProcess;

    public KuromojiIpadicTokenizer(String toTokenize) throws IOException {
        tokens = KUROMOJI_TOKENIZER.tokenize(toTokenize);
        Files.write(
                Paths.get(FULL_TOKEN_FILE.toURI()),
                tokens.stream()
                        .filter(t -> !(UNUSABLE_SPEECHLEVEL.contains(t.getPartOfSpeechLevel1())))
                        .filter(t -> !(t.getPartOfSpeechLevel1().equals("動詞") && t.getSurface().length() == 1))
                        .map(t -> t.getSurface())
                        .collect(Collectors.toList()),
                StandardOpenOption.APPEND);
        index = tokens.isEmpty() ?  -1:0;
    }

    @Override
    public boolean hasMoreTokens() {
        if(index < 0) {
            return false;
        } else {
            return index < tokens.size();
        }
    }

    @Override
    public int countTokens() {
        return tokens.size();
    }

    @Override
    public String nextToken() {
        if (index < 0) {
            return null;
        }

        Token tok = tokens.get(index);
        index++;

        if(preProcess != null) {
            return preProcess.preProcess(tok.getSurface());
        } else {
            return tok.getSurface();
        }
    }

    @Override
    public List<String> getTokens() {
        List<String> ret = new ArrayList<String>();
        while(hasMoreTokens()) {
            ret.add(nextToken());
        }
        return ret;
    }

    @Override
    public void setTokenPreProcessor(TokenPreProcess tokenPreProcessor) {
        this.preProcess = tokenPreProcessor;
    }
}
