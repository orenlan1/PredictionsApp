package world.grid;

import world.entity.api.EntityInstance;

import java.io.Serializable;
import java.util.Random;
import java.util.List;

public class GridCoordinate implements Serializable {
    private int x;
    private int y;

    private final int maxCols;
    private final int maxRows;

    public GridCoordinate(int maxRows, int maxCols) {
        this.maxRows = maxRows;
        this.maxCols = maxCols;
    }

    public GridCoordinate(int x, int y, int maxCols, int maxRows) {
        this.x = x;
        this.y = y;
        this.maxCols = maxCols;
        this.maxRows = maxRows;
    }

    public void setRandomlyCoordinate(Grid grid, EntityInstance entityInstance) {
        List<GridCoordinate> gridCoordinateList = grid.getUntakenCoordinates();
        Random random = new Random();
        int index = random.nextInt(gridCoordinateList.size());
        GridCoordinate instanceCoordinate = gridCoordinateList.get(index);
        entityInstance.setCoordinate(instanceCoordinate);
        grid.addCoordinateToMap(entityInstance);
    }

    public boolean move(Direction direction, Grid grid) {
        this.x = (x + direction.getDeltaX() > maxCols) ? (x + direction.getDeltaX()) % maxCols : x + direction.getDeltaX();
        this.y = (y + direction.getDeltaY() > maxRows) ? (y + direction.getDeltaY()) % maxRows : y + direction.getDeltaY();
        return !grid.isCoordinateTaken(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCoordinateInProximity(GridCoordinate targetCoordinate,int proximityLevel) {
        for (int k = 1; k <= proximityLevel; k++) {
            for (int i = -proximityLevel; i <= proximityLevel; i++) {
                for (int j = -proximityLevel; j <= proximityLevel; j++) {
                    if (Math.abs(i) == proximityLevel || Math.abs(j) == proximityLevel) {
                        int x = this.x + i;
                        int y = this.y + j;
                        if (x > maxCols)
                            x = x % maxCols;
                        if (y > maxRows)
                            y = y % maxRows;
                        if (x == targetCoordinate.getX() && y == targetCoordinate.getY()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }



   public GridCoordinate cloneCoordinate() {
        GridCoordinate newCoordinate = new GridCoordinate(this.maxRows ,this.maxCols);
        newCoordinate.x = this.x;
        newCoordinate.y = this.y;
        return newCoordinate;
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GridCoordinate coordinate = (GridCoordinate) o;

        return x == coordinate.x && y == coordinate.y;
    }

    @Override
    public int hashCode() {
        return x + y;
    }
}
