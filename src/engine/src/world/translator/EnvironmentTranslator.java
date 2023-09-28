package world.translator;


import jaxb.generated.PRDEnvProperty;
import jaxb.generated.PRDEnvironment;
import world.environment.api.EnvironmentVariablesManager;
import world.environment.impl.EnvironmentVariablesManagerImpl;
import world.exceptions.EnvironmentVariableNameExistException;
import world.exceptions.InvalidVariableTypeException;
import world.property.api.PropertyDefinition;
import world.property.impl.BooleanPropertyDefinition;
import world.property.impl.FloatPropertyDefinition;
import world.property.impl.IntegerPropertyDefinition;
import world.property.impl.StringPropertyDefinition;
import world.value.generator.api.ValueGeneratorFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnvironmentTranslator {
    public static EnvironmentVariablesManager translateEnvironment(PRDEnvironment prdEnvironment) throws EnvironmentVariableNameExistException, InvalidVariableTypeException {
        EnvironmentVariablesManager environmentVariablesManager = new EnvironmentVariablesManagerImpl();
        List<PropertyDefinition> envProperties = EnvironmentTranslator.translateEnvironmentProperties(prdEnvironment);

        for (PropertyDefinition property : envProperties) {
            environmentVariablesManager.addEnvironmentVariable(property);
        }
        return environmentVariablesManager;
    }

    public static PropertyDefinition TranslateEnvironmentPropertyDefinition(PRDEnvProperty prdEnvProperty) throws InvalidVariableTypeException {
        String name = prdEnvProperty.getPRDName();
        String propertyType = prdEnvProperty.getType();
        double from = -Double.MAX_VALUE;
        double to = Double.MAX_VALUE;

   

        if (prdEnvProperty.getPRDRange() != null) {

            from = prdEnvProperty.getPRDRange().getFrom();
            to = prdEnvProperty.getPRDRange().getTo();
        }
        PropertyDefinition propertyDefinition = null;

        switch (propertyType) {
            case "decimal":
                propertyDefinition = new IntegerPropertyDefinition(name, ValueGeneratorFactory.createRandomInteger((int) from, (int) to), (int) from, (int) to,Boolean.FALSE);
                break;
            case "float":
                propertyDefinition = new FloatPropertyDefinition(name, ValueGeneratorFactory.createRandomFloat((float) from, (float) to), (float) from, (float) to,Boolean.FALSE);
                break;
            case "boolean":
                propertyDefinition = new BooleanPropertyDefinition(name, ValueGeneratorFactory.createRandomBoolean(),Boolean.FALSE);
                break;
            case "string":
                propertyDefinition = new StringPropertyDefinition(name, ValueGeneratorFactory.createRandomString(),Boolean.FALSE);
                break;
            default:
                throw new InvalidVariableTypeException("translating environment variables", "decimal, float, boolean or string", propertyType);
        }
        return propertyDefinition;
    }

    public static List<PropertyDefinition> translateEnvironmentProperties(PRDEnvironment prdEnvironment) throws InvalidVariableTypeException {
        List<PropertyDefinition> propertyDefinitions = new LinkedList<>();
        for (PRDEnvProperty prdEnvProperty : prdEnvironment.getPRDEnvProperty()) {
            propertyDefinitions.add(EnvironmentTranslator.TranslateEnvironmentPropertyDefinition(prdEnvProperty));
        }
        return propertyDefinitions;
    }
}
