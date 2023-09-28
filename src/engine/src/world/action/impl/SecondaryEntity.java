package world.action.impl;

import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SecondaryEntity implements Serializable {
    private final EntityDefinition secondaryEntityDefinition;
    private final String count;
    private final ConditionAction condition;

    public SecondaryEntity(EntityDefinition secondaryEntityDefinition, String count, ConditionAction condition) {
        this.secondaryEntityDefinition = secondaryEntityDefinition;
        this.count = count;
        this.condition = condition;
    }

    public EntityDefinition getSecondaryEntityDefinition() {
        return secondaryEntityDefinition;
    }



    public List<EntityInstance> computeSecondaryEntitiesForAction(Integer currTicks) throws Exception {
        Random random = new Random();
        List<EntityInstance> secondaryEntityInstances = secondaryEntityDefinition.getEntityInstances();
        List<EntityInstance> computedList = new ArrayList<>();
        List<EntityInstance> resultList = new ArrayList<>();
        int secondaryEntityListSize = secondaryEntityDefinition.getEntityInstances().size();
        if ( count.equals("ALL")) {
            for ( EntityInstance entityInstance : secondaryEntityInstances) {
                if ( entityInstance.isAlive())
                    resultList.add(entityInstance);
            }
            return resultList;
        }
        else {
            int count = Integer.parseInt(this.count);
            if ( count > secondaryEntityListSize)
                count = secondaryEntityListSize;
            if ( condition != null) {
                for ( EntityInstance entityInstance : secondaryEntityInstances){
                    if ( entityInstance.isAlive()) {
                        if (condition.evaluate(currTicks, entityInstance))
                            computedList.add(entityInstance);
                    }
                }
            }
            else {
                computedList = secondaryEntityInstances.stream()
                        .filter((EntityInstance::isAlive))
                        .collect(Collectors.toList());
            }
            if ( computedList.isEmpty())
                return computedList;
            int size = computedList.size();
            for ( int i = count; i > 0; i--) {
                int randomIndex = random.nextInt(size);
                resultList.add(computedList.get(randomIndex));
            }
        }
        return resultList;
    }


    public String getCount() {
        return count;
    }

    public ConditionAction getCondition() {
        return condition;
    }
}
