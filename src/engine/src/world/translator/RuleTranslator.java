package world.translator;


import jaxb.generated.PRDRule;
import jaxb.generated.PRDAction;
import jaxb.generated.PRDActivation;
import jaxb.generated.PRDThen;
import jaxb.generated.PRDElse;
import jaxb.generated.PRDCondition;
import jaxb.generated.PRDDivide;
import jaxb.generated.PRDMultiply;
import world.action.api.Action;
import world.action.impl.*;
import world.context.Context;
import world.context.ContextImpl;
import world.entity.api.EntityDefinition;
import world.environment.api.ActiveEnvironment;
import world.exceptions.*;
import world.expressions.ExpressionDecoder;
import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyDefinition;
import world.rule.activation.Activation;
import world.rule.activation.ActivationImpl;
import world.rule.api.Rule;
import world.rule.impl.RuleImpl;
import world.expressions.api.Expression;

import java.util.ArrayList;
import java.util.List;

public class RuleTranslator {

    public static Rule translateRule(PRDRule prdRule, List<EntityDefinition> entitiesList, ActiveEnvironment activeEnvironment) throws Exception {
        String ruleName = prdRule.getName();
        Double ruleProbability = 1.0;
        Integer ruleTicks = 1;
        PRDActivation prdActivation = prdRule.getPRDActivation();
        if (prdActivation != null) {
            if (prdRule.getPRDActivation().getProbability() != null)
                ruleProbability = prdRule.getPRDActivation().getProbability();
            if (prdRule.getPRDActivation().getTicks() != null)
                ruleTicks = prdRule.getPRDActivation().getTicks();
        }
        if (ruleTicks == null)
            ruleTicks = 1;
        if (ruleProbability == null)
            ruleProbability = 1.0;

        Activation ruleActivation = new ActivationImpl(ruleProbability, ruleTicks);
        Rule rule = new RuleImpl(ruleName, ruleActivation);
        for (PRDAction prdAction : prdRule.getPRDActions().getPRDAction())
        {
            rule.addAction(translateAction(prdAction, entitiesList, activeEnvironment, null, null));
        }
        return rule;
    }


    public static Action translateAction(PRDAction prdAction, List<EntityDefinition> entitiesList, ActiveEnvironment activeEnvironment, SecondaryEntity secondaryEntity, Context entitiesContext) throws Exception {
        String actionName = prdAction.getType();
        if ( secondaryEntity == null) {
            secondaryEntity = translateSecondaryEntity(prdAction, entitiesList, activeEnvironment);
        }
        EntityDefinition entityDefinition = null;
        if ( !actionName.equals("proximity") && !actionName.equals("replace")) {
            String entityName = prdAction.getEntity();
            entityDefinition = getEntityDefinition(entityName, entitiesList);
            if ( entitiesContext == null) { // Must be a new action
                entitiesContext = createEntitiesContext(entityDefinition, secondaryEntity);

            }
            else { // Must be a sub action
                if ( !isEntityRelatedToContext(entitiesContext,entityDefinition)) {
                    String primaryEntityName = entitiesContext.getPrimaryEntity().getName();
                    if (entitiesContext.getSecondaryEntity() != null) {
                        String secondaryEntityName = entitiesContext.getSecondaryEntity().getName();
                        throw new EntityNotRelatedToActionException(entityDefinition.getName(),actionName, primaryEntityName + "," + secondaryEntityName);
                    }
                    throw new EntityNotRelatedToActionException(entityDefinition.getName(),actionName, primaryEntityName);
                }
                entitiesContext = createSubActionEntitiesContext(entityDefinition,entitiesContext);
            }
        }

        String propertyName = prdAction.getProperty();
        String resultProp = prdAction.getResultProp();
        String mode = prdAction.getMode();
        String createdEntityName = prdAction.getCreate();
        String mainEntityName = prdAction.getKill();

        switch(actionName) {
            case "increase":
                return translateIncreaseAction(prdAction, entityDefinition, propertyName, activeEnvironment, secondaryEntity, entitiesContext);
            case "decrease":
                return translateDecreaseAction(prdAction, entityDefinition, propertyName, activeEnvironment, secondaryEntity, entitiesContext);
            case "calculation":
                return translateCalculationAction(prdAction, entityDefinition, resultProp, activeEnvironment, secondaryEntity, entitiesContext);
            case "condition":
                return translateConditionAction(prdAction.getPRDCondition(), prdAction.getPRDThen(), prdAction.getPRDElse(), entityDefinition, activeEnvironment, entitiesList, secondaryEntity, entitiesContext);
            case "set":
                return translateSetAction(prdAction, entityDefinition, propertyName, activeEnvironment, secondaryEntity, entitiesContext);
            case "kill":
                return translateKillAction(entityDefinition, secondaryEntity, entitiesContext);
            case "replace":
                EntityDefinition mainEntity = getEntityDefinition(mainEntityName,entitiesList);
                EntityDefinition createdEntity = getEntityDefinition(createdEntityName,entitiesList);
                if ( entitiesContext == null) {
                    entitiesContext = new ContextImpl(mainEntity, createdEntity);
                }
                return translateReplaceAction(mainEntity, createdEntity, mode, entitiesContext);
            case "proximity":
                String sourceEntity = prdAction.getPRDBetween().getSourceEntity();
                String targetEntity = prdAction.getPRDBetween().getTargetEntity();
                String of = prdAction.getPRDEnvDepth().getOf();
                EntityDefinition sourceEntityDefinition = getEntityDefinition(sourceEntity, entitiesList);
                EntityDefinition targetEntityDefinition = getEntityDefinition(targetEntity,entitiesList);
                List<PRDAction> prdActions = prdAction.getPRDActions().getPRDAction();
                if ( entitiesContext == null) {
                    entitiesContext = new ContextImpl(sourceEntityDefinition, targetEntityDefinition);
                }
                return translateProximityAction(sourceEntityDefinition,targetEntityDefinition,prdActions, of, activeEnvironment, entitiesList, entitiesContext);
            default:
                return null;
        }
    }


