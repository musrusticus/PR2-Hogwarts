package a11807184;

// i used the builder pattern to refactor my wizard because it's easier to read and to maintain the project

import java.util.HashSet;
import java.util.Set;

public class Wizard implements MagicSource, Trader, MagicEffectRealization {

    private String name;
    private MagicLevel level;

    private int basicHP;
    private int HP;

    private int basicMP;
    private int MP;

    private int money;

    private Set<Spell> knownSpells;
    private Set<AttackingSpell> protectedFrom;

    private int carryingCapacity;
    private Set<Tradeable> inventory;
    
    private Set<WizardState> states = new HashSet<>();
    
    // make wizard private to ensure that builder is being used / has to be used
    private Wizard(
            String name,
            MagicLevel level,
            int basicHP,
            int HP,
            int basicMP,
            int MP,
            int money,
            Set<Spell> knownSpells,
            Set<AttackingSpell> protectedFrom,
            int carryingCapacity,
            Set<Tradeable> inventory
    ) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (level == null) {
            throw new IllegalArgumentException();
        }
        if (basicHP < 0 || HP < 0) {
            throw new IllegalArgumentException();
        }
        if (basicMP < level.toMana() || MP < 0) {
            throw new IllegalArgumentException();
        }
        if (money < 0 || carryingCapacity < 0) {
            throw new IllegalArgumentException();
        }
        if (knownSpells == null || protectedFrom == null || inventory == null) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.level = level;

        this.basicHP = basicHP;
        this.HP = HP;

        this.basicMP = basicMP;
        this.MP = MP;

        this.money = money;

        this.knownSpells = new HashSet<>(knownSpells);
        this.protectedFrom = new HashSet<>(protectedFrom);

