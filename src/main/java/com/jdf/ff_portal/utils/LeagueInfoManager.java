package com.jdf.ff_portal.utils;

import java.sql.Timestamp;
import java.util.HashMap;

import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.backend.SbfDraftService;
import com.jdf.ff_portal.backend.SbfTeamService;
import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.SbfDraftRecord;
import com.jdf.ff_portal.backend.data.SbfTeam;
import com.vaadin.ui.UI;


public class LeagueInfoManager {
	private static LeagueInfoManager INSTANCE = null;
	private static final int	NUMBER_OF_TEAMS = 12;

	//private HashMap<Integer, Player> lookupPlayerByDraftSlot;
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
//		RestAPIUtils restUtils = RestAPIUtils.getInstance();
//		league = restUtils.unMarshalFantasyTeams(LEAGUE_FILE_NAME);
//		players = restUtils.unMarshalPlayerList(PLAYERS_FILE_NAME);
//
//		for(FantasyTeam currentTeam : league.getTeams()){
//			for(Player currentDraftedPlayer : currentTeam.getPlayers()){
//				Player syncPlayer = players.getPlayerByRank(currentDraftedPlayer.getCurrentRank());
//				currentDraftedPlayer = syncPlayer;
//			}
//		}
	}

//	public void saveLeagueInfo(){
////		RestAPIUtils.getInstance().marshalFantasyTeams(league, LEAGUE_FILE_NAME);
////		RestAPIUtils.getInstance().marshalPlayerList(players, PLAYERS_FILE_NAME);
//	}
//
//	public FantasyLeague getLeague(){
//		return league;
//	}
//
//	public Players getPlayers(){
//		return players;
//	}

	public synchronized int getCurrentPick(){
		if (SbfDraftService.getInstance().getAllSbfDraftRecords().isEmpty()) return 1;
		return SbfDraftService.getInstance().getAllSbfDraftRecords().stream().max(
				(s1,s2)->Integer.compare(s1.getSlotDrafted(), s2.getSlotDrafted())
				).get().getSlotDrafted() + 1;
	}
	public synchronized int getRound(){
		int pick = getCurrentPick();
		int round = 1;
		if (pick%NUMBER_OF_TEAMS ==0){
			round = pick/NUMBER_OF_TEAMS;
		}else{
			round = pick/NUMBER_OF_TEAMS + 1;
		}
		return round;
	}

	public synchronized int getPickInRound(){
		int pick = getCurrentPick();
		if (pick <= NUMBER_OF_TEAMS) return pick;
		if (pick % NUMBER_OF_TEAMS == 0) return NUMBER_OF_TEAMS;
		return (pick % NUMBER_OF_TEAMS) ;
		//return 0;
	}

	public synchronized SbfTeam getTeamOnTheClock(){
		int round = getRound();
		int startingDraftSlot;
		if (round % 2 == 0){ //even Round
			startingDraftSlot = (NUMBER_OF_TEAMS+1) - getPickInRound();
		}else{
			startingDraftSlot = getPickInRound();
		}
		for (SbfTeam currentTeam : SbfTeamService.getInstance().getAllSbfTeams()){
			if (currentTeam.getDraftSlot() == startingDraftSlot) return currentTeam;
		}
		return null;
	}

	public synchronized void undoLastDraftPick(){
		if (SbfDraftService.getInstance().getAllSbfDraftRecords().isEmpty()) return;
		SbfDraftRecord lastDraftedPlayer = 
				SbfDraftService.getInstance().getAllSbfDraftRecords().stream().max(
						(s1,s2)->Integer.compare(s1.getSlotDrafted(), s2.getSlotDrafted())
						).get();
		SbfDraftService.getInstance().deleteDraftRecord(lastDraftedPlayer);
	}

	public synchronized void draftPlayer(Player player){
		if (SbfDraftService.getInstance().getSbfDraftRecordByPlayerId(player.getPlayerId()) == null){
			SbfDraftRecord draftRecord = new SbfDraftRecord(
					(Integer) UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID),
					getTeamOnTheClock().getSbfId(),player.getPlayerId(),
					getCurrentPick(),new Timestamp(System.currentTimeMillis()));
			SbfDraftService.getInstance().insertDraftRecord(draftRecord);
		}
	}
	
	public synchronized void movePlayerUp(Player player){
		int newRank = player.getSbfRank().getRank() - 1;
		if (newRank < 1) return;
		Player existingPlayer;
		if ((existingPlayer=PlayerService.getInstance().getPlayerBySbfRank(player.getSbfRank().getRank()-1)) != null){
			existingPlayer.getSbfRank().setRank(newRank+1);
			existingPlayer.getSbfRank().setFlagForUpdate(true);
		}
		player.getSbfRank().setRank(newRank);
		player.getSbfRank().setFlagForUpdate(true);
	}

	public synchronized void movePlayerDown(Player player){
		int newRank = player.getSbfRank().getRank() + 1;
		if (newRank < 1) return;
		Player existingPlayer;
		if ((existingPlayer=PlayerService.getInstance().getPlayerBySbfRank(player.getSbfRank().getRank()+1)) != null){
			existingPlayer.getSbfRank().setRank(newRank-1);
			player.getSbfRank().setRank(newRank);
			existingPlayer.getSbfRank().setFlagForUpdate(true);
			player.getSbfRank().setFlagForUpdate(true);
		}
	}
}
