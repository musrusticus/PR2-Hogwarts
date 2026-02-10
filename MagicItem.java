package a11807184;

public abstract class MagicItem
        implements Tradeable, MagicEffectRealization, MagicSource {

    private String name;
    private int usages;
    private int price;
    private int weight;

    public MagicItem(String name, int usages, int price, int weight) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (usages < 0 || price < 0 || weight < 0) {
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.usages = usages;
        this.price = price;
        this.weight = weight;
    }

    public int getUsages() {
        return usages;
    }

    public boolean tryUsage() {
        if (usages > 0) {
            usages--;
            return true;
        }
        return false;
    }
    
    public String getName() {
    	return name;
    }

    public String usageString() {
        return usages == 1 ? "use" : "uses";
    }

    public String additionalOutputString() {
        return "";
    }

    @Override
    public String toString() {
        String currency = price == 1 ? "Knut" : "Knuts";

        return "[" + name
                + "; " + weight + " g"
                + "; " + price + " " + currency
                + "; " + usages + " " + usageString()
                + additionalOutputString()
                + "]";
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public boolean provideMana(MagicLevel levelNeeded, int amount) {
        if (levelNeeded == null || amount < 0) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    @Override
    public void takeDamagePercent(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException();
        }
        usages = (int) (usages * (1 - percentage / 100.0));
    }
}