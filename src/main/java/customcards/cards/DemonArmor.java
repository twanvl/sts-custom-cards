package customcards.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import customcards.powers.DemonArmorPower;

public class DemonArmor extends CustomCard {
    public static final String ID = "Demon Armor";
    public static final String IMAGE = "img/cards/DemonArmor.png";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;
    private static final int AMOUNT = 1;

    public DemonArmor() {
        super(ID, NAME, IMAGE, COST, DESCRIPTION, CardType.POWER, CardColor.RED, CardRarity.RARE, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DemonArmorPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DemonArmor();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
        }
    }
}
