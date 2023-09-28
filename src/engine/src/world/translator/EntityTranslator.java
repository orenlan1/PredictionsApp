package world.translator;


import jaxb.generated.PRDEntities;
import jaxb.generated.PRDEntity;

import world.entity.api.EntityDefinition;
import world.entity.impl.EntityDefinitionImpl;
import world.exceptions.EntityPropertyNameExistException;
import world.exceptions.InvalidVariableTypeException;
import world.property.api.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class EntityTranslator {
    public static EntityDefinition TranslateEntityDefinition(PRDEntity prdEntity) throws EntityPropertyNameExistException, InvalidVariableTypeException {
        String name = prdEntity.getName();
        List<PropertyDefinition> lst = PropertyTranslator.translateProperties(prdEntity.getPRDProperties());

        EntityDefinition entityDefinition = new EntityDefinitionImpl(name);
        for (PropertyDefinition propertyDefinition : lst) {
            entityDefinition.addPropertyDefinition(propertyDefinition);
        }
        //entityDefinition.createEntityInstancesPopulation();
        return entityDefinition;
    }

    public static List<EntityDefinition> translateEntities (PRDEntities prdEntities) throws Exception {
        List<EntityDefinition> lst = new ArrayList<>();
        for (PRDEntity prdEntity : prdEntities.getPRDEntity()) {
            lst.add(EntityTranslator.TranslateEntityDefinition(prdEntity));
        }
        return lst;
    }
}
