// You only have 12 seconds to complete your turn.
package customcards.relics;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import basemod.abstracts.CustomRelic;
import customcards.CustomCards;

public class TimeEatersClock extends CustomRelic {
    public static final String ID = "TimeEatersClock";
    private static final float SECONDS_PER_TURN = 12.0f;
    private boolean isTicking = false;
    private float turnTimer;

    public TimeEatersClock() {
        super(ID, CustomCards.relicImage(ID), CustomCards.relicOutline(ID), AbstractRelic.RelicTier.BOSS,
                AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void update() {
        // update timer
        if (this.isObtained && this.isTicking) {
            this.turnTimer -= Gdx.graphics.getDeltaTime();
            checkTick();
        }
        super.update();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (int)SECONDS_PER_TURN + DESCRIPTIONS[1];
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
        onPlayerEndTurn();
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
        onPlayerEndTurn();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TimeEatersClock();
    }

    @Override
    public void atTurnStartPostDraw() {
        this.isTicking = true;
        this.beginLongPulse();
        this.turnTimer = SECONDS_PER_TURN;
        checkTick();
    }

    @Override
    public void onPlayerEndTurn() {
        this.isTicking = false;
        this.stopPulse();
        this.setCounter(-1);
    }

    @Override
    public void onVictory() {
        onPlayerEndTurn();
    }

    private void checkTick() {
        if (!this.isTicking) return;
        if (AbstractDungeon.player.isDead || AbstractDungeon.getMonsters() == null || AbstractDungeon.getMonsters().areMonstersDead()) {
            onPlayerEndTurn();
            return;
        }
        if (this.turnTimer < 0) {
            onPlayerEndTurn();
            // from TimeWarpPower
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
            AbstractDungeon.actionManager.cardQueue.clear();
            for (AbstractCard c : AbstractDungeon.player.limbo.group) {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
            }
            AbstractDungeon.player.limbo.group.clear();
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.endTurnButton.disable(true); // this ends the turn
        } else {
            this.setCounter((int) Math.ceil(this.turnTimer));
        }
    }
}
