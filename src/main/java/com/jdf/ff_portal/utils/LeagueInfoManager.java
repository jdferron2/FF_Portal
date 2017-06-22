package com.jdf.ff_portal.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.backend.SbfDraftPickService;
import com.jdf.ff_portal.backend.SbfDraftService;
import com.jdf.ff_portal.backend.SbfLeagueService;
import com.jdf.ff_portal.backend.SbfTeamService;
import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.SbfDraftPick;
import com.jdf.ff_portal.backend.data.SbfDraftRecord;
import com.jdf.ff_portal.backend.data.SbfRank;
import com.jdf.ff_portal.backend.data.SbfTeam;
import com.vaadin.ui.UI;


public class LeagueInfoManager {
	private static LeagueInfoManager INSTANCE = null;
	private static final int	NUMBER_OF_TEAMS = 12;
	private static final int	NUMBER_OF_ROUNDS =15;

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
		return getRound(getCurrentPick());
	}
	
	public synchronized int getRound(int pick){
		int round = 1;
		if (pick%NUMBER_OF_TEAMS ==0){
			round = pick/NUMBER_OF_TEAMS;
		}else{
			round = pick/NUMBER_OF_TEAMS + 1;
		}
		return round;
	}

	public synchronized int getPickInRound(){
		return getPickInRound(getCurrentPick());
	}
	
	public synchronized int getPickInRound(int pick){
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
		Integer ownerId = SbfDraftPickService.getInstance().getPickOwnerId(getCurrentPick());
		if (ownerId != null){
			return SbfTeamService.getInstance().getSbfTeamBySbfId(ownerId);
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
		SbfRank sbfRank = PlayerService.getInstance().getSbfRankById(player.getPlayerId());
		int newRank = sbfRank.getRank()-1;
		if (newRank < 1) return;
		Player existingPlayer;
		if ((existingPlayer=PlayerService.getInstance().getPlayerBySbfRank(newRank)) != null){
			PlayerService.getInstance().getSbfRankById(existingPlayer.getPlayerId()).setRank(newRank+1);
			PlayerService.getInstance().getSbfRankById(existingPlayer.getPlayerId()).setFlagForUpdate(true);
		}else{
			for(int i = newRank; i>0; i--){//player added that wasnt ranked and given 1000 default rank
				existingPlayer=PlayerService.getInstance().getPlayerBySbfRank(i);
				if (existingPlayer != null){
					PlayerService.getInstance().getSbfRankById(existingPlayer.getPlayerId()).setRank(i+1);
					PlayerService.getInstance().getSbfRankById(existingPlayer.getPlayerId()).setFlagForUpdate(true);
					newRank = i;
					break;
				}
			}
		}
		PlayerService.getInstance().getSbfRankById(player.getPlayerId()).setRank(newRank);
		PlayerService.getInstance().getSbfRankById(player.getPlayerId()).setFlagForUpdate(true);
	}

	public synchronized void movePlayerDown(Player player){
		SbfRank sbfRank = PlayerService.getInstance().getSbfRankById(player.getPlayerId());
		int newRank = sbfRank.getRank()+1;
		if (newRank < 1) return;
		Player existingPlayer;
		if ((existingPlayer=PlayerService.getInstance().getPlayerBySbfRank(newRank)) != null){
			PlayerService.getInstance().getSbfRankById(existingPlayer.getPlayerId()).setRank(newRank-1);
			PlayerService.getInstance().getSbfRankById(existingPlayer.getPlayerId()).setFlagForUpdate(true);
		}
		PlayerService.getInstance().getSbfRankById(player.getPlayerId()).setRank(newRank);
		PlayerService.getInstance().getSbfRankById(player.getPlayerId()).setFlagForUpdate(true);
	}
	
	public synchronized List<Integer> getPicksForTeam(int teamId){
		List<SbfDraftPick> extraPicks = SbfDraftPickService.getInstance().getAllSbfDraftPicks();
		SbfTeam team = SbfTeamService.getInstance().getSbfTeamBySbfId(teamId);
		int numTeams = SbfLeagueService.getInstance()
			.getLeagueById((Integer)UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID))
			.getNumTeams();
		
		ArrayList<Integer> picks = new ArrayList<Integer>();
		int currentPick;
		Integer testTeamId;
		for(int i = 1; i<=NUMBER_OF_ROUNDS; i++){
			if (i%2 == 0){//even round
				currentPick = i*numTeams - team.getDraftSlot() + 1;
			}else{
				currentPick = (i-1)*numTeams + team.getDraftSlot();
			}
			testTeamId = SbfDraftPickService.getInstance().getPickOwnerId(currentPick);
			if(testTeamId == null || testTeamId == teamId){
				picks.add(currentPick);	
			}
		}
		for(SbfDraftPick pick : extraPicks){
			if(!picks.contains(pick.getPick()) && pick.getSbfId() == teamId){
				picks.add(pick.getPick());
			}
		}
		picks.sort((p1,p2)->Integer.compare(p1, p2));
		return picks;
	}

	public void addPickToTeam(SbfTeam team, Integer pick) {
		SbfDraftPick sbfDraftPick = SbfDraftPickService.getInstance().getPickBySbfIdPick(team.getSbfId(), pick);
		if(sbfDraftPick != null){
			//pick already traded at some point, update the record to point to the new team
			sbfDraftPick.setSbfId(team.getSbfId());
			SbfDraftPickService.getInstance().updateDraftPick(sbfDraftPick);
		}else{
			//pick was with original owner, create a new record
			sbfDraftPick = new SbfDraftPick(
					(Integer)UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID),
					team.getSbfId(), 
					pick);
			SbfDraftPickService.getInstance().insertDraftPick(sbfDraftPick);
		}
	}
}
