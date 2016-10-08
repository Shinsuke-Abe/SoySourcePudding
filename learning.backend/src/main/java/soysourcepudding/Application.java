package soysourcepudding;

import blog.lerning.MTTypeCleanser;
import blog.lerning.tokenizer.KuromojiIpadicTokenizerFactory;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.EndingPreProcessor;

import java.io.IOException;

/**
 * @author mao.instantlife at gmail.com
 */
public class Application {
    public static void main(String[] args) throws IOException {
        MTTypeCleanser cleanser = new MTTypeCleanser("/Users/mao/Documents/SoySourcePudding/src/main/resources/mao-instantlife.hatenablog.com.export.txt");
        SentenceIterator iter = new BasicLineIterator(cleanser.run());

        final EndingPreProcessor preProcessor = new EndingPreProcessor();
        KuromojiIpadicTokenizerFactory tokenizer = new KuromojiIpadicTokenizerFactory();

        tokenizer.setTokenPreProcessor(new TokenPreProcess() {
            @Override
            public String preProcess(String token) {
                token = token.toLowerCase();
                String base = preProcessor.preProcess(token);
                base = base.replaceAll("\\d", "__NUMBER__");
                return base;
            }
        });

        int batchSize = 1000;
        int iterations = 5;
        int layerSize = 150;

        Word2Vec vec = new Word2Vec.Builder()
                .batchSize(batchSize)
                .minWordFrequency(5)
                .useAdaGrad(false)
                .layerSize(layerSize)
                .iterations(iterations)
                .seed(1)
                .windowSize(5)
                .learningRate(0.025)
                .minLearningRate(1e-3)
                .negativeSample(10)
                .iterate(iter)
                .tokenizerFactory(tokenizer)
                .workers(6)
                .build();
        vec.fit();


        WordVectorSerializer.writeWordVectors(vec, "/Users/mao/Documents/SoySourcePudding/src/main/resources/model-wordvectors.txt");
    }
}
