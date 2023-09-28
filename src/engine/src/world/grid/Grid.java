package world.grid;

import world.entity.api.EntityInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid implements Serializable {
    private final int cols;
    private final int rows;
    private Map<GridCoordinate, EntityInstance> entityMap;

    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        entityMap = new HashMap<>();
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public boolean isCoordinateTaken(GridCoordinate coordinate) {
        return entityMap.containsKey(coordinate);
    }

    public void removeCoordinateFromMap(GridCoordinate coordinate) {
        entityMap.remove(coordinate);
    }

    public void addCoordinateToMap(EntityInstance entityInstance) {
        if ( entityMap.containsKey(entityInstance.getCoordinate())){
            return;
        }
        else {
            entityMap.put(entityInstance.getCoordinate(),entityInstance);
        }
    }

    public List<GridCoordinate> getUntakenCoordinates() {
        List<GridCoordinate> untakenCoordinates = new ArrayList<>();
        for (int i = 1; i <= cols; i++) {
            for ( int j = 1; j <= rows; j++) {
                GridCoordinate newCoordinate = new GridCoordinate(i,j,cols,rows);
                if ( !entityMap.containsKey(newCoordinate))
                    untakenCoordinates.add(newCoordinate);
                
            }
        }
        return untakenCoordinates;
    }
}