    public static EntityDefinition getEntityDefinition(String entityName, List<EntityDefinition> entitiesList) throws Exception {
        for (EntityDefinition entityDefinition : entitiesList) {
            if ( entityDefinition.getName().equals(entityName)) {
                return entityDefinition;
            }
        }
        throw new EntityNotExistException(entityName);
    }

    public static IncreaseAction translateIncreaseAction(PRDAction prdAction, EntityDefinition entityDefinition, String propertyName, ActiveEnvironment activeEnvironment, SecondaryEntity secondaryEntity, Context entitiesContext) throws Exception {
        Expression expression;
        PropertyDefinition propertyDefinition = entityDefinition.getPropertyByName(propertyName);
        AbstractPropertyDefinition.PropertyType type = propertyDefinition.getType();
        if (type.equals(AbstractPropertyDefinition.PropertyType.DECIMAL) || type.equals(AbstractPropertyDefinition.PropertyType.FLOAT) ) {
            expression = ExpressionDecoder.decode(prdAction.getBy(), activeEnvironment, entityDefinition, type,"increase",secondaryEntity, entitiesContext);
            String expType = expression.getType();
            String lowerType = type.name().toLowerCase();
            if (!(lowerType.equals(expType) || (lowerType.equals("float") && expType.equals("decimal")))) {
                throw new MismatchTypesException("Expression in increase action", lowerType, expType);
            }
        }
        else {
            throw new PropertyNotMatchActionException(prdAction.getType(),propertyDefinition.getName(),entityDefinition.getName(),type.toString());
        }
        return new IncreaseAction(entityDefinition, expression, propertyDefinition, secondaryEntity, entitiesContext);
    }

    public static DecreaseAction translateDecreaseAction(PRDAction prdAction, EntityDefinition entityDefinition, String propertyName, ActiveEnvironment activeEnvironment, SecondaryEntity secondaryEntity, Context entitiesContext) throws Exception {
        Expression expression;
        PropertyDefinition propertyDefinition = entityDefinition.getPropertyByName(propertyName);
        AbstractPropertyDefinition.PropertyType type = propertyDefinition.getType();
        if (type.equals(AbstractPropertyDefinition.PropertyType.DECIMAL) || type.equals(AbstractPropertyDefinition.PropertyType.FLOAT) ) {
            expression = ExpressionDecoder.decode(prdAction.getBy(), activeEnvironment, entityDefinition, type,"decrease", secondaryEntity, entitiesContext);
            String expType = expression.getType();
            String lowerType = type.name().toLowerCase();
            if (!(lowerType.equals(expType) || (lowerType.equals("float") && expType.equals("decimal")))) {
                throw new MismatchTypesException("Expression in decrease action", lowerType, expType);
            }
        }
        else {
            throw new PropertyNotMatchActionException(prdAction.getType(),propertyDefinition.getName(),entityDefinition.getName(),type.toString());
        }
        return new DecreaseAction(entityDefinition, expression,propertyDefinition, secondaryEntity, entitiesContext);
    }

