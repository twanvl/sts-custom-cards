package customcards;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import customcards.cards.DemonArmor;
import customcards.cards.MindBurn;
import customcards.relics.TimeEatersClock;

@SpireInitializer
public class CustomCards implements
        PostInitializeSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber {
    private static final String MODNAME = "Custom Cards and Relics";
    private static final String AUTHOR = "twanvl";
    private static final String DESCRIPTION = "Adds some custom cards and relics designed by the fine people of Reddit.";

	public static final Logger logger = LogManager.getLogger(CustomCards.class.getName());

    public CustomCards() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new CustomCards();
    }

    public void receivePostInitialize() {
        // Mod badge
        Texture badgeTexture = new Texture("img/CustomCardsBadge.png");
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, null);
    }
    @Override
    public void receiveEditCards() {
        logger.info("begin editing cards");
        BaseMod.addCard(new MindBurn());
        BaseMod.addCard(new DemonArmor());
        logger.info("done editing cards");
    }

    @Override
    public void receiveEditRelics() {
        logger.info("begin editing relics");
        BaseMod.addRelic(new TimeEatersClock(), RelicType.SHARED);
        logger.info("done editing relics");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("begin editing strings");
        BaseMod.loadCustomStrings(CardStrings.class, loadJson("localization/eng/custom-cards.json"));
        BaseMod.loadCustomStrings(PowerStrings.class, loadJson("localization/eng/custom-powers.json"));
        BaseMod.loadCustomStrings(RelicStrings.class, loadJson("localization/eng/custom-relics.json"));
        logger.info("done editing strings");
    }
    private static String loadJson(String jsonPath) {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    public static String cardImage(String id) {
        return "img/cards/" + id + ".png";
    }
    public static Texture relicImage(String id) {
        return ImageMaster.loadImage("img/relics/" + id + ".png");
    }
    public static Texture relicOutline(String id) {
        return ImageMaster.loadImage("img/relics/outline/" + id + ".png");
    }
}
