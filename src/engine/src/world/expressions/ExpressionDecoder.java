
package world.expressions;

import world.action.impl.SecondaryEntity;
import world.context.Context;
import world.context.ContextImpl;
import world.entity.api.EntityDefinition;
import world.environment.api.ActiveEnvironment;
import world.exceptions.*;
import world.expressions.api.Expression;
import world.expressions.impl.HelperFunctionExpression;
import world.expressions.impl.PropertyNameExpression;
import world.expressions.impl.ValueExpression;
import world.helper.function.impl.*;
import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;

import java.io.Serializable;


public class ExpressionDecoder implements Serializable {
    public static Expression decode(String expressionName, ActiveEnvironment activeEnvironment, EntityDefinition entityDefinition, AbstractPropertyDefinition.PropertyType type, String actionName, SecondaryEntity secondaryEntity, Context entitiesContext) throws Exception {
        Expression expression;
        expression = ExpressionDecoder.isHelperFunction(expressionName,activeEnvironment, entityDefinition,secondaryEntity, actionName, entitiesContext);
        if (expression != null)
            return expression;

        expression = ExpressionDecoder.isPropertyName(expressionName, entityDefinition, secondaryEntity, entitiesContext);
        if (expression != null)
            return expression;

        expression = ExpressionDecoder.freeValue(expressionName, actionName, type);
        return expression;
    }

    public static Expression isHelperFunction(String expressionName, ActiveEnvironment activeEnvironment, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity, String actionName, Context entitiesContext) throws Exception
    {
        Expression expression = null;
        try {
            if ( expressionName.contains("percent")) {
                String arg = extractCharsBetweenParentheses(expressionName);
                String[] parts = arg.split(",");
                String value = parts[0];
                String percent = parts[1];
                Expression valExp = ExpressionDecoder.decode(value, activeEnvironment, primaryEntity, AbstractPropertyDefinition.PropertyType.FLOAT, actionName, secondaryEntity, entitiesContext);
                Expression percentExp = ExpressionDecoder.decode(percent, activeEnvironment, primaryEntity, AbstractPropertyDefinition.PropertyType.FLOAT, actionName, secondaryEntity, entitiesContext);
                if (valExp.getType().equals("boolean"))
                    throw new MismatchTypesException("Percent function", "number", "boolean");
                else if (valExp.getType().equals("string"))
                    throw new MismatchTypesException("Percent function", "number", "string");
                else if (percentExp.getType().equals("boolean"))
                    throw new MismatchTypesException("Percent function", "number", "boolean");
                else if (percentExp.getType().equals("string"))
                    throw new MismatchTypesException("Percent function", "number", "string");

                PercentFunction percentFunction = new PercentFunction(valExp, percentExp);
                expression = new HelperFunctionExpression(expressionName, "float", percentFunction);
            }
            else if (expressionName.contains("environment")) {
                String arg = expressionName.split("\\(")[1].split("\\)")[0];
                PropertyInstance propertyInstance = activeEnvironment.getProperty(arg).orElseThrow(() -> new EnvironmentVariableNotExistException(arg));/// some exception
                expression = new HelperFunctionExpression(expressionName, propertyInstance.getPropertyDefinition().getType().name().toLowerCase(), new EnvironmentFunction(propertyInstance));// maybe the environment function should get the property instance
            }
            else if (expressionName.contains("random")) {
                String arg = expressionName.split("\\(")[1].split("\\)")[0];
                try {
                    Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    throw new MismatchTypesException("Random helper function", "Integer", arg);
                }
                expression = new HelperFunctionExpression(expressionName, "decimal", new RandomFunction(arg));
            }
            else if (expressionName.contains("evaluate")) {
                String arg = expressionName.split("\\(")[1].split("\\)")[0];
                if ( !arg.contains(".")) {
                    throw new HelperFunctionFormatException("evaluate","entity.property name",arg);
                }
                String[] parts = arg.split("\\.");
                if ( parts.length > 2) {
                    throw new HelperFunctionFormatException("evaluate","entity.property name",arg);
                }
                String entityName = parts[0];
                String propertyName = parts[1];
                AbstractPropertyDefinition.PropertyType type = getTypeOfHelperFuncArg(entitiesContext, entityName, propertyName, actionName,"evaluate");
                EvaluateFunction evaluateFunction = new EvaluateFunction(entityName, propertyName, entitiesContext);
                expression = new HelperFunctionExpression(expressionName, type.toString().toLowerCase(), evaluateFunction);
            }
            else if (expressionName.contains("ticks")) {
                String arg = expressionName.split("\\(")[1].split("\\)")[0];
                if (!arg.contains(".")) {
                    throw new HelperFunctionFormatException("ticks", "entity.property name", arg);
                }
                String[] parts = arg.split("\\.");
                if (parts.length > 2) {
                    throw new HelperFunctionFormatException("ticks", "entity.property name", arg);
                }
                String entityName = parts[0];
                String propertyName = parts[1];
                AbstractPropertyDefinition.PropertyType type = getTypeOfHelperFuncArg(entitiesContext, entityName, propertyName, actionName,"ticks");
                TicksFunction ticksFunction = new TicksFunction(entityName, propertyName, entitiesContext);
                expression = new HelperFunctionExpression(expressionName, "decimal", ticksFunction);
            }
        } catch (NumberFormatException  e) {
            throw new RuntimeException(e);
        }
        return expression;
    }

