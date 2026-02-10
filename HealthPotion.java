package a11807184;

/**
 * HealthPotions increase the consumer's mana
 */
public class HealthPotion extends Potion {

    private int health;

    public HealthPotion(String name, int usages, int price, int weight, int health) {
        super(name, usages, price, weight);

        if (health < 0) {
            throw new IllegalArgumentException();
        }

        this.health = health;
    }

    @Override
    public String additionalOutputString() {
        return "; +" + health + " HP";
    }

    @Override
    public void useOn(MagicEffectRealization target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }

        if (tryUsage()) {
            target.heal(health);
        }
    }
}