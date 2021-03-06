package eu.fbk.dh.tint.digimorph.annotator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alessio on 25/05/15.
 */

public class GuessModelInstance {

    private static GuessModelInstance instance;
    private GuessModel model;
    private static final Logger LOGGER = LoggerFactory.getLogger(GuessModelInstance.class);

    private GuessModelInstance() {
        LOGGER.info("Loading guess model for lemma");
        model = new GuessModel();
    }

    public static GuessModelInstance getInstance() {
        if (instance == null) {
            instance = new GuessModelInstance();
        }

        return instance;
    }

    public GuessModel getModel() {
        return model;
    }
}
