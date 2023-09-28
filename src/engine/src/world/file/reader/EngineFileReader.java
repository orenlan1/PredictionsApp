package world.file.reader;

import jaxb.generated.*;
//import generated.*;
import world.World;
import world.entity.api.EntityDefinition;
import world.environment.api.ActiveEnvironment;
import world.environment.api.EnvironmentVariablesManager;
import world.grid.Grid;
import world.property.api.PropertyDefinition;
import world.property.impl.PropertyInstanceImpl;
import world.termination.Termination;
import world.translator.*;
import world.rule.api.Rule;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class EngineFileReader {
    private final static String JAXB_XML_PACKAGE_NAME = "jaxb.generated";

    public World checkFileValidation(String fileName) throws Exception {
        World world = new World();
        File file = new File(fileName);
        if (!file.exists()) {
            throw new Exception("The xml file does not exist.");
        }
        InputStream inputStream = new FileInputStream(file);
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        PRDWorld prdWorld = (PRDWorld) u.unmarshal(inputStream);

        //translations
        EnvironmentVariablesManager environmentVariablesManager = EnvironmentTranslator.translateEnvironment(prdWorld.getPRDEnvironment());
        ActiveEnvironment activeEnvironment = environmentVariablesManager.createActiveEnvironment();

        for (PropertyDefinition propertyDefinition : environmentVariablesManager.getEnvironmentVariables()) {
            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition, propertyDefinition.generateValue()));
        }
        List<EntityDefinition> entityDefinitions = EntityTranslator.translateEntities(prdWorld.getPRDEntities());


        //updating world
        world.setEnvironmentVariablesManager(environmentVariablesManager);
        world.setActiveEnvironment(activeEnvironment);
        for (EntityDefinition entity : entityDefinitions) {
            world.addEntityDefinition(entity.getName(), entity);
        }

        for (PRDRule prdRule : prdWorld.getPRDRules().getPRDRule())
        {
            Rule newRule = RuleTranslator.translateRule(prdRule, entityDefinitions, activeEnvironment);
            world.addRule(newRule);
        }

        // Setting termination
        Termination termination = TerminationTranslator.translateTermination(prdWorld.getPRDTermination());
        world.setTermination(termination);
        // Setting grid
        Grid grid = GridTranslator.translateGrid(prdWorld.getPRDGrid());
        world.setGrid(grid);
        // Setting thread count
        world.setThreadCount(prdWorld.getPRDThreadCount());

        return world;
    }
}




