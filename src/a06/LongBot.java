import battleship.BattleShip2;
import battleship.BattleShipBot;
import java.awt.Point;
import java.util.*;

/**
 * This is the class header.
 *
 * @date Apr 10, 2022
 * @author DUC LONG NGUYEN (Paul)
 *
 *
 * DATA STRUCTURE/ALGORITHMS used:
 * - Arrays
 * - Recursion
 * - Sorting
 * - Collections
 *
 */
public class LongBot implements BattleShipBot {
    private int gameSize;
    private BattleShip2 battleShip;
    private boolean[][] map;        /**Stored the points are shot.*/
    private boolean[][] isCheck;    /**Stored the points are checked.*/
    private int trackingSunkShips;  /**The remaining points that need to shot*/
    /**
     * Constructor keeps a copy of the BattleShip instance
     * Create instances of any Data Structures and initialize any variables here
     * @param b previously created battleship instance - should be a new game
     *
     *  DATA STRUCTURE/ALGORITHMS used:
     *  * - Arrays
     */
    @Override
    public void initialize(BattleShip2 b) {
        battleShip = b;
        gameSize = BattleShip2.BOARD_SIZE;
        // Need to use a Seed if you want the same results to occur from run to run
        // This is needed if you are trying to improve the performance of your code
        map=new boolean[gameSize][gameSize];
        isCheck=new boolean[gameSize][gameSize];
        for(int row=0; row<gameSize; row++)
            for (int column=0; column<gameSize; column++) {
                map[row][column] = false;
                isCheck[row][column]=false;
            }
        int shotCorrect = 0;
        for (int shipSize : battleShip.getShipSizes())
            shotCorrect+=shipSize;
        trackingSunkShips =shotCorrect;
    }
    /**The ships when placed on the board cannot touch in the vertical or horizontal direction.
     * So, if we know a ship, all points around the ship are gonna be not to shot
     * shotPoints method gets all correct shot points based on x direction (vertical) or  y direction (horizontal)
     * @param isVertical value to know if shotPoints method gets all correct shot points based on x direction (vertical)
     *                   isVertical=true: gets all correct shot points based on x direction (vertical). Otherwise, =false
     * @return An ArrayList of all correct shot points based on x direction (vertical) or  y direction (horizontal)
     *
     *  * DATA STRUCTURE/ALGORITHMS used:
     *  * - Arrays
     *  * - Sorting
     *  * - Collections
     *
     * */
    private ArrayList<int[]> shotPoints(boolean isVertical){
        ArrayList<int[]> listShips = new ArrayList<>();
        for (int countRow=0; countRow<(isVertical ? gameSize : gameSize-1); countRow++)
            for (int countColumn=0; countColumn<(isVertical ? gameSize-1 : gameSize); countColumn++)
                if (map[countRow][countColumn] && map[countRow][countColumn]
                        ==map[isVertical ? countRow : (countRow+1)][isVertical ? (countColumn+1) : countColumn]) {
                    listShips.add(new int[]{countRow,countColumn});
                    listShips.add(new int[]{isVertical ? countRow : (countRow + 1), isVertical ? (countColumn + 1) : countColumn});
                }
        Collections.sort(listShips, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return isVertical ? (o1[0]==o2[0] ? o1[1]-o2[1] : o1[0]-o2[0]) : o1[1]==o2[1] ? o1[0]-o2[0] : o1[1]-o2[1];
            }
        });
        ArrayList<int[]> _listShips = new ArrayList<>();    //remove duplicates
        if (listShips.size()>0) _listShips.add(listShips.get(0));
        for (int count=1; count<listShips.size();count++)
            if (listShips.get(count)[0]==listShips.get(count-1)[0] && listShips.get(count)[1]==listShips.get(count-1)[1]){}
            else _listShips.add(listShips.get(count));
        return _listShips;
    }
    /**The ships when placed on the board cannot touch in the vertical or horizontal direction.
     * So, if we know a ship, all points around the ship are gonna be not to shot
     * checkedSidesShip method checks and ignores all points around the ships
     * @param listPointVerticals list all correct shot points based on x direction (vertical)
     * @param listPointHorizontals list all correct shot points based on y direction (horizontal)
     *
     *  * DATA STRUCTURE/ALGORITHMS used:
     *  * - Arrays
     *  * - Collections
     * */
    private void checkedSidesShip(ArrayList<int[]> listPointVerticals, ArrayList<int[]> listPointHorizontals){
        for (int count=0; count<listPointVerticals.size(); count++){
            if (listPointVerticals.get(count)[0]>0)
                isCheck[listPointVerticals.get(count)[0]-1][listPointVerticals.get(count)[1]] =true;
            if (listPointVerticals.get(count)[0]<gameSize-1)
                isCheck[listPointVerticals.get(count)[0]+1][listPointVerticals.get(count)[1]] =true;
        }
        for (int count=0; count<listPointHorizontals.size(); count++){
            if (listPointHorizontals.get(count)[1]>0)
                isCheck[listPointHorizontals.get(count)[0]][listPointHorizontals.get(count)[1]-1] =true;
            if (listPointHorizontals.get(count)[1]<gameSize-1)
                isCheck[listPointHorizontals.get(count)[0]][listPointHorizontals.get(count)[1]+1] =true;
        }
    }
    /**The setShot method shots all the points based on checked and selected points from checkShotDirection and checkShot method
     * @param countRow the index of row where the shot point is correct
     * @param countColumn the index of column where the shot point is correct
     * @param point the point to shot
     * @param endRow the last row of the diagonal line
     * @param endColumn the last column of the diagonal line
     *
     *  * DATA STRUCTURE/ALGORITHMS used:
     *  * - Arrays
     *  * - Recursion
     * */
    private void setShot(int countRow, int countColumn, Point point, int endRow, int endColumn){
        if ((countRow>=0 && countRow<gameSize) && (countColumn>=0 && countColumn<gameSize) &&
                !isCheck[countRow][countColumn] && trackingSunkShips >0 && point!=null && !map[countRow][countColumn]) {
            isCheck[countRow][countColumn] = true;
            if (battleShip.shoot(point)) {
                map[countRow][countColumn] = true;
                trackingSunkShips--;
                /**determined if this point is located the x direction (left and right) or y direction of original point*/
                boolean isX=false;
                int[] eastPoint = new int[]{countRow, countColumn<gameSize-1 ? countColumn+1 : -1};
                int[] westPoint = new int[]{countRow, countColumn>0 ? countColumn-1 : -1};
                if ((eastPoint[0]!=-1 && eastPoint[1]!=-1 && map[eastPoint[0]][eastPoint[1]])
                        || (westPoint[0]!=-1 && westPoint[1]!=-1 && map[westPoint[0]][westPoint[1]]))
                    isX=true;
                /**recurs checkShot method to check around points again and again (recursion) until around shot points are incorrect*/
                checkShot(countRow, countColumn, endRow, endColumn, isX, !isX);
            }
        }
    }
    /**The checkShotDirection method checks if all the points existed based on selected points from checkShot method
     * @param isCheckNorth check if the selected point is located the north of correct shot point and if this north point existed
     * @param isCheckSouth check if the selected point is located the south of correct shot point and if this south point existed
     * @param isCheckEast check if the selected point is located the east of correct shot point and if this east point existed
     * @param isCheckWest check if the selected point is located the west of correct shot point and if this west point existed
     * @param countRow the index of row where the shot point is correct
     * @param countColumn the index of column where the shot point is correct
     * @param endRow the last row of the diagonal line
     * @param endColumn the last column of the diagonal line
     * @param northPoint the north point of the correct shot point
     * @param southPoint the south point of the correct shot point
     * @param eastPoint the east point of the correct shot point
     * @param westPoint the west point of the correct shot point
     * */
    private void checkShotDirection(boolean isCheckNorth, boolean isCheckSouth, boolean isCheckEast, boolean isCheckWest,
                                    int countRow, int countColumn, int endRow, int endColumn,
                                    Point northPoint, Point southPoint, Point eastPoint, Point westPoint){
        if (!isCheckSouth && (!isCheckEast && !isCheckWest || (countRow < gameSize - 1)))
            setShot(countRow+1, countColumn,southPoint,endRow,endColumn);
        if (!isCheckNorth && (!isCheckEast && !isCheckWest || (countRow > 0)))
            setShot(countRow-1, countColumn,northPoint,endRow,endColumn);
        if (!isCheckWest && (!isCheckNorth && !isCheckSouth || (countColumn > 0)))
            setShot(countRow, countColumn-1,westPoint,endRow,endColumn);
        if (!isCheckEast && (!isCheckNorth && !isCheckSouth || (countColumn < gameSize - 1)))
            setShot(countRow, countColumn+1,eastPoint,endRow,endColumn);
    }
    /**The checkShot method gets all the points (4 points) around the correct shot point
     * @param countRow the index of row where the shot point is correct
     * @param countColumn the index of column where the shot point is correct
     * @param endRow the last row of the diagonal line
     * @param endColumn the last column of the diagonal line
     * @param isX if diagonalLine method gets 2 points based on the x direction (right and left of correct point)
     * @param isY if diagonalLine method gets 2 points based on the y direction (top and bottom of correct point)
     * E.g:   0 1 2 3
     *      0   ?
     *      1 ? * ?     <--- The correct shot point is [1,1] => get 4 points around the correct point if isX,isY=false
     *      2   ?             => get 2 points ([1,0] and [1,2] if isX=true) or 2 points ([0,1] and [2,1] if isY=true)
     *      3
     * */
    private void checkShot(int countRow, int countColumn, int endRow, int endColumn, boolean isX, boolean isY){
        Point northPoint = countRow==0 ? null : new Point(countRow-1, countColumn);
        Point southPoint = countRow==endRow-1 ? null : new Point(countRow+1, countColumn);
        Point eastPoint = countColumn==endColumn-1 ? null : new Point(countRow, countColumn+1);
        Point westPoint = countColumn==0 ? null : new Point(countRow, countColumn-1);
        if (countRow==0)
            checkShotDirection(isY || !isX,false,false,false,
                    countRow,countColumn,endRow,endColumn,null,
                    (!isX || isY) ? southPoint : null, (isX || !isY) ? eastPoint : null,  (isX || !isY) ? westPoint : null);
        else if (countRow==endRow-1)
            checkShotDirection(false,isY || !isX,false,false,
                    countRow,countColumn,endRow,endColumn, (!isX || isY) ? northPoint : null,
                    (!isX || isY) ? southPoint : null, (isX || !isY) ? eastPoint : null, (isX || !isY) ? westPoint : null);
        else if (countColumn==0)
            checkShotDirection(false,false,false, isX || !isY,
                    countRow,countColumn,endRow,endColumn, (!isX || isY) ? northPoint : null,
                    (!isX || isY) ? southPoint : null, (isX || !isY) ? eastPoint : null,null);
        else if (countColumn==endColumn-1)
            checkShotDirection(false,false,isX || !isY,false,
                    countRow,countColumn,endRow,endColumn,(!isX || isY) ? northPoint : null,
                    (!isX || isY) ? southPoint : null, (isX || !isY) ? eastPoint : null, (isX || !isY) ? westPoint : null);
        else checkShotDirection(false,false,false, false,
                    countRow,countColumn,endRow,endColumn,(!isX || isY) ? northPoint : null,
                    (!isX || isY) ? southPoint : null, (isX || !isY) ? eastPoint : null, (isX || !isY) ? westPoint : null);
    }
    /**The diagonalLine method shots all the points in a diagonal line of the game board
     * @param beginRow the first row of the diagonal line
     * @param endRow the last row of the diagonal line
     * @param beginColumn the first column of the diagonal line
     * @param endColumn the last column of the diagonal line
     * @param diagonalIndex the index of selected diagonal line
     * E.g:   0 1 2 3
     *      0 *   *
     *      1   *   * <---- diagonalIndex = 2
     *      2     *
     *      3       * <---- diagonalIndex = 0
     *
     *
     *  * DATA STRUCTURE/ALGORITHMS used:
     *  * - Arrays
     *
     * */
    private void diagonalLine(int beginRow, int endRow, int beginColumn, int endColumn, int diagonalIndex){
        for (int countRow=beginRow; countRow<endRow; countRow++)
            for (int countColumn = beginColumn; countColumn < endColumn; countColumn++)
                if ((countRow==countColumn+diagonalIndex) ||(countRow==countColumn-diagonalIndex))
                    if (!isCheck[countRow][countColumn]  && !map[countRow][countColumn] && trackingSunkShips >0) {
                        isCheck[countRow][countColumn] = true;
                        if (battleShip.shoot(new Point(countRow, countColumn))) {   //shot
                            map[countRow][countColumn] = true;
                            trackingSunkShips--;
                            checkShot(countRow, countColumn, endRow, endColumn, false, false);
                        }
                    }
    }
    /**
     * Create a random shot and calls the battleship shoot method
     * Put all logic here (or in other methods called from here)
     * The BattleShip API will call your code until all ships are sunk
     */
    @Override
    public void fireShot() {
        int beginRow = 0;
        int beginColumn = 0;
        int endRow=gameSize;
        int endColumn=gameSize;
        /**First, shot all points in the diagonal line with diagonalIndex={0,8}*/
        for (int count=0; count<gameSize; count=count+8){
            diagonalLine(beginRow,endRow,beginColumn,endColumn, count);
            checkedSidesShip(shotPoints(true), shotPoints(false));
        }
        /**Then, shot all points in the diagonal line with diagonalIndex={4,12}*/
        for (int count=4; count<gameSize; count=count+8){
            diagonalLine(beginRow,endRow,beginColumn,endColumn, count);
            checkedSidesShip(shotPoints(true), shotPoints(false));
        }
        /**Then, shot all points in the diagonal line with diagonalIndex={2,6,10,14}*/
        if (trackingSunkShips >0)
            for (int count=2; count<gameSize; count=count+4){
                diagonalLine(beginRow,endRow,beginColumn,endColumn, count);
                checkedSidesShip(shotPoints(true), shotPoints(false));
            }
//        System.out.println("total shot: " + battleShip.totalShotsTaken());
    }
    /**
     * Authorship of the solution - must return names of all students that contributed to
     * the solution
     * @return names of the authors of the solution
     */
    @Override
    public String getAuthors() {
        return "Nguyen Duc Long (CSAIT Student)";
    }
}