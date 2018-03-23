package customcards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.localization.CardStrings;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.*;

import customcards.cards.*;

@SpireInitializer
public class CustomCards implements PostInitializeSubscriber, EditCardsSubscriber, EditStringsSubscriber {
    private static final String MODNAME = "Custom Cards";
    private static final String AUTHOR = "twanvl";
    private static final String DESCRIPTION = "v1.0.0 NL Adds some custom cards designed by the fine people of reddit.";

	public static final Logger logger = LogManager.getLogger(CustomCards.class.getName());

    public CustomCards() {
        BaseMod.subscribeToPostInitialize(this);
        BaseMod.subscribeToEditCards(this);
        BaseMod.subscribeToEditStrings(this);
    }
    
    public static void initialize() {
        new CustomCards();
    }
    
    public void receivePostInitialize() {
        // Mod badge
        Texture badgeTexture = new Texture("img/CustomCardsBadge.png");
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addLabel("This mod does not have any settings.", 400.0f, 700.0f, (me) -> {});
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }
    @Override
    public void receiveEditCards() {
        logger.info("begin editing cards");
        BaseMod.addCard(new MindBurn());
        logger.info("done editing cards");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("begin editing strings");
        BaseMod.loadCustomStrings(CardStrings.class, loadJson("localization/eng/custom-cards.json"));
        logger.info("done editing strings");
    }
    private static String loadJson(String jsonPath) {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }
}
