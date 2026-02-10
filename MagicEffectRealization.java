package a11807184;

import java.util.Set;

public interface MagicEffectRealization {

    default void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
    }

    default void takeDamagePercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
    }

    default void weakenMagic(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
    }

    default void weakenMagicPercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
    }

    default void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
    }

    default void healPercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
    }

    default void enforceMagic(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
    }

    default void enforceMagicPercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
    }

    default boolean isProtected(Spell s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        return false;
    }

    default void setProtection(Set<AttackingSpell> attacks) {
        if (attacks == null) {
            throw new IllegalArgumentException();
        }
    }

    default void removeProtection(Set<AttackingSpell> attacks) {
        if (attacks == null) {
            throw new IllegalArgumentException();
        }
    }
}