    public static CalculationAction translateCalculationAction(PRDAction prdAction, EntityDefinition entityDefinition, String resultProp, ActiveEnvironment activeEnvironment, SecondaryEntity secondaryEntity, Context entitiesContext) throws Exception {
        PRDMultiply prdMultiply = prdAction.getPRDMultiply();
        PRDDivide prdDivide = prdAction.getPRDDivide();
        PropertyDefinition propertyDefinition = entityDefinition.getPropertyByName(resultProp);
        AbstractPropertyDefinition.PropertyType type = propertyDefinition.getType();
        if (type.equals(AbstractPropertyDefinition.PropertyType.DECIMAL) || type.equals(AbstractPropertyDefinition.PropertyType.FLOAT)) {
            if (prdMultiply == null) {
                Expression expressionArg1 = ExpressionDecoder.decode(prdDivide.getArg1(),activeEnvironment,entityDefinition,type,"calculation", secondaryEntity, entitiesContext);
                Expression expressionArg2 = ExpressionDecoder.decode(prdDivide.getArg2(),activeEnvironment,entityDefinition,type,"calculation", secondaryEntity, entitiesContext);
                String exp1Type = expressionArg1.getType(), exp2Type = expressionArg2.getType();
                if ((exp1Type.equals("decimal") || exp1Type.equals("float")) && (exp2Type.equals("decimal") || exp2Type.equals("float")))
                    if ((float) expressionArg2.evaluate(null) != 0) {
                        return new DivisionAction(entityDefinition, propertyDefinition, expressionArg1, expressionArg2, secondaryEntity, entitiesContext);
                    } else throw new Exception("Division by zero! Invalid xml file");
                else
                    throw new MismatchTypesException("Expression in division action", "Decimal or Float", exp1Type + ", " + exp2Type);
            }
            else {
                Expression expressionArg1 = ExpressionDecoder.decode(prdMultiply.getArg1(),activeEnvironment,entityDefinition,type,"calculation",secondaryEntity, entitiesContext);
                Expression expressionArg2 = ExpressionDecoder.decode(prdMultiply.getArg2(),activeEnvironment,entityDefinition,type,"calculation", secondaryEntity, entitiesContext);
                String exp1Type = expressionArg1.getType(), exp2Type = expressionArg2.getType();
                if ((exp1Type.equals("decimal") || exp1Type.equals("float")) && (exp2Type.equals("decimal") || exp2Type.equals("float")))
                    return new MultiplicationAction(entityDefinition,propertyDefinition,expressionArg1,expressionArg2, secondaryEntity, entitiesContext);
                else
                    throw new MismatchTypesException("Expression in multiplication action", "Decimal or Float", exp1Type + ", " + exp2Type);
            }
        }
        throw new PropertyNotMatchActionException(prdAction.getType(),propertyDefinition.getName(),entityDefinition.getName(),type.toString());
    }

