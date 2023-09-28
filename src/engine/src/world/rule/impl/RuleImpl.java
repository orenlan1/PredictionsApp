package world.rule.impl;

import world.World;
import world.action.api.Action;
import world.action.api.ActionType;
import world.entity.api.EntityInstance;
import world.rule.activation.Activation;
import world.rule.activation.ActivationImpl;
import world.rule.api.Rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RuleImpl implements Rule, Serializable {
    private final String name;
    private final List<Action> actions;
    private final Activation activation;

    public RuleImpl(String name, Activation activation) {
        this.name = name;
        this.activation = activation;
        actions = new ArrayList<>();
    }

    @Override
    public String getName() {return name; }

    @Override
    public Activation getActivation() {                 //ADD IMPLEMENTATION!!!!!
        return activation;
    }

    @Override
    public List<Action> getaActionsToPerform() { return actions; }

    @Override
    public void addAction(Action action) {
        actions.add(action);
    }

    @Override
    public void performActions(EntityInstance entityInstance, int currTick) throws Exception {
        for (Action action : actions) {
            action.activate(currTick, entityInstance);
        }
    }
}
