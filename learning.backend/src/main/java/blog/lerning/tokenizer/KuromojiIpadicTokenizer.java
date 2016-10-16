package blog.lerning.tokenizer;

import com.atilika.kuromoji.ipadic.Token;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mao.instantlife at gmail.com
 */
public class KuromojiIpadicTokenizer implements Tokenizer {
    private static com.atilika.kuromoji.ipadic.Tokenizer KUROMOJI_TOKENIZER = new com.atilika.kuromoji.ipadic.Tokenizer();
    private List<Token> tokens;
    private int index;
    private TokenPreProcess preProcess;

    public KuromojiIpadicTokenizer(String toTokenize) {
        tokens = KUROMOJI_TOKENIZER.tokenize(toTokenize);
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