    public static ConditionAction translateConditionAction(PRDCondition prdCondition, PRDThen prdThen, PRDElse prdElse, EntityDefinition entityDefinition, ActiveEnvironment activeEnvironment, List<EntityDefinition> entityDefinitions, SecondaryEntity secondaryEntity, Context entitiesContext) throws  Exception {
        String singularity = prdCondition.getSingularity();
        boolean boolSingularity = !singularity.equals("multiple");
        String logic = prdCondition.getLogical();

        List<Action> thenActions = new ArrayList<>();
        List<Action> elseActions = new ArrayList<>();
        List<ConditionAction> conditions = new ArrayList<>();

        for (PRDAction prdAction1 : prdThen.getPRDAction()) {
            thenActions.add(translateAction(prdAction1, entityDefinitions, activeEnvironment, null, entitiesContext));
        }
        if (prdElse != null) {
            for (PRDAction prdAction2 : prdElse.getPRDAction()) {
                elseActions.add(translateAction(prdAction2, entityDefinitions, activeEnvironment, null, entitiesContext));
            }
        }
        if (prdCondition.getPRDCondition() != null) {
            for (PRDCondition prdCondition1 : prdCondition.getPRDCondition()) {
                conditions.add(translateConditionActionSecondary(prdCondition1, entityDefinition, activeEnvironment, secondaryEntity, entitiesContext, entityDefinitions));
            }
        }

        if (boolSingularity) {
            String operator = prdCondition.getOperator();
            String property = prdCondition.getProperty();
            Expression propertyExp = ExpressionDecoder.decode(property, activeEnvironment, entityDefinition, AbstractPropertyDefinition.PropertyType.STRING,"condition", secondaryEntity, entitiesContext);
            String propertyExpType = propertyExp.getType();
            AbstractPropertyDefinition.PropertyType type = AbstractPropertyDefinition.PropertyType.valueOf(propertyExpType.toUpperCase());
            String valueStr = prdCondition.getValue();
            Expression expression = ExpressionDecoder.decode(valueStr, activeEnvironment, entityDefinition, type, "condition", secondaryEntity, entitiesContext);
            String expType = expression.getType();
            if (expType.equals(propertyExpType) || (expType.equals("decimal")) && propertyExpType.equals("float"))
                return new SingularCondition(entityDefinition, propertyExp, expression, operator, thenActions, elseActions, secondaryEntity, entitiesContext);
            else
                throw new MismatchTypesException("Expression in condition action", propertyExpType, expType);
        }
        else
            return new MultipleCondition(conditions, logic, entityDefinition, thenActions, elseActions, secondaryEntity, entitiesContext);
    }

    public static ConditionAction translateConditionActionSecondary(PRDCondition prdCondition, EntityDefinition entityDefinition, ActiveEnvironment activeEnvironment, SecondaryEntity secondaryEntity, Context entitiesContext, List<EntityDefinition> entityDefinitions) throws Exception {
        String singularity = prdCondition.getSingularity();
        boolean boolSingularity = !singularity.equals("multiple");

        String logic = prdCondition.getLogical();

        List<Action> thenActions = new ArrayList<>();
        List<Action> elseActions = new ArrayList<>();
        List<ConditionAction> conditions = new ArrayList<>();

        String entityName = prdCondition.getEntity();
        if ( entityName != null) {
            entityDefinition = getEntityDefinition(entityName, entityDefinitions);
            if (!isEntityRelatedToContext(entitiesContext, entityDefinition)) {
                String primaryEntityName = entitiesContext.getPrimaryEntity().getName();
                if (entitiesContext.getSecondaryEntity() != null) {
                    String secondaryEntityName = entitiesContext.getSecondaryEntity().getName();
                    throw new EntityNotRelatedToActionException(entityDefinition.getName(), "condition", primaryEntityName + "," + secondaryEntityName);
                }
                throw new EntityNotRelatedToActionException(entityDefinition.getName(), "condition", primaryEntityName);
            }
            entitiesContext = createSubActionEntitiesContext(entityDefinition, entitiesContext);
        }

        if (prdCondition.getPRDCondition() != null) {
            for (PRDCondition prdCondition1 : prdCondition.getPRDCondition()) {
                conditions.add(translateConditionActionSecondary(prdCondition1, entityDefinition, activeEnvironment, secondaryEntity, entitiesContext, entityDefinitions));
            }
        }

        if (boolSingularity) {
            String operator = prdCondition.getOperator();
            String property = prdCondition.getProperty();
            Expression propertyExp = ExpressionDecoder.decode(property, activeEnvironment, entityDefinition, AbstractPropertyDefinition.PropertyType.STRING,"condition", secondaryEntity, entitiesContext);
            String propertyExpType = propertyExp.getType();
            AbstractPropertyDefinition.PropertyType type = AbstractPropertyDefinition.PropertyType.valueOf(propertyExpType.toUpperCase());
            String valueStr = prdCondition.getValue();
            Expression expression = ExpressionDecoder.decode(valueStr, activeEnvironment, entityDefinition, type, "condition", secondaryEntity, entitiesContext);
            String expType = expression.getType();
            if (expType.equals(propertyExpType) || (expType.equals("decimal")) && propertyExpType.equals("float"))
                return new SingularCondition(entityDefinition, propertyExp, expression, operator, thenActions, elseActions, null, entitiesContext);
            else
                throw new MismatchTypesException("Expression in condition action", propertyExpType, expType);
        }
        else
            return new MultipleCondition(conditions, logic, entityDefinition, thenActions, elseActions, null, entitiesContext);
    }


