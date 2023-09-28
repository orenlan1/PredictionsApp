package world.translator;

import jaxb.generated.PRDBySecond;
import jaxb.generated.PRDByTicks;
import jaxb.generated.PRDTermination;
import world.termination.Termination;

import java.util.List;

public class TerminationTranslator {
    public static Termination translateTermination(PRDTermination prdTermination) {
        Integer ticks = null;
        Integer seconds = null;
        Object byUser = prdTermination.getPRDByUser();
        if ( byUser != null) {
            return new Termination(ticks, seconds,true);
        }
        List<Object> ticksOrSecond = prdTermination.getPRDBySecondOrPRDByTicks();
        Object possibleTicks = ticksOrSecond.get(0), possibleSeconds = null;
        if (ticksOrSecond.size() > 1)
            possibleSeconds = ticksOrSecond.get(1);

        if (possibleTicks instanceof PRDByTicks) {
            ticks = ((PRDByTicks) possibleTicks).getCount();
        }
        else if (possibleTicks instanceof PRDBySecond) {
            seconds = ((PRDBySecond) possibleTicks).getCount();
        }
        if (possibleSeconds instanceof PRDBySecond) {
            seconds = ((PRDBySecond) possibleSeconds).getCount();
        }
        return new Termination(ticks, seconds,false);
    }
}
