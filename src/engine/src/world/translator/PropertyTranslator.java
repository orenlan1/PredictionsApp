package world.translator;


import jaxb.generated.PRDProperties;
import jaxb.generated.PRDProperty;
import world.exceptions.InvalidVariableTypeException;
import world.property.api.PropertyDefinition;
import world.property.impl.BooleanPropertyDefinition;
import world.property.impl.FloatPropertyDefinition;
import world.property.impl.IntegerPropertyDefinition;
import world.property.impl.StringPropertyDefinition;
import world.value.generator.api.ValueGeneratorFactory;

import java.util.LinkedList;
import java.util.List;

public class PropertyTranslator {
    public static PropertyDefinition TranslatePropertyDefinition(PRDProperty prdProperty) throws InvalidVariableTypeException {
        String name = prdProperty.getPRDName();
        String propertyType = prdProperty.getType();
        boolean randomInit = prdProperty.getPRDValue().isRandomInitialize();
        double from = -Double.MAX_VALUE;
        double to = Double.MAX_VALUE;

        if (prdProperty.getPRDRange() != null) {

            from = prdProperty.getPRDRange().getFrom();
            to = prdProperty.getPRDRange().getTo();
        }
        String initValue = prdProperty.getPRDValue().getInit();
        PropertyDefinition propertyDefinition = null;

        switch (propertyType) {
            case "decimal":
                if (randomInit) {
                    propertyDefinition = new IntegerPropertyDefinition(name, ValueGeneratorFactory.createRandomInteger((int) from, (int) to), (int) from, (int) to,randomInit);
                } else {
                    propertyDefinition = new IntegerPropertyDefinition(name, ValueGeneratorFactory.createFixed(Integer.parseInt(initValue)), (int) from, (int) to,randomInit);
                }
                break;
            case "float":
                if (randomInit) {
                    propertyDefinition = new FloatPropertyDefinition(name, ValueGeneratorFactory.createRandomFloat((float) from, (float) to), (float) from, (float) to,randomInit);
                } else {
                    propertyDefinition = new FloatPropertyDefinition(name, ValueGeneratorFactory.createFixed(Float.parseFloat(initValue)), (float) from, (float) to,randomInit);
                }
                break;
            case "boolean":
                if (randomInit) {
                    propertyDefinition = new BooleanPropertyDefinition(name, ValueGeneratorFactory.createRandomBoolean(),randomInit);
                } else {
                    propertyDefinition = new BooleanPropertyDefinition(name, ValueGeneratorFactory.createFixed(Boolean.parseBoolean(initValue)),randomInit);
                }
                break;
            case "string":
                if (randomInit) {
                    propertyDefinition = new StringPropertyDefinition(name, ValueGeneratorFactory.createRandomString(),randomInit);
                } else {
                    propertyDefinition = new StringPropertyDefinition(name, ValueGeneratorFactory.createFixed(initValue),randomInit);
                }
                break;
            default:
                throw new InvalidVariableTypeException("translating properties", "decimal, float, boolean or string", propertyType);
        }
        return propertyDefinition;
    }


    public static List<PropertyDefinition> translateProperties(PRDProperties prdPropertiesList) throws InvalidVariableTypeException {
        List<PropertyDefinition> propertyDefinitions = new LinkedList<>();
        for (PRDProperty prdProperty : prdPropertiesList.getPRDProperty()) {
            propertyDefinitions.add(PropertyTranslator.TranslatePropertyDefinition(prdProperty));
        }
        return propertyDefinitions;
    }
}
