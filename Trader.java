package a11807184;

/**
 * Trader objects have an inventory in which they can store magic items.
 * They can trade and use these objects. Additionally they are able to
 * steal and loot objects from other traders. 
 */
public interface Trader {

    boolean possesses(Tradeable item);

    boolean canAfford(int amount);

    boolean hasCapacity(int weight);

    boolean pay(int amount);

    boolean earn(int amount);

    boolean addToInventory(Tradeable item);

    boolean removeFromInventory(Tradeable item);

    default boolean canSteal() {
        return false;
    }

    boolean steal(Trader thief);

    default boolean isLootable() {
        return false;
    }

    default boolean canLoot() {
        return false;
    }

    boolean loot(Trader looter);
}