    public static SetAction translateSetAction(PRDAction prdAction, EntityDefinition entityDefinition, String propertyName, ActiveEnvironment activeEnvironment, SecondaryEntity secondaryEntity, Context entitiesContext) throws Exception {
        PropertyDefinition propertyDefinition = entityDefinition.getPropertyByName(propertyName);
        AbstractPropertyDefinition.PropertyType type = propertyDefinition.getType();
        Expression expression = ExpressionDecoder.decode(prdAction.getValue(), activeEnvironment,entityDefinition,type,"set", secondaryEntity, entitiesContext);
        String expType = expression.getType();
        String propertyType = type.name().toLowerCase();
        if ((propertyType.equals(expType)) || (propertyType.equals("float") && expType.equals("decimal")))
            return new SetAction(entityDefinition,expression,propertyDefinition, secondaryEntity, entitiesContext);
        else
            throw new MismatchTypesException("Expression in set action", propertyType, expType);
    }

    public static KillAction translateKillAction(EntityDefinition entityDefinition, SecondaryEntity secondaryEntity, Context entitiesContext) {
        return new KillAction(entityDefinition, secondaryEntity, entitiesContext);
    }


    public static ReplaceAction translateReplaceAction(EntityDefinition mainEntity, EntityDefinition createdEntity,String mode, Context entitiesContext) throws Exception {

        return new ReplaceAction(mode, mainEntity, createdEntity, entitiesContext);
    }

    public static ProximityAction translateProximityAction(EntityDefinition sourceEntity, EntityDefinition targetEntity, List<PRDAction> prdActions, String ofExpression, ActiveEnvironment activeEnvironment, List<EntityDefinition> entityDefinitions, Context entitiesContext) throws  Exception {
        if ( !isEntityRelatedToContext(entitiesContext,sourceEntity) || !isEntityRelatedToContext(entitiesContext,targetEntity)) {
            String name = null;
            if (!isEntityRelatedToContext(entitiesContext,sourceEntity)) {
                name = sourceEntity.getName();
            }
            if (!isEntityRelatedToContext(entitiesContext,targetEntity)) {
                name = targetEntity.getName();
            }
            String primaryEntityName = entitiesContext.getPrimaryEntity().getName();
            if (entitiesContext.getSecondaryEntity() != null) {
                String secondaryEntityName = entitiesContext.getSecondaryEntity().getName();
                throw new EntityNotRelatedToActionException(name,"replace", primaryEntityName + "," + secondaryEntityName);
            }
            throw new EntityNotRelatedToActionException(name,"replace", primaryEntityName);
        }
        entitiesContext = createSubActionEntitiesContext(sourceEntity,entitiesContext);
        Expression of = ExpressionDecoder.decode(ofExpression,activeEnvironment, sourceEntity, AbstractPropertyDefinition.PropertyType.DECIMAL,"proximity", null, entitiesContext);
        String type = of.getType();
        if ( !type.equals("float") && !type.equals("decimal") ) {
            throw new MismatchTypesException("Expression in condition action","number",type);
        }
        ProximityAction proximityAction = new ProximityAction(sourceEntity,targetEntity,of, entitiesContext);
        for (PRDAction prdAction : prdActions) {
            proximityAction.addAction(translateAction(prdAction, entityDefinitions, activeEnvironment, null, proximityAction.getEntitiesContext()));
        }
        return proximityAction;
    }


