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

public class MindBurnAction extends AbstractGameAction {
    public MindBurnAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            int i = 0;
            while (!AbstractDungeon.player.drawPile.isEmpty()) {
                AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
                AbstractDungeon.player.drawPile.group.remove(card);
                AbstractDungeon.getCurrRoom().souls.remove(card);
                card.freeToPlayOnce = true;
                card.exhaustOnUseOnce = true;
                AbstractDungeon.player.limbo.group.add(card);
                card.current_y = -200.0f * Settings.scale;
                card.target_x = (float)Settings.WIDTH / 2.0f + (float)(i++ * 200);
                card.target_y = (float)Settings.HEIGHT / 2.0f;
                card.targetAngle = 0.0f;
                card.lighten(false);
                card.drawScale = 0.12f;
                card.targetDrawScale = 0.75f;
                AbstractMonster target = AbstractDungeon.getRandomMonster();
                AbstractDungeon.actionManager.addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 1));
                AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
                if (!card.canUse(AbstractDungeon.player, target)) {
                    AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
                    continue;
                }
                card.applyPowers();
                AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
                AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            }
            this.isDone = true;
        }
    }
}

