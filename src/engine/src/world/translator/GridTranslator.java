package world.translator;

import jaxb.generated.PRDWorld.PRDGrid;
import world.exceptions.GridException;
import world.grid.Grid;

public class GridTranslator {
    public static Grid translateGrid(PRDGrid prdGrid) throws Exception {
        int cols = prdGrid.getColumns();;
        int rows = prdGrid.getRows();
        Integer columns = cols;
        Integer rowsInt = rows;
        if ( cols < 10 || cols > 100 ) throw new GridException(columns.toString(), "columns");
        if ( rows < 10 || rows > 100 ) throw new GridException(rowsInt.toString(), "rows");
        return new Grid(prdGrid.getColumns(), prdGrid.getRows());
    }
}