        this.carryingCapacity = carryingCapacity;
        this.inventory = new HashSet<>(inventory);
    }
    
    // create builder
    public static Builder builder() {
        return new Builder();
    }
    
    // needed for states
    public void addState(WizardState state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }
        states.add(state);
    }

    public void removeState(WizardState state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }
        states.remove(state);
    }

    public boolean hasState(WizardState state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }
        return states.contains(state);
    }

    
    
    public boolean isDead() {
        return HP == 0;
    }
    
    // not needed but I decided to use it anyways to make my life easier
    private boolean isParalyzed() {
        return states.contains(WizardState.PARALYZED);
    }
    
    public int getNumberOfKnownSpells() {
    	return knownSpells.size();
    }

    private int inventoryTotalWeight() {
        int sum = 0;
        for (Tradeable t : inventory) {
            sum += t.getWeight();
        }
        return sum;
    }
    
    public void senseAttack(Wizard target) {
        for (Spell s : knownSpells) {

            if (s.isAttack()) {
                AttackingSpell attack = (AttackingSpell) s;

                if (!target.isProtected(attack)) {
                    castSpell(attack, target);
                }
            }
        }
    }

    /* SPELLS */

    public boolean learn(Spell s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        if (isDead()) {
            return false;
        }
        return knownSpells.add(s);
    }

    public boolean forget(Spell s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        if (isDead()) {
            return false;
        }
        return knownSpells.remove(s);
    }

    public boolean castSpell(Spell s, MagicEffectRealization target) {
    	
    	// no spell casting allowed if wizard is muted or paralyzed because wizard cannot talk
    	if (hasState(WizardState.MUTED)) {
    	    return false;
    	}
    	
    	if (isParalyzed()) {
    	    return false;
    	}
    	
    	if (s == null || target == null) {
            throw new IllegalArgumentException();
        }
        if (isDead()) {
            return false;
        }
        if (!knownSpells.contains(s)) {
            return false;
        }

        s.cast(this, target);
        return true;
    }

    public boolean castRandomSpell(MagicEffectRealization target) {
    	
    	// no spell casting allowed if wizard is muted or paralyzed because wizard cannot talk
    	if (hasState(WizardState.MUTED)) {
    	    return false;
    	}
    	
    	if (isParalyzed()) {
    	    return false;
    	}
    	
    	if (target == null) {
            throw new IllegalArgumentException();
        }
        if (knownSpells.isEmpty()) {
            return false;
        }

        Spell s = knownSpells.iterator().next();
        return castSpell(s, target);
    }

    /* ITEM USAGE */

    public boolean useItem(Tradeable item, MagicEffectRealization target) {
        // no item usage allowed if wizard is blinded because wizard cannot see, also not permitted if paralyzed because wizard cannot move
    	if (hasState(WizardState.BLIND)) {
    	    return false;
    	}
    	
    	if (isParalyzed()) {
    	    return false;
    	}
    	
    	if (item == null || target == null) {
            throw new IllegalArgumentException();
        }
        if (isDead()) {
            return false;
        }
        if (!inventory.contains(item)) {
            return false;
        }

        item.useOn(target);
        return true;
    }

    public boolean useRandomItem(MagicEffectRealization target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (inventory.isEmpty()) {
            return false;
        }

        Tradeable item = inventory.iterator().next();
        return useItem(item, target);
    }

    /* TRADING */

    public boolean sellItem(Tradeable item, Trader target) {
        if (item == null || target == null) {
            throw new IllegalArgumentException();
        }
        if (isDead()) {
            return false;
        }
        return item.purchase(this, target);
    }

    public boolean sellRandomItem(Trader target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        if (inventory.isEmpty()) {
            return false;
        }

        Tradeable item = inventory.iterator().next();
        return sellItem(item, target);
    }

    @Override
    public boolean possesses(Tradeable item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return inventory.contains(item);
    }

    @Override
    public boolean canAfford(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        return money >= amount;
    }

    @Override
    public boolean hasCapacity(int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException();
        }
        return inventoryTotalWeight() + weight <= carryingCapacity;
    }

    @Override
    public boolean pay(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        if (isDead() || money < amount) {
            return false;
        }
        money -= amount;
        return true;
    }

    @Override
    public boolean earn(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        if (isDead()) {
            return false;
        }
        money += amount;
        return true;
    }

    @Override
    public boolean addToInventory(Tradeable item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!hasCapacity(item.getWeight())) {
            return false;
        }
        return inventory.add(item);
    }

    @Override
    public boolean removeFromInventory(Tradeable item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return inventory.remove(item);
    }

    @Override
    public boolean canSteal() {
        return !isDead();
    }

    @Override
    public boolean steal(Trader thief) {
        if (thief == null) {
            throw new IllegalArgumentException();
        }
        if (!thief.canSteal() || inventory.isEmpty()) {
            return false;
        }

        Tradeable item = inventory.iterator().next();
        inventory.remove(item);

        if (thief.hasCapacity(item.getWeight())) {
            thief.addToInventory(item);
            return true;
        }

        return false;
    }

    @Override
    public boolean isLootable() {
        return isDead();
    }

    @Override
    public boolean canLoot() {
        return !isDead();
    }

    @Override
    public boolean loot(Trader looter) {
        if (looter == null) {
            throw new IllegalArgumentException();
        }
        if (!looter.canLoot() || !isLootable()) {
            return false;
        }

        boolean transferred = false;

        for (Tradeable item : inventory) {
            if (looter.hasCapacity(item.getWeight())) {
                looter.addToInventory(item);
                transferred = true;
            }
        }

        inventory.clear();
        return transferred;
    }

    /* MAGIC MANA */

    @Override
    public boolean provideMana(MagicLevel levelNeeded, int manaAmount) {
        if (levelNeeded == null || manaAmount < 0) {
            throw new IllegalArgumentException();
        }
        if (isDead()) {
            return false;
        }
        if (level.ordinal() < levelNeeded.ordinal()) {
            return false;
        }
        if (MP < manaAmount) {
            return false;
        }

        MP -= manaAmount;
        return true;
    }

    /* MAGIC EFFECTS HEAL DAMAGE */

    @Override
    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        HP = Math.max(0, HP - amount);
    }

    @Override
    public void takeDamagePercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
        HP = Math.max(0, HP - (int) (basicHP * (percentage / 100.0)));
    }

    @Override
    public void weakenMagic(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        MP = Math.max(0, MP - amount);
    }

    @Override
    public void weakenMagicPercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
        MP = Math.max(0, MP - (int) (basicMP * (percentage / 100.0)));
    }

    @Override
    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        HP += amount;
    }

    @Override
    public void healPercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
        HP += (int) (basicHP * (percentage / 100.0));
    }

    @Override
    public void enforceMagic(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException();
        }
        MP += amount;
    }

    @Override
    public void enforceMagicPercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
        MP += (int) (basicMP * (percentage / 100.0));
    }

    @Override
    public boolean isProtected(Spell s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        return protectedFrom.contains(s);
    }

    @Override
    public void setProtection(Set<AttackingSpell> attacks) {
        if (attacks == null) {
            throw new IllegalArgumentException();
        }
        protectedFrom.addAll(attacks);
    }

    @Override
    public void removeProtection(Set<AttackingSpell> attacks) {
        if (attacks == null) {
            throw new IllegalArgumentException();
        }
        protectedFrom.removeAll(attacks);
    }

    /* toString */

    @Override
    public String toString() {
        return "[" + name
                + "(" + level + "): "
                + HP + "/" + basicHP + " "
                + MP + "/" + basicMP + "; "
                + money + (money == 1 ? " Knut" : " Knuts")
                + "; knows " + knownSpells
                + "; carries " + inventory
                + "]";
    }
    
    /* Builder */
    
    public static class Builder {

        private String name;
        private MagicLevel level;

        private int basicHP;
        private int HP;

        private int basicMP;
        private int MP;

        private int money;
        private int carryingCapacity;

        private Set<Spell> knownSpells = new HashSet<>();
        private Set<AttackingSpell> protectedFrom = new HashSet<>();
        private Set<Tradeable> inventory = new HashSet<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder level(MagicLevel level) {
            this.level = level;
            return this;
        }

        public Builder basicHP(int basicHP) {
            this.basicHP = basicHP;
            this.HP = basicHP; // Default
            return this;
        }

        public Builder basicMP(int basicMP) {
            this.basicMP = basicMP;
            this.MP = basicMP; // Default
            return this;
        }

        public Builder money(int money) {
            this.money = money;
            return this;
        }

        public Builder carryingCapacity(int carryingCapacity) {
            this.carryingCapacity = carryingCapacity;
            return this;
        }

        public Builder knownSpells(Set<Spell> spells) {
            this.knownSpells = spells;
            return this;
        }

        public Builder protectedFrom(Set<AttackingSpell> attacks) {
            this.protectedFrom = attacks;
            return this;
        }

        public Builder inventory(Set<Tradeable> inventory) {
            this.inventory = inventory;
            return this;
        }

        public Wizard build() {
            return new Wizard(
                    name,
                    level,
                    basicHP,
                    HP,
                    basicMP,
                    MP,
                    money,
                    knownSpells,
                    protectedFrom,
                    carryingCapacity,
                    inventory
            );
        }
    }
}