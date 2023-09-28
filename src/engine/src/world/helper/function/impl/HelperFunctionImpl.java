package world.helper.function.impl;

import world.helper.function.api.HelperFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class HelperFunctionImpl implements HelperFunction, Serializable {
    private final String name;
    private final int numOfArguments;
    private final List<String> argsTypes;

    public HelperFunctionImpl(String name, int numOfArguments, String... args) {
        this.name = name;
        this.numOfArguments = numOfArguments;
        this.argsTypes = new ArrayList<>();
        Collections.addAll(argsTypes, args);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumOfArgs() {
        return numOfArguments;
    }

    @Override
    public List<String> getTypeOfArgs() {
        return argsTypes;
    }
}
