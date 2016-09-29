package eu.fbk.dh.tint.readability.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static eu.fbk.dh.tint.readability.Readability.getStream;

/**
 * Created by alessio on 26/09/16.
 */
public class SpanishReadabilityModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpanishReadabilityModel.class);
    private static SpanishReadabilityModel ourInstance = null;

    private static final int LIMIT_EASY = 500;
    private static final int LIMIT_MEDIUM = 2500;
    private static final int LIMIT_HARD = 5000;

    private Set<String> level1Lemmas = new HashSet<>();
    private Set<String> level2Lemmas = new HashSet<>();
    private Set<String> level3Lemmas = new HashSet<>();

//    private HashMap<String, GlossarioEntry> glossario = new HashMap<>();
//    private HashMap<Integer, HashMultimap<String, String>> easyWords = new HashMap<>();

    public static SpanishReadabilityModel getInstance(Properties globalProperties, Properties localProperties) {
        if (ourInstance == null) {
            String freqLemmaFile = localProperties.getProperty("lemmasFile");

            LOGGER.info("Loading lemmas");
            Set<String> level1Lemmas = new HashSet<>();
            Set<String> level2Lemmas = new HashSet<>();
            Set<String> level3Lemmas = new HashSet<>();

            try {
                InputStream stream = getStream(freqLemmaFile, "/models/content_words_freq_manualcheck.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length < 2) {
                        continue;
                    }

                    String lemma = parts[0];

                    boolean done = false;
                    if (i < LIMIT_EASY) {
                        level1Lemmas.add(lemma);
                        done = true;
                    }
                    if (i < LIMIT_MEDIUM) {
                        level2Lemmas.add(lemma);
                        done = true;
                    }
                    if (i < LIMIT_HARD) {
                        level3Lemmas.add(lemma);
                        done = true;
                    }

                    if (!done) {
                        break;
                    }

                    i++;
                }
                reader.close();
            } catch (Exception e) {
                LOGGER.warn("Unable to load easyWords file: {}", e.getMessage());
            }

            ourInstance = new SpanishReadabilityModel(level1Lemmas, level2Lemmas, level3Lemmas);
        } else {
            LOGGER.info("Readability model already loaded");
        }
        return ourInstance;
    }

    private SpanishReadabilityModel(Set<String> level1Lemmas, Set<String> level2Lemmas,
            Set<String> level3Lemmas) {
        this.level1Lemmas = level1Lemmas;
        this.level2Lemmas = level2Lemmas;
        this.level3Lemmas = level3Lemmas;
    }

    public Set<String> getLevel1Lemmas() {
        return level1Lemmas;
    }

    public Set<String> getLevel2Lemmas() {
        return level2Lemmas;
    }

    public Set<String> getLevel3Lemmas() {
        return level3Lemmas;
    }
}
