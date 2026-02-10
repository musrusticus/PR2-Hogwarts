package a11807184;

/**
 * A Spell object generates a magic effect on a target. To cast a spell the caster
 * has to provide sufficient mana and has to have the required magic level. 
 */
public abstract class Spell {

    private String name;
    private int manaCost;
    private MagicLevel levelNeeded;

    public Spell(String name, int manaCost, MagicLevel levelNeeded) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (manaCost < 0) {
            throw new IllegalArgumentException();
        }
        if (levelNeeded == null) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.manaCost = manaCost;
        this.levelNeeded = levelNeeded;
    }

    public void cast(MagicSource source, MagicEffectRealization target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException();
        }

        if (!source.provideMana(levelNeeded, manaCost)) {
            return;
        }

        doEffect(target);
    }

    public abstract void doEffect(MagicEffectRealization target);
    
    public boolean isAttack() {
    	return false;
    }

    public String additionalOutputString() {
        return "";
    }

    @Override
    public String toString() {
        return "[" + name
                + "(" + levelNeeded + "): "
                + manaCost + " mana"
                + additionalOutputString()
                + "]";
    }
}