    public static String extractCharsBetweenParentheses(String input) {
        int startIndex = input.indexOf('(');
        int endIndex = input.lastIndexOf(')');

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            // Extract the characters between '(' and ')'
            return input.substring(startIndex + 1, endIndex);
        } else {
            // Handle the case where '(' and ')' are not found or are in the wrong order
            return "";
        }
    }


    public static AbstractPropertyDefinition.PropertyType  getTypeOfHelperFuncArg(Context context, String entityName, String propertyName, String actionName, String funcName) throws Exception {
        if ( context.getPrimaryEntity().getName().equals(entityName)) {
            PropertyDefinition propertyDefinition = context.getPrimaryEntity().getPropertyByName(propertyName);
            return propertyDefinition.getType();
        }
        else if ( context.getSecondaryEntity() != null) {
            if ( context.getSecondaryEntity().getName().equals(entityName)) {
                PropertyDefinition propertyDefinition = context.getSecondaryEntity().getPropertyByName(propertyName);
                return propertyDefinition.getType();
            }
            else throw new HelperFunctionException(entityName, funcName, actionName);
        }
        else throw new HelperFunctionException(entityName, funcName, actionName);
    }

    public static Expression isPropertyName(String expressionName, EntityDefinition entityDefinition, SecondaryEntity secondaryEntity, Context entitiesContext)  {

        for (PropertyDefinition property : entitiesContext.getPrimaryEntity().getPropertiesList())
        {
            if (property.getName().equals(expressionName))
                return new PropertyNameExpression(expressionName, property.getType().name().toLowerCase(), property);
        }
        if (entitiesContext.getSecondaryEntity() != null) {
            for (PropertyDefinition property : entitiesContext.getSecondaryEntity().getPropertiesList())
            {
                if (property.getName().equals(expressionName))
                    return new PropertyNameExpression(expressionName, property.getType().name().toLowerCase(), property);
            }
        }
        return null;
    }

    public static Expression freeValue(String expressionName, String actionName, AbstractPropertyDefinition.PropertyType type) throws MismatchTypesException {
        if (type.equals(AbstractPropertyDefinition.PropertyType.DECIMAL)) {
            try {
                Integer.parseInt(expressionName);
                return new ValueExpression(expressionName, "decimal");
            } catch (NumberFormatException e) {
                throw new MismatchTypesException(actionName, "Integer", expressionName);
            }
        } else if (type.equals(AbstractPropertyDefinition.PropertyType.FLOAT)) {
            try {
                Float.parseFloat(expressionName);
                return new ValueExpression(expressionName, "float");
            } catch (NumberFormatException e) {
                throw new MismatchTypesException(actionName, "Float", expressionName);
            }
        } else if (type.equals(AbstractPropertyDefinition.PropertyType.BOOLEAN)) {
            if (expressionName.equals("true") || expressionName.equals("false"))
                return new ValueExpression(expressionName, "boolean");
            else
                throw new MismatchTypesException(actionName, "Boolean", expressionName);
        } else
            return new ValueExpression(expressionName, "string");
    }
}



