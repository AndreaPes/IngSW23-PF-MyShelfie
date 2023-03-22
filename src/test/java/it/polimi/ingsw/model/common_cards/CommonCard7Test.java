package it.polimi.ingsw.model.common_cards;

import it.polimi.ingsw.model.GameData;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Library;
import it.polimi.ingsw.model.enums.ItemTileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CommonCard7Test {

    CommonGoalCard card7;
    GameData gameData;
    List<String> players;
    Library libraryP1;
    ItemTile[][] gridP1;
    Random random;

    @BeforeEach
    void setUp() {
        List<Integer> points = new ArrayList<>();
        points.add(8);
        points.add(6);
        card7 = new CommonCard7(7, points);
        players = new ArrayList<>();
        players.add("Pippo");
        players.add("Pluto");
        gameData = new GameData(players, 2);
        libraryP1 = gameData.getPlayerDashboards().get(players.get(0)).getLibrary();
        gridP1 = gameData.getPlayerDashboards().get(players.get(0)).getLibrary().getGrid();
        random = new Random();
    }

    @Test
    @DisplayName("Test check rules for card 7 from left")
    void checkRulesFromLeft() {
        assertFalse(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(0, 0, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(1, 1, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(2, 2, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(3, 3, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(4, 4, new ItemTile(ItemTileType.CAT));
        assertTrue(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(4, 4, new ItemTile(ItemTileType.PLANT));
        assertFalse(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(1, 0, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(2, 1, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(3, 2, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(4, 3, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(5, 4, new ItemTile(ItemTileType.TROPHY));
        assertTrue(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(5, 4, new ItemTile(ItemTileType.PLANT));
        assertFalse(card7.checkRules(gameData, players.get(0)));
    }

    @Test
    @DisplayName("Test check rules for card 7 from right")
    void checkRulesFromRight() {
        assertFalse(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(0, 4, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(1, 3, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(2, 2, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(3, 1, new ItemTile(ItemTileType.CAT));
        libraryP1.setItemTile(4, 0, new ItemTile(ItemTileType.CAT));
        assertTrue(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(4, 0, new ItemTile(ItemTileType.PLANT));
        assertFalse(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(1, 4, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(2, 3, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(3, 2, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(4, 1, new ItemTile(ItemTileType.TROPHY));
        libraryP1.setItemTile(5, 0, new ItemTile(ItemTileType.TROPHY));
        assertTrue(card7.checkRules(gameData, players.get(0)));
        libraryP1.setItemTile(5, 0, new ItemTile(ItemTileType.PLANT));
        assertFalse(card7.checkRules(gameData, players.get(0)));
    }
}