package a11807184;

import java.util.Set;

/**
 * ProtectingSpell objects grant protection against a number of specific attacking spells 
 */
import java.util.HashSet;
import java.util.Set;

public class ProtectingSpell extends Spell {

    private Set<AttackingSpell> attacks;

    public ProtectingSpell(
            String name,
            int manaCost,
            MagicLevel levelNeeded,
            Set<AttackingSpell> attacks
    ) {
        super(name, manaCost, levelNeeded);

        if (attacks == null || attacks.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.attacks = new HashSet<>(attacks);
    }

    @Override
    public void doEffect(MagicEffectRealization target) {
        target.setProtection(attacks);
    }

    @Override
    public String additionalOutputString() {
        return "; protects against " + attacks;
    }
}