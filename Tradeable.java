package a11807184;

public interface Tradeable {

    int getPrice();

    int getWeight();

    private boolean transfer(Trader from, Trader to) {
        return from.removeFromInventory(this) && to.addToInventory(this);
    }

    default boolean give(Trader giver, Trader taker) {
        if (giver == null || taker == null || giver == taker) {
            throw new IllegalArgumentException();
        }

        if (!giver.possesses(this)) {
            return false;
        }

        if (!taker.hasCapacity(this.getWeight())) {
            return false;
        }

        return transfer(giver, taker);
    }

    default boolean purchase(Trader seller, Trader buyer) {
        if (seller == null || buyer == null || seller == buyer) {
            throw new IllegalArgumentException();
        }

        if (!seller.possesses(this)) {
            return false;
        }

        if (!buyer.canAfford(this.getPrice())) {
            return false;
        }

        if (!buyer.hasCapacity(this.getWeight())) {
            return false;
        }

        if (!buyer.pay(this.getPrice())) {
            return false;
        }

        seller.earn(this.getPrice());

        return transfer(seller, buyer);
    }

    void useOn(MagicEffectRealization target);
}