package a11807184;

/**
 * Magic spells that do some sort of damage to a target
 * 
 */

import java.util.HashSet;
import java.util.Set;

public class AttackingSpell extends Spell {

    private boolean type;
    private boolean percentage;
    private int amount;

    public AttackingSpell(
            String name,
            int manaCost,
            MagicLevel levelNeeded,
            boolean type,
            boolean percentage,
            int amount
    ) {
        super(name, manaCost, levelNeeded);

        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        if (percentage && amount > 100) {
            throw new IllegalArgumentException();
        }

        this.type = type;
        this.percentage = percentage;
        this.amount = amount;
    }

    @Override
    public void doEffect(MagicEffectRealization target) {
        if (target.isProtected(this)) {
            Set<AttackingSpell> s = new HashSet<>();
            s.add(this);
            target.removeProtection(s);
            return;
        }

        if (type) { // HP
            if (percentage) {
                target.takeDamagePercent(amount);
            } else {
                target.takeDamage(amount);
            }
        } else { // MP
            if (percentage) {
                target.weakenMagicPercent(amount);
            } else {
                target.weakenMagic(amount);
            }
        }
    }

    @Override
    public String additionalOutputString() {
        return "; -"
                + amount
                + (percentage ? " % " : " ")
                + (type ? "HP" : "MP");
    }
    
    @Override
    public boolean isAttack() {
    	return true;
    }
}