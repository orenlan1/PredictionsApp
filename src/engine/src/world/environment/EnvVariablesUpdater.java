package world.environment;

import dto.UserInputEnvironmentVariableDTO;
import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyInstance;
import world.property.impl.FloatPropertyDefinition;
import world.property.impl.IntegerPropertyDefinition;

import java.io.Serializable;

public class EnvVariablesUpdater implements Serializable {
    public void updateVariable(PropertyInstance propertyInstance, AbstractPropertyDefinition.PropertyType type, UserInputEnvironmentVariableDTO dto) throws NumberFormatException {
        if (dto.getValue() != null) {
            switch (type) {
                case DECIMAL:
                    Integer intValue = Integer.parseInt(dto.getValue());
                    propertyInstance.updateValue(intValue, 0);
                    break;
                case FLOAT:
                    Float floatValue = Float.parseFloat(dto.getValue());
                    propertyInstance.updateValue(floatValue, 0);
                    break;
                case BOOLEAN:
                    if (dto.getValue().equals("False"))
                        propertyInstance.updateValue(false, 0);
                    else if (dto.getValue().equals("True"))
                        propertyInstance.updateValue(true, 0);
                    break;
                case STRING:
                    propertyInstance.updateValue((dto.getValue()), 0);
                    break;
            }
        }
    }
}
