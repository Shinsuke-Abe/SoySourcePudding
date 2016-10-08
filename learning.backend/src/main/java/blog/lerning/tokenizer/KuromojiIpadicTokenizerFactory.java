package blog.lerning.tokenizer;

import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.InputStream;

/**
 * @author mao.instantlife at gmail.com
 */
public class KuromojiIpadicTokenizerFactory implements TokenizerFactory {
    private TokenPreProcess preProcess;

    @Override
    public Tokenizer create(String toTokenize) {
        if(toTokenize == null || toTokenize.isEmpty()) {
            throw new IllegalArgumentException("Unable to proceed; no sentense to tokenize");
        }

        KuromojiIpadicTokenizer ret = new KuromojiIpadicTokenizer(toTokenize);
        ret.setTokenPreProcessor(preProcess);
        return ret;
    }

    @Override
    public Tokenizer create(InputStream toTokenize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTokenPreProcessor(TokenPreProcess preProcessor) {
        this.preProcess = preProcessor;
    }

    @Override
    public TokenPreProcess getTokenPreProcessor() {
        return this.preProcess;
    }
}
