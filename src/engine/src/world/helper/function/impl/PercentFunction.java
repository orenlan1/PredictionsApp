package world.helper.function.impl;

import world.entity.api.EntityInstance;
import world.exceptions.MismatchTypesException;
import world.expressions.api.Expression;
import world.expressions.impl.HelperFunctionExpression;

public class PercentFunction extends HelperFunctionImpl {

    private final Expression value;
    private final Expression percent;

    public PercentFunction(Expression value, Expression percent) {
        super("percent",2, "expression,expression");
        this.value = value;
        this.percent = percent;
    }

    public Object percentInvoke(EntityInstance firstInstance, EntityInstance secondInstance, Integer currTick) throws Exception {
        Object val = null;
        Object percentOfVal = null;
        if (value instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperFunctionExpression = (HelperFunctionExpression) value;
            try {
                val = helperFunctionExpression.evaluate(firstInstance, currTick);
            } catch (Exception e) {
                if ( secondInstance != null)
                    val = helperFunctionExpression.evaluate(secondInstance, currTick);
            }
        }
        else {
            val = value.evaluate(firstInstance);
        }

        if (percent instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperFunctionExpression = (HelperFunctionExpression) percent;
            try {
                percentOfVal = helperFunctionExpression.evaluate(firstInstance, currTick);
            } catch (Exception e) {
                if ( secondInstance != null)
                    percentOfVal = helperFunctionExpression.evaluate(secondInstance, currTick);
            }
        }
        else {
            percentOfVal = percent.evaluate(firstInstance);
        }

        if (!(val instanceof Float) && !(val instanceof Integer)) {
            if ( val instanceof String) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "string");
            else if ( val instanceof Boolean) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "boolean");
        }
        else if (!(percentOfVal instanceof Float) && !(percentOfVal instanceof Integer)) {
            if ( percentOfVal instanceof String) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "string");
            else if ( percentOfVal instanceof Boolean) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "boolean");
        }
        if ( percentOfVal instanceof  Float && val instanceof  Float)
            return ( (float) percentOfVal / 100) *  (float) val;
        else if (percentOfVal instanceof Float && val instanceof Integer)
            return ((float) percentOfVal / 100 ) * ((int) val);
        else if (percentOfVal instanceof Integer && val instanceof Float)
            return ((int) percentOfVal / 100) * ((float) val);
        else if (percentOfVal instanceof Integer && val instanceof  Integer)
            return ((int) percentOfVal / 100)  * ((int) val);
        else
            throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "non-number");
    }




    @Override
    public Object invoke(EntityInstance entityInstance, Integer currTick)  throws Exception {
        Object val = null;
        Object percentOfVal = null;
        if (value instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperFunctionExpression = (HelperFunctionExpression) value;
            val = helperFunctionExpression.evaluate(entityInstance, currTick);
        }
        else {
            val = value.evaluate(entityInstance);
        }

        if (percent instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperFunctionExpression = (HelperFunctionExpression) percent;
            percentOfVal = helperFunctionExpression.evaluate(entityInstance, currTick);
        }
        else {
            percentOfVal = percent.evaluate(entityInstance);
        }

        if (!(val instanceof Float)) {
            if ( val instanceof String) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "string");
            else if ( val instanceof Boolean) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "boolean");
        }
        else if (!(percentOfVal instanceof Float)) {
            if ( percentOfVal instanceof String) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "string");
            else if ( percentOfVal instanceof Boolean) throw new MismatchTypesException("Error while running simulation.\n" +
                    "Percent function","number", "boolean");
        }

        return ( (float) percentOfVal / 100) *  (float) val;

    }

}
