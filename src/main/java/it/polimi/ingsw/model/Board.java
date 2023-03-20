package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.ItemTileType;

import java.util.List;

/**
 * Class representing the board of the game.
 */
public class Board {

    /**
     * Data structure to represent the board.
     */
    private final BoardCell[][] boardGrid;

    /**
     * Index of the cell of the board [row][column].
     * [0,0] [0,1] [0,2] [0,3] [0,4] [0,5] [0,6] [0,7] [0,8]
     * [1,0] [1,1] [1,2] [1,3] [1,4] [1,5] [1,6] [1,7] [1,8]
     * [2,0] [2,1] [2,2] [2,3] [2,4] [2,5] [2,6] [2,7] [2,8]
     * [3,0] [3,1] [3,2] [3,3] [3,4] [3,5] [3,6] [3,7] [3,8]
     * [4,0] [4,1] [4,2] [4,3] [4,4] [4,5] [4,6] [4,7] [4,8]
     * [5,0] [5,1] [5,2] [5,3] [5,4] [5,5] [5,6] [5,7] [5,8]
     * [6,0] [6,1] [6,2] [6,3] [6,4] [6,5] [6,6] [6,7] [6,8]
     * [7,0] [7,1] [7,2] [7,3] [7,4] [7,5] [7,6] [7,7] [7,8]
     * [8,0] [8,1] [8,2] [8,3] [8,4] [8,5] [8,6] [8,7] [8,8]
     */

    /**
     * Number of players required to use the cell.
     * Empty cells are considered null.
     * [ ] [ ] [ ] [3] [4] [ ] [ ] [ ] [ ]
     * [ ] [ ] [ ] [2] [2] [4] [ ] [ ] [ ]
     * [ ] [ ] [3] [2] [2] [2] [3] [ ] [ ]
     * [ ] [4] [2] [2] [2] [2] [2] [2] [3]
     * [4] [2] [2] [2] [2] [2] [2] [2] [4]
     * [3] [2] [2] [2] [2] [2] [2] [4] [ ]
     * [ ] [ ] [3] [2] [2] [2] [3] [ ] [ ]
     * [ ] [ ] [ ] [4] [2] [2] [ ] [ ] [ ]
     * [ ] [ ] [ ] [ ] [4] [3] [ ] [ ] [ ]
     */

    /**
     * Constructor Board, initializing the cells in the grid, based on the number of players.
     *
     * @param numPlayers number of player in the game
     */
    public Board(int numPlayers) {
        if(numPlayers < 2 || numPlayers > 4) throw new IllegalArgumentException("Invalid number of players");
        boolean createCell3 = (numPlayers == 3 || numPlayers == 4);
        boolean createCell4 = (numPlayers == 4);
        this.boardGrid = new BoardCell[][] {
                { null, null, null, (createCell3 ? new BoardCell(0, 3) : null), (createCell4 ? new BoardCell(0, 4) : null), null, null, null, null },
                { null, null, null, new BoardCell(1, 3), new BoardCell(1, 4), (createCell4 ? new BoardCell(1, 5) : null), null, null, null },
                { null, null, (createCell3 ? new BoardCell(2, 2) : null), new BoardCell(2, 3), new BoardCell(2, 4), new BoardCell(2, 5), (createCell3 ? new BoardCell(2, 6) : null), null, null },
                { null, (createCell4 ? new BoardCell(3, 1) : null), new BoardCell(3, 2), new BoardCell(3, 3), new BoardCell(3, 4), new BoardCell(3, 5), new BoardCell(3, 6), new BoardCell(3, 7), (createCell3 ? new BoardCell(3, 8) : null) },
                { (createCell4 ? new BoardCell(4, 0) : null), new BoardCell(4, 1), new BoardCell(4, 2), new BoardCell(4, 3), new BoardCell(4, 4), new BoardCell(4, 5), new BoardCell(4, 6), new BoardCell(4, 7), (createCell4 ? new BoardCell(4, 8) : null) },
                { (createCell3 ? new BoardCell(5, 0) : null), new BoardCell(5, 1), new BoardCell(5, 2), new BoardCell(5, 3), new BoardCell(5, 4), new BoardCell(5, 5), new BoardCell(5, 6), (createCell4 ? new BoardCell(5, 7) : null), null },
                { null, null, (createCell3 ? new BoardCell(6, 2) : null), new BoardCell(6, 3), new BoardCell(6, 4), new BoardCell(6, 5), (createCell3 ? new BoardCell(6, 6) : null), null, null },
                { null, null, null, (createCell4 ? new BoardCell(7, 3) : null), new BoardCell(7, 4), new BoardCell(7, 5), null, null, null },
                { null, null, null, null, (createCell4 ? new BoardCell(8, 4) : null), (createCell3 ? new BoardCell(8, 5) : null), null, null, null },
        };
    }


    /**
     * Get the grid of the board.
     *
     * @return grid of the board
     */
    public BoardCell[][] getBoardGrid() {
        return this.boardGrid;
    }

    /**
     * Set the empty and not null cells with the item tiles provided.
     * Checks if itemTileList size is bigger than the available cells.
     *
     * @param itemTileList list of item tile to put in the board
     */
    public void setItemTiles(List<ItemTile> itemTileList) {
        int emptyCells = 0;
        for (BoardCell[] boardCells : boardGrid) {
            for (BoardCell boardCell : boardCells) {
                if (boardCell != null && boardCell.getItemTile().getItemTileType() == ItemTileType.EMPTY) {
                    emptyCells++;
                }
            }
        }

        if (itemTileList.size() > emptyCells) {
            throw new IllegalArgumentException("Not enough empty cells on the board to place all item tiles.");
        }

        for (BoardCell[] boardCells : boardGrid) {
            for (BoardCell boardCell : boardCells) {
                if (boardCell != null && boardCell.getItemTile().getItemTileType() == ItemTileType.EMPTY) {
                    if (!itemTileList.isEmpty()) {
                        ItemTile itemTile = itemTileList.remove(0);
                        boardCell.setItemTile(itemTile);
                    } else {
                        return;
                    }
                }
            }
        }
    }

}
