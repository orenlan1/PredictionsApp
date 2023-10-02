package world.simulation;

import dto.*;
import world.World;
import world.entity.api.EntityDefinition;
import world.factory.DTOFactory;
import world.property.api.PropertyDefinition;
import world.rule.api.Rule;

import java.util.*;
import java.util.ArrayList;


public class SimulationInfoBuilder {

    public SimulationInfoDTO createSimulationInfo(World world) {
        DTOFactory dtoFactory = new DTOFactory();
        List<EntityDTO> entityDTOList = createEntityDTOList(world.getNameToEntityDefinition());
        List<RuleDTO> ruleDTOList = createRuleDTOList(world.getRules());
        List<PropertyDTO> envVariablesList = new ArrayList<>();
        GridDTO gridDTO = dtoFactory.createGridDTO(world.getGrid());
        for (PropertyDefinition envProperty : world.getEnvironmentVariablesManager().getEnvironmentVariables()) {
            envVariablesList.add(dtoFactory.createPropertyDTO(envProperty));
        }
        return new SimulationInfoDTO(entityDTOList, ruleDTOList, envVariablesList, gridDTO);
    }

    public List<EntityDTO> createEntityDTOList(Map<String, EntityDefinition> nameToEntity) {
        DTOFactory dtoFactory = new DTOFactory();
        List<EntityDTO> entityDTOList = new ArrayList<>();
        for (EntityDefinition entityDefinition : nameToEntity.values()) {
            entityDTOList.add(dtoFactory.createEntityDTO(entityDefinition));
        }
        return entityDTOList;
    }

    public List<RuleDTO> createRuleDTOList(List<Rule> rulesList) {
        DTOFactory dtoFactory = new DTOFactory();
        List<RuleDTO> rulesDTOList = new ArrayList<>();
        for (Rule rule : rulesList) {
            rulesDTOList.add(dtoFactory.creatRuleDTO(rule));
        }
        return rulesDTOList;
    }
}
