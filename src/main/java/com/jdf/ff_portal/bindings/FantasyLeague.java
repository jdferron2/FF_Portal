package com.jdf.ff_portal.bindings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="League")
public class FantasyLeague {
	private List<FantasyTeam> teams;
	private int currentPick=1;
	//private int pickInRound=1;
	//private int whosPick=1;
	//private Players players;
	//private HashMap<String, Player> playerMap;
	


	public FantasyLeague(){}
	
	public FantasyLeague(List<FantasyTeam> teams){
		super();
		this.teams = teams;
	}
	
	@XmlElement(name="Team")
    public List<FantasyTeam> getTeams() {
        if (teams == null) {
        	teams = new ArrayList<FantasyTeam>();
        }
        return this.teams;
    }
	
	public void setTeams( List<FantasyTeam> teams){
		this.teams = teams;
	}	
	
	@XmlAttribute  (name="currentPick")
	public int getCurrentPick() {
		return currentPick;
	}
	
	public void setCurrentPick(int pick) {
		currentPick = pick;
	}
	
//	@XmlAttribute  (name="pickInRound")
//	public int getPickInRound() {
//		return pickInRound;
//	}
//
//	public void setPickInRound(int pickInRound) {
//		this.pickInRound = pickInRound;
//	}
//
//	@XmlAttribute  (name="whosPick")
//	public int getWhosPick() {
//		return whosPick;
//	}
//
//	public void setWhosPick(int whosPick) {
//		this.whosPick = whosPick;
//	}
	
//	@XmlElement(name="players")
//    public Players getPlayers() {
//        return this.players;
//    }
//	
//	public void setPlayers(Players players){
//		this.players = players;
//	}
}
