package a11807184;

/**
 * A Scroll object contains a spell that can be read by someone. The scroll provides the
 * necessary mana and magic level even if the user's capabilities would not be sufficient
 * to cast the spell 
 */
public class Scroll extends MagicItem {

    private Spell spell;

    public Scroll(
            String name,
            int usages,
            int price,
            int weight,
            Spell spell
    ) {
        super(name, usages, price, weight);

        if (spell == null) {
            throw new IllegalArgumentException();
        }

        this.spell = spell;
    }

    @Override
    public String additionalOutputString() {
        return "; casts " + spell;
    }

    @Override
    public void useOn(MagicEffectRealization target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }

        if (tryUsage()) {
            spell.cast(this, target);
        }
    }
}