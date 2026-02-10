package a11807184;

public class StateSpell extends Spell {

    private WizardState state;

    public StateSpell(String name,
                      int manaCost,
                      MagicLevel levelNeeded,
                      WizardState state) {
        super(name, manaCost, levelNeeded);
        this.state = state;
    }

    @Override
    public void doEffect(MagicEffectRealization target) {
        if (target instanceof Wizard w) {
            w.addState(state);
        }
    }
}

// usage of existing class spell, just some overrides needed