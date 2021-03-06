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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mao.instantlife at gmail.com
 */
public class Application {
    private static String LEARNING_BASE_DIR = "/opt/learning/";

    public static void main(String[] args) throws Exception {
        if(args.length <= 0) {
            throw new Exception("コーパスファイル名を指定してください。");
        }
        String inputFileName = LEARNING_BASE_DIR + "input/" + args[0];
        MTTypeCleanser cleanser = new MTTypeCleanser(inputFileName);
        SentenceIterator iter = new BasicLineIterator(cleanser.run());

        final EndingPreProcessor preProcessor = new EndingPreProcessor();
        KuromojiIpadicTokenizerFactory tokenizer = new KuromojiIpadicTokenizerFactory();

        tokenizer.setTokenPreProcessor(token -> {
            token = token.toLowerCase();
            String base = preProcessor.preProcess(token);
            base = base.replaceAll("\\d", "__NUMBER__");
            return base;
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

        String modelFileName = LEARNING_BASE_DIR + "model/model-wordvectors.txt";
        WordVectorSerializer.writeWordVectors(vec, modelFileName);

        // トークンモデル
        try(Stream<String> a = Files.lines(Paths.get(Commons.FULL_TOKEN_FILE.toURI()))) {
            String tokenRankFileName = LEARNING_BASE_DIR + "model/model-tokenrank.csv";

            Files.deleteIfExists(Paths.get(tokenRankFileName));
            Files.createFile(Paths.get(tokenRankFileName));

            Files.write(
                    Paths.get(LEARNING_BASE_DIR + "model/model-tokenrank.csv"),
                    a.collect(Collectors.groupingBy(t -> t, Collectors.counting())).entrySet().stream()
                            .map(e -> e.getKey() + "," + e.getValue().toString())
                            .collect(Collectors.toList()),
                    StandardOpenOption.APPEND);
        } catch(IOException e) {
            throw e;
        }
    }
}
