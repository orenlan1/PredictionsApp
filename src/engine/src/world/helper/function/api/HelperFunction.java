package world.helper.function.api;

import world.entity.api.EntityInstance;
import world.environment.api.ActiveEnvironment;

import java.util.List;

public interface HelperFunction {
    String getName();
    Object invoke(EntityInstance entityInstance, Integer currTick) throws Exception;
    int getNumOfArgs();
    List<String> getTypeOfArgs();
}
