package it.polimi.ingsw.model.common_cards;

import it.polimi.ingsw.model.data.ItemTile;
import it.polimi.ingsw.model.data.Library;
import it.polimi.ingsw.model.data.enums.ItemTileType;
import it.polimi.ingsw.model.logic.common_cards.CommonCard8;
import it.polimi.ingsw.model.logic.common_cards.CommonGoalCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonCard8Test {

    CommonGoalCard card8;
    Library library;
    ItemTile[][] libraryGrid;

    @BeforeEach
    void setUp() {
        List<Integer> points = new ArrayList<>();
        points.add(8);
        points.add(6);
        card8 = new CommonCard8(8, points);
        library = new Library();
        libraryGrid = library.getGrid();
    }

    @Test
    @DisplayName("Test check rules for card 8")
    void checkRules() {
        assertFalse(card8.checkRules(libraryGrid));

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                library.setItemTile(row, col, new ItemTile(ItemTileType.CAT));
            }
        }
        assertTrue(card8.checkRules(libraryGrid));

        for (int row = 0; row < 4; row++) {
            library.setItemTile(row, 1, new ItemTile(ItemTileType.PLANT));
        }
        assertTrue(card8.checkRules(libraryGrid));

        for (int row = 0; row < 4; row++) {
            library.setItemTile(row, 2, new ItemTile(ItemTileType.TROPHY));
        }
        assertTrue(card8.checkRules(libraryGrid));

        for (int row = 0; row < 4; row++) {
            library.setItemTile(row, 3, new ItemTile(ItemTileType.GAME));
        }
        assertFalse(card8.checkRules(libraryGrid));
    }

    @Test
    @DisplayName("Test check rules for card 8 with less than 5 tiles")
    void testCheckRulesLessThan5Tiles(){
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                library.setItemTile(row, col, new ItemTile(ItemTileType.CAT));
            }
        }
        assertFalse(card8.checkRules(libraryGrid));
    }
}