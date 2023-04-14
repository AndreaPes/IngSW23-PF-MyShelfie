package it.polimi.ingsw.model.gameMechanics;

import it.polimi.ingsw.model.gameEntity.Player;
import it.polimi.ingsw.model.gameEntity.common_cards.CommonGoalCard;
import it.polimi.ingsw.model.gameEntity.enums.ItemTileType;
import it.polimi.ingsw.model.gameEntity.personal_cards.Goal;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class that manages the points of the game.
 */
public class PointsManager {
    /**
     * The player of the game.
     */
    private final Player player;
    /**
     * The number of players of the game.
     */
    private final Integer numOfPlayers;
    /**
     * The list of the common goal cards of the game.
     */
    private final List<CommonGoalCard> commonGoalCardList;
    /**
     * The username of the first player that has completed the library.
     */
    private final Optional<String> firstFullLibraryUsername;

    /**
     * Constructor of the class.
     *
     * @param currentPlayer the player of the game
     * @param numOfPlayers the number of players of the game
     * @param commonGoalCardList the list of the common goal cards of the game
     * @param firstFullLibraryUsername the username of the first player that has completed the library
     */
    public PointsManager(Player currentPlayer, Integer numOfPlayers, List<CommonGoalCard> commonGoalCardList, Optional<String> firstFullLibraryUsername) {
        this.player = currentPlayer;
        this.numOfPlayers = numOfPlayers;
        this.commonGoalCardList = commonGoalCardList;
        this.firstFullLibraryUsername = firstFullLibraryUsername;
    }

    /**
     * Get the total points of the player, related to the common cards.
     *
     * @return total points achieved related to Common Cards
     */
    public int commonPoints() {
        int commonPoints =0;

        List<Integer> pointsSource = new ArrayList<>();
        switch (numOfPlayers){
            case 2:
                pointsSource = Arrays.asList(8,4);
                break;
            case 3:
                pointsSource = Arrays.asList(8,6,4);
                break;
            case 4:
                pointsSource = Arrays.asList(8,6,4,2);
                break;
        }

        for (CommonGoalCard commonGoalCard : commonGoalCardList){
                if(!commonGoalCard.isSmartPlayer(player) && commonGoalCard.checkRules(player.getLibrary())){
                    commonGoalCard.addSmartPlayer(player);
                }
                if(commonGoalCard.isSmartPlayer(player)){
                    commonPoints+= commonGoalCard.getPoint(pointsSource, player);
                }
        }
        return commonPoints;
    }


    /**
     * Get the total points of the player, related to the personal card.
     *
     * @return total points achieved related to Common Cards
     */
    public int personalPoints(){

        List<Goal> goals = player.getPersonalGoalCard().getGoals();

        int counter =0;
        for (Goal goal : goals) {
            int row = goal.getRow();
            int col = goal.getColumn();

            if (player.getLibrary().getItemTile(row, col).getItemTileType() == goal.getItemTileType()) {
                counter++;
            }
        }
            switch (counter){
                case 1:  return 1;
                case 2:  return 2;
                case 3:  return 4;
                case 4:  return 6;
                case 5:  return 9;
                case 6:  return 12;
            }
        return 0;
    }

    /**
     * Get the total points of the player, related to the adjacent items.
     *
     * @return total points achieved related to adjacent Groups
     */
    public int adjacentPoints(){
        Predicate<Pair<ItemTileType,Integer>> filterGroup =
                (group) -> (group.getValue0()!=ItemTileType.EMPTY && group.getValue1()>2);
        Function<Pair<ItemTileType,Integer>,Integer> calculateCommonPoints =
                (group)->{
                    int numberOfTile = group.getValue1();
                    if (numberOfTile==3) return 2;
                    else if (numberOfTile==4) return 3;
                    else if (numberOfTile==5) return 5;
                    else return 8;
                };
        LibraryManager libraryManager = new LibraryManager(player.getLibrary());
        List<Pair<ItemTileType, Integer>> listGroupsAdjacentTiles = libraryManager.getListGroupsAdjacentTiles();

         /* To help the debugging
        System.out.println(listGroupsAdjacentTiles.stream().filter(filterGroup).collect(Collectors.toList()));
        System.out.println(listGroupsAdjacentTiles.stream().filter(filterGroup).map(calculateCommonPoints).collect(Collectors.toList()));
         */
        return listGroupsAdjacentTiles.stream().filter(filterGroup).map(calculateCommonPoints).reduce(0, Integer::sum);
    }

    /**
     * Get the final point of the player, if he is the first to complete the library.
     *
     * @return the additional point to the first player to the first finisher
     */
    public int finalPoint(){
        if (firstFullLibraryUsername.isPresent()){
            if (firstFullLibraryUsername.get().equals(player.getUsername())) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * This method updates the Total Points adding Points related to: Common and Personal Card, adjacent Items, (optional) final point
     */
    public void updateTotalPoints(){
        player.setTotPoints(commonPoints()+personalPoints()+adjacentPoints()+finalPoint());
    }
}
