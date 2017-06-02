package com.jdf.ff_portal.utils;

import java.util.HashMap;

import com.jdf.ff_portal.bindings.FantasyLeague;
import com.jdf.ff_portal.bindings.FantasyTeam;
import com.jdf.ff_portal.bindings.Player;
import com.jdf.ff_portal.bindings.Players;

public class LeagueInfoManager {
	private static LeagueInfoManager INSTANCE = null;
	private static final String LEAGUE_FILE_NAME = "leagueInfo.xml";
	private static final String PLAYERS_FILE_NAME = "players.xml";
	private static final int	NUMBER_OF_TEAMS = 12;

	//private HashMap<Integer, Player> lookupPlayerByDraftSlot;
	private FantasyLeague league;
	private Players players;
	public static LeagueInfoManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LeagueInfoManager();

		}
		return INSTANCE;
	}

	private LeagueInfoManager(){
		initialize();
	}

	private void initialize(){
		RestAPIUtils restUtils = RestAPIUtils.getInstance();
		league = restUtils.unMarshalFantasyTeams(LEAGUE_FILE_NAME);
		players = restUtils.unMarshalPlayerList(PLAYERS_FILE_NAME);

		for(FantasyTeam currentTeam : league.getTeams()){
			for(Player currentDraftedPlayer : currentTeam.getPlayers()){
				Player syncPlayer = players.getPlayerByRank(currentDraftedPlayer.getCurrentRank());
				currentDraftedPlayer = syncPlayer;
			}
		}
	}

	public void saveLeagueInfo(){
		RestAPIUtils.getInstance().marshalFantasyTeams(league, LEAGUE_FILE_NAME);
		RestAPIUtils.getInstance().marshalPlayerList(players, PLAYERS_FILE_NAME);
	}

	public FantasyLeague getLeague(){
		return league;
	}

	public Players getPlayers(){
		return players;
	}

	public int getRoundFromPick(int pick){
		int round = 1;
		if (pick%NUMBER_OF_TEAMS ==0){
			round = pick/NUMBER_OF_TEAMS;
		}else{
			round = pick/NUMBER_OF_TEAMS + 1;
		}
		return round;
	}

	public int getPickInRoundFromPick(int pick){
		if (pick <= NUMBER_OF_TEAMS) return pick;
		if (pick % NUMBER_OF_TEAMS == 0) return NUMBER_OF_TEAMS;
		return (pick % NUMBER_OF_TEAMS) ;
		//return 0;
	}

	public FantasyTeam getTeamOnTheClockFromPick(int pick){
		int round = getRoundFromPick(pick);
		int startingDraftSlot;
		if (round % 2 == 0){ //even Round
			startingDraftSlot = (NUMBER_OF_TEAMS+1) - getPickInRoundFromPick(pick);
		}else{
			startingDraftSlot = getPickInRoundFromPick(pick);
		}
		for (FantasyTeam currentTeam : league.getTeams()){
			if (currentTeam.getDraftPosition() == startingDraftSlot) return currentTeam;
		}
		return null;
	}

	public synchronized void undoLastDraftPick(){
		int currentPick = league.getCurrentPick();
		if(currentPick == 1) return;
		int pickToBeUndone = currentPick-1;
		Player lastDraftedPlayer = lookupPlayerByDraftSlot(pickToBeUndone);
		if (lastDraftedPlayer == null) return;
		lastDraftedPlayer.setOwner(null);
		lastDraftedPlayer.setDraftedSlot(0);
		league.setCurrentPick(pickToBeUndone);
		//league.setPickInRound(getPickInRoundFromPick(pickToBeUndone));
		//league.setWhosPick(getTeamOnTheClockFromPick(pickToBeUndone).getDraftPosition());
	}

	public Player lookupPlayerByDraftSlot(int draftSlot){
		//if (lookupPlayerByDraftSlot == null){
		//	lookupPlayerByDraftSlot = new HashMap<Integer,Player>();
		for(Player p : players.getPlayers()){
			//lookupPlayerByDraftSlot.put(p.getDraftedSlot(), p);
			if (p.getDraftedSlot() == draftSlot) return p;
		}

		return null;
		//}
		//return lookupPlayerByDraftSlot.get(draftSlot);
	}

	public synchronized void draftPlayer(Player player){
		int pick = league.getCurrentPick();
		FantasyTeam draftedByTeam = getTeamOnTheClockFromPick(pick);
		player.setOwner(draftedByTeam.getOwner());
		draftedByTeam.addPlayer(player);
		player.setDraftedSlot(pick);
		pick++;		
		league.setCurrentPick(pick);
	}
}
