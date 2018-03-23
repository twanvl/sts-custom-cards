package customcards.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MindBurnAction extends AbstractGameAction {
	public static final Logger logger = LogManager.getLogger(MindBurnAction.class.getName());
    public MindBurnAction() {
        logger.info("MindBurnAction::Construct");
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = AbstractDungeon.player;
    }

    @Override
    public void update() {
        logger.info("MindBurnAction::update " + AbstractDungeon.player.drawPile.size());
        if (this.duration == Settings.ACTION_DUR_FAST) {
            logger.info("MindBurnAction::update2 " + AbstractDungeon.player.drawPile.size());
            AbstractDungeon.actionManager.addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.drawPile.size()));
            while (!AbstractDungeon.player.drawPile.isEmpty()) {
                logger.info("MindBurnAction::update3 " + AbstractDungeon.player.drawPile.size());
                AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
                AbstractDungeon.player.drawPile.group.remove(card);
                AbstractDungeon.getCurrRoom().souls.remove(card);
                card.freeToPlayOnce = true;
                card.exhaustOnUseOnce = true;
                AbstractDungeon.player.limbo.group.add(card);
                card.current_y = -200.0f * Settings.scale;
                card.target_x = (float)Settings.WIDTH / 2.0f;
                card.target_y = (float)Settings.HEIGHT / 2.0f;
                card.targetAngle = 0.0f;
                card.lighten(false);
                card.drawScale = 0.12f;
                card.targetDrawScale = 0.75f;
                AbstractMonster target = AbstractDungeon.getRandomMonster();
                if (!card.canUse(AbstractDungeon.player, target)) {
                    AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
                } else {
                    card.applyPowers();
                    AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
                    AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                }
            }
            this.isDone = true;
        }
    }
}

