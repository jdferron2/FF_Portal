package com.jdf.ff_portal.bindings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "Players", propOrder = { "players" })

@XmlRootElement(name="Players")
public class Players {
	private List<Player> players;
	private HashMap<String, Player> playerMap;
	
	public Players(){}
	
	public Players(List<Player> players){
		super();
		this.players = players;
	}
	
	@XmlElement(name="Player")
    public List<Player> getPlayers() {
        if (players == null) {
        	players = new ArrayList<Player>();
        }
        return this.players;
    }
	
	public void setPlayers( List<Player> players){
		this.players = players;
	}
	
	public void movePlayerUp(Player player){
		int newRank=player.getCurrentRank()-1;
		Player existingPlayer = getPlayerByRank(newRank);
		if (existingPlayer == null){
			return;
		}
		existingPlayer.setCurrentRank(newRank+1);
		player.setCurrentRank(newRank);
	}
	
	public void movePlayerDown(Player player){
		int newRank=player.getCurrentRank()+1;
		Player existingPlayer = getPlayerByRank(newRank);
		if (existingPlayer == null){
			return;
		}
		existingPlayer.setCurrentRank(newRank-1);
		player.setCurrentRank(newRank);
	}

	/*public void updatePlayerRanks(SortableTable cheatSheetTable) {
		for (Object itemId : cheatSheetTable.getItemIds().toArray()){
			Item currentItem = cheatSheetTable.getItem(itemId);
			String name;
			String position;
			String team;
			String key;
			int currentRank;
			Player currentPlayer;
			
			name = (String) currentItem.getItemProperty("Name").getValue();
			position = (String) currentItem.getItemProperty("Position").getValue();
			team = (String) currentItem.getItemProperty("Team").getValue();
			key = name + team + position;
			
			currentPlayer = getPlayer(key);
			currentRank = (int) currentItem.getItemProperty("Current Rank").getValue();
			if (currentPlayer != null) currentPlayer.setJdfRank(currentRank);
		}		
	}*/
	
	public Player getPlayer(String key){
		if (playerMap == null){
			playerMap = new HashMap<String, Player>();
			for (Player currentPlayer : getPlayers()){
				String playerName = currentPlayer.getDisplayName();
				String team = currentPlayer.getTeam();
				String position = currentPlayer.getPosition();
				//int number = currentPlayer.getJersey();
				String mapKey = playerName + team + position ;
				if (playerMap.get(mapKey) == null) playerMap.put(mapKey, currentPlayer);
			}
		}
		return playerMap.get(key);
		
	}
	
	public Player getPlayerByRank(int rank){
		for (Player currentPlayer : getPlayers()){
			if (currentPlayer.getCurrentRank() == rank){
				return currentPlayer;
			}
		}
		return null;
	}
	
	public void resetStartingRanks(){
		for (Player currentPlayer : getPlayers()){
			currentPlayer.setStartingRank(currentPlayer.getCurrentRank());
		}
	}
	

}
