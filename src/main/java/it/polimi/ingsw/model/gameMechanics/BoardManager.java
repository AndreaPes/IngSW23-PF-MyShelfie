package it.polimi.ingsw.model.gameMechanics;

import it.polimi.ingsw.AbstractListenable;
import it.polimi.ingsw.model.gameEntity.Board;
import it.polimi.ingsw.model.gameEntity.Bag;
import it.polimi.ingsw.model.gameEntity.Coordinate;
import it.polimi.ingsw.model.gameEntity.ItemTile;
import it.polimi.ingsw.model.gameEntity.enums.ItemTileType;
import it.polimi.ingsw.model.gameState.events.BoardUpdateEvent;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that manages the board.
 */
public class BoardManager extends AbstractListenable {
    /**
     * The board of the game.
     */
    final Board board;
    /**
     * The bag of the game.
     */
    final Bag bag;

    /**
     * Constructor of the class.
     *
     * @param board the board of the game
     * @param bag the bag of the game
     */
    public BoardManager( Board board, Bag bag) {
        this.board = board;
        this.bag = bag;
    }

    /**
     * Checks if the board is ready for the refill.
     *
     * @return true if the board is ready, false otherwise
     */
    public boolean isRefillTime(){

        for (int row = 0; row < board.getROWS(); row++) {
            for (int col = 0; col < board.getCOLUMNS(); col++) {
                if (!(board.getBoardCell(row,col).isPlayable())) continue;
                if ((board.getBoardCell(row,col).getItemTile().getItemTileType()!=ItemTileType.EMPTY) && !(board.isAlone(row,col))) return false;
            }
        }
        return true;
    }

    /**
     * Refills the board with new random item tiles from the bag
     */
    public void refillBoard() {

        bag.shuffle();

        for (int row = 0; row < board.getROWS(); row++) {
            for (int col = 0; col < board.getCOLUMNS(); col++) {
                if (!(board.getBoardCell(row,col).isPlayable())) continue;
                if (board.getBoardCell(row,col).getItemTile().getItemTileType()==ItemTileType.EMPTY) {
                    board.putItemTile(row,col, bag.getRandomItemTile());
                }
            }
        }

        notifyAllListeners(new BoardUpdateEvent(board, true));
    }

    /**
     * This method grabs the item tiles from the board and returns them.
     *
     * @param coordinates the coordinates of the item tiles to grab
     * @return the item tiles grabbed
     */
    public List<ItemTile> grabItemTiles(List<Coordinate> coordinates) throws BreakRulesException {

        int size = coordinates.size();
        if (size < 1 || size > 3) throw new BreakRulesException(BreakRules.TOO_MUCH_TILES_SELECTED);
        if (new HashSet<>(coordinates).size()<size) throw new BreakRulesException(BreakRules.DUPLICATE_TILES_SELECTED);

        if (!(isLined(coordinates))) throw new BreakRulesException(BreakRules.ALONE_TILE);


        for (Coordinate coordinate : coordinates) {
            int row = coordinate.getRow();
            int col = coordinate.getCol();

            if (!board.getBoardCell(row,col).isPlayable() ) throw new BreakRulesException(BreakRules.NOT_PLAYABLE_TILE);
            if (board.getBoardCell(row,col).getItemTile().getItemTileType() == ItemTileType.EMPTY) throw new BreakRulesException(BreakRules.EMPTY_CELL);
            if (!hasSideFree(row,col)) throw new BreakRulesException(BreakRules.SURROUNDED_TILE);
            if (board.isAlone(row,col))throw new BreakRulesException(BreakRules.ALONE_TILE);
        }

        List<ItemTile> itemTileList = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            int row = coordinate.getRow();
            int col = coordinate.getCol();

            itemTileList.add(board.takeItemTile(row,col));

        }

        notifyAllListeners(new BoardUpdateEvent(board, false));
        return itemTileList;
    }

    /**
     * This method checks if the cell has a side free.
     *
     * @param row the row of the cell
     * @param col the column of the cell
     * @return true if the cell has a side free, false otherwise
     */
    private boolean hasSideFree( int row, int col){
        if (!(Board.hasLeftBoardCell(row,col) && Board.hasUpperBoardCell(row,col) && Board.hasRightBoardCell(row,col) && Board.hasLowerBoardCell(row,col))){
            return  true;
        }
        if (!(board.getLeftBoardCell(row,col).isPlayable() && board.getUpperBoardCell(row,col).isPlayable() && board.getRightBoardCell(row,col).isPlayable() && board.getLowerBoardCell(row,col).isPlayable())){
            return true;
        }
        return !(board.getLeftBoardCell(row, col).getItemTile().getItemTileType() != ItemTileType.EMPTY && board.getUpperBoardCell(row, col).getItemTile().getItemTileType() != ItemTileType.EMPTY && board.getRightBoardCell(row, col).getItemTile().getItemTileType() != ItemTileType.EMPTY && board.getLowerBoardCell(row, col).getItemTile().getItemTileType() != ItemTileType.EMPTY);
    }

    /**
     * This method checks if the coordinates are in line.
     *
     * @param coordinates the coordinates to check
     * @return true if the coordinates are in line, false otherwise
     */
    private boolean isLined(List<Coordinate> coordinates){

        int number = coordinates.size();

        boolean sameRow = coordinates.stream().map(Coordinate::getRow).collect(Collectors.toSet()).size()==1;
        List<Integer> columns = coordinates.stream().map(Coordinate::getCol).sorted().collect(Collectors.toList());
        boolean inRow = (columns.get(columns.size()-1)-columns.get(0) == number-1) && sameRow;

        boolean sameColumn = coordinates.stream().map(Coordinate::getCol).collect(Collectors.toSet()).size()==1;
        List<Integer> rows = coordinates.stream().map(Coordinate::getRow).sorted().collect(Collectors.toList());
        boolean inColumn = (rows.get(rows.size()-1)-rows.get(0) == number-1) && sameColumn;

        return inRow || inColumn;

    }





}
