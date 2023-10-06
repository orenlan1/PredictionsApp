package predictions.allocations;

import dto.AllocationDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllocationsManager {
    private final Map<String, Map<Integer, Allocation>> allAllocations;
    Integer allocationID;

    public AllocationsManager() {
        allAllocations = new LinkedHashMap<>();
        allocationID = 1;
    }

    public Integer getAllocationID() {
        return allocationID;
    }

    public synchronized void addAllocation(String username, Allocation newAllocation) {
        if (!allAllocations.containsKey(username))
            allAllocations.put(username, new LinkedHashMap<>());

        allAllocations.get(username).put(allocationID++, newAllocation);
    }

    public synchronized Allocation getAllocation(String username, Integer id) {
        Map<Integer, Allocation> idToAllocation = allAllocations.get(username);
        if (idToAllocation != null) {
            return idToAllocation.get(id);
        }
        return null;
    }

    public synchronized Map<Integer, Allocation> getUserAllocations(String username) {
        if (!allAllocations.containsKey(username))
            allAllocations.put(username, new LinkedHashMap<>());

        return allAllocations.get(username);
    }

    public synchronized List<Allocation> getAllAllocations() {
        List<Allocation> newAllocations = new ArrayList<>();
        for ( Map<Integer,Allocation> userAllocations : allAllocations.values()) {
            newAllocations.addAll(userAllocations.values());
        }
        return newAllocations;
    }
}
