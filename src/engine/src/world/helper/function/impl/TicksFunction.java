package world.helper.function.impl;

import world.context.Context;
import world.entity.api.EntityInstance;
import world.exceptions.SimulationFunctionsException;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;

public class TicksFunction extends HelperFunctionImpl {

    private final String propertyName;
    private final String entityName;
    private final Context functionContext;

    public TicksFunction(String entityName, String propertyName, Context context) {
        super("ticks", 1, "String");
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.functionContext = context;
    }

    @Override
    public Object invoke(EntityInstance entityInstance, Integer currTick) throws Exception {

        if ( entityName.equals(entityInstance.getEntityDefinition().getName())) {
            PropertyDefinition propertyDefinition = entityInstance.getEntityDefinition().getPropertyByName(propertyName);
            PropertyInstance propertyInstance = entityInstance.getPropertyByName(propertyName);
            return currTick - propertyInstance.getLastUpdateTick();
        }
        else
            throw new SimulationFunctionsException(entityName, "evaluate");
    }
}
