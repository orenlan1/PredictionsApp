package world.simulation;

import dto.EntityDTO;
import dto.RuleDTO;
import dto.SimulationInfoDTO;
import dto.TerminationDTO;
import world.World;
import world.entity.api.EntityDefinition;
import world.factory.DTOFactory;
import world.rule.api.Rule;

import java.util.*;
import java.util.ArrayList;


public class SimulationInfoBuilder {

    public SimulationInfoDTO createSimulationInfo(World world) {
        DTOFactory dtoFactory = new DTOFactory();
        List<EntityDTO> entityDTOList = createEntityDTOList(world.getNameToEntityDefinition());
        List<RuleDTO> ruleDTOList = createRuleDTOList(world.getRules());
        TerminationDTO terminationDTO = dtoFactory.createTerminationDTO(world.getTermination());
        return new SimulationInfoDTO(entityDTOList,ruleDTOList,terminationDTO);
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