    public static SecondaryEntity translateSecondaryEntity(PRDAction prdAction, List<EntityDefinition> entitiesList, ActiveEnvironment activeEnvironment) throws Exception {
        EntityDefinition secondaryEntity = null;
        EntityDefinition primaryEntity = null;
        if ( prdAction.getPRDSecondaryEntity() != null) {
            if ( prdAction.getEntity() != null) {
                primaryEntity = getEntityDefinition(prdAction.getEntity(), entitiesList);
            }

            secondaryEntity = getEntityDefinition(prdAction.getPRDSecondaryEntity().getEntity(),entitiesList);
            Context entitiesContext = new ContextImpl(primaryEntity, secondaryEntity);
            PRDAction.PRDSecondaryEntity.PRDSelection selection = prdAction.getPRDSecondaryEntity().getPRDSelection();
            String count = selection.getCount();
            PRDCondition condition = selection.getPRDCondition();
            ConditionAction secondaryEntityCondition = translateSecondaryEntityCondition(condition,secondaryEntity,activeEnvironment, entitiesContext);
            return new SecondaryEntity(secondaryEntity,count,secondaryEntityCondition);
        }
        return null;
    }

    public static ConditionAction translateSecondaryEntityCondition(PRDCondition prdCondition, EntityDefinition secondaryEntity, ActiveEnvironment activeEnvironment, Context entitiesContext) throws Exception {
        if ( prdCondition == null) {
            return null;
        }
        String singularity = prdCondition.getSingularity();
        boolean singularityIsMulti = singularity.equals("multiple");

        //////// Return new multiple condition
        if (singularityIsMulti) {
            String logic = prdCondition.getLogical();
            List<ConditionAction> conditions = new ArrayList<>();
            if (prdCondition.getPRDCondition() != null) {
                for (PRDCondition prdSomeCondition : prdCondition.getPRDCondition()) {
                    conditions.add(translateSecondaryEntityCondition(prdSomeCondition,secondaryEntity,activeEnvironment,entitiesContext));
                }
            }
            return new MultipleCondition(conditions, logic, secondaryEntity, null, null, null, entitiesContext);
        }

        //////// Return new single condition
        return translateSecondaryEntitySingleCondition(prdCondition, secondaryEntity, activeEnvironment, entitiesContext);
    }


    public static SingularCondition translateSecondaryEntitySingleCondition(PRDCondition prdCondition, EntityDefinition secondaryEntity, ActiveEnvironment activeEnvironment, Context entitiesContext) throws Exception {
            String operator = prdCondition.getOperator();
            String property = prdCondition.getProperty();
            Expression propertyExp = ExpressionDecoder.decode(property, activeEnvironment, secondaryEntity, AbstractPropertyDefinition.PropertyType.STRING,"condition", null, entitiesContext);
            String propertyExpType = propertyExp.getType();
            AbstractPropertyDefinition.PropertyType type = AbstractPropertyDefinition.PropertyType.valueOf(propertyExpType.toUpperCase());
            String valueStr = prdCondition.getValue();
            Expression expression = ExpressionDecoder.decode(valueStr, activeEnvironment, secondaryEntity, type, "condition", null, entitiesContext);
            String expType = expression.getType();
            if (expType.equals(propertyExpType) || (expType.equals("decimal")) && propertyExpType.equals("float"))
                return new SingularCondition(secondaryEntity, propertyExp, expression, operator, null, null,null, entitiesContext);
            else
                throw new MismatchTypesException("Expression in condition action", propertyExpType, expType);
    }

    public static Context createEntitiesContext(EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) {
        if ( secondaryEntity == null) {
            return new ContextImpl(primaryEntity, null);
        }
        else {
            return new ContextImpl(primaryEntity, secondaryEntity.getSecondaryEntityDefinition());
        }
    }

    public static boolean isEntityRelatedToContext(Context entitiesContext, EntityDefinition entityDefinition) {
        if ( entitiesContext.getPrimaryEntity() == entityDefinition) {
            return true;
        }
        else if ( entitiesContext.getSecondaryEntity() != null) {
            return entitiesContext.getSecondaryEntity() == entityDefinition;
        }
        else
            return false;
    }

    public static Context createSubActionEntitiesContext(EntityDefinition entityDefinition, Context entitiesContext) {
        if ( entitiesContext.getSecondaryEntity() != null ) {
            if ( entityDefinition == entitiesContext.getPrimaryEntity()) {
                return entitiesContext;
            }
            else {
                return new ContextImpl(entitiesContext.getSecondaryEntity(), entitiesContext.getPrimaryEntity());
            }
        }
        else
            return entitiesContext;
    }

}
