package com.jdf.ff_portal.views;

import com.jdf.ff_portal.UI.elements.ConfirmButton;
import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.backend.SbfTeamService;
import com.jdf.ff_portal.backend.data.DraftRank;
import com.jdf.ff_portal.backend.data.DraftRankings;
import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.Players;
import com.jdf.ff_portal.backend.data.SbfRank;
import com.jdf.ff_portal.backend.data.SbfTeam;
import com.jdf.ff_portal.utils.LeagueInfoManager;
import com.jdf.ff_portal.utils.RestAPIUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class AdminView extends VerticalLayout implements View  {
	public static final String NAME = "Admin Functions";
	private ComboBox<Integer> team1PickSelector = createTradeBox(1);
	private ComboBox<Integer> team2PickSelector = createTradeBox(2);
	private ComboBox<SbfTeam> team1Selector = this.createTeamSelectorCB("Team 1", team1PickSelector);
	private ComboBox<SbfTeam> team2Selector = this.createTeamSelectorCB("Team 2",team2PickSelector);
	private Button processTrade = new Button("Process Trade");
	private GridLayout tradeLayout = new GridLayout(3,3);
	private boolean viewBuilt = false;
	@Override
	public void enter(ViewChangeEvent event) {
		if(!viewBuilt){
			buildView();
			viewBuilt=true;
		}		
	}

	private void buildView(){
		setSpacing(true);
		setMargin(true);
		Button resetPlayerList = new Button("Reset Players Table");
		resetPlayerList.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				//delete current player table
				PlayerService.getInstance().deleteAllPlayers();

				//re-load active players from api
				Players players = RestAPIUtils.getInstance().invokeQueryPlayers();
				for(Player player : players.getPlayers()){
					if (player.getActive() == 1){
						PlayerService.getInstance().insertPlayer(player);
					}			
				}

				//set pro ranks for all players
				DraftRankings ranks = RestAPIUtils.getInstance().invokeQueryRanks();
				for(DraftRank rank: ranks.getDraftRanks()){
					Player player = PlayerService.getInstance().getPlayerById(rank.getPlayerId());
					if (player != null){
						player.setProRank(rank.getProRank());
						PlayerService.getInstance().updatePlayer(player);
					}
				}

				
				int newPlayerRank = 1000;
				for (Player p : PlayerService.getInstance().getAllPlayers()){
					if (p.getProRank() == 0){
						p.setProRank(PlayerService.getInstance().getMaxProRank()+1);
						PlayerService.getInstance().updatePlayer(p);
					}
					if (PlayerService.getInstance().getSbfRankById(p.getPlayerId()) == null){
						SbfRank rank = new SbfRank(1, p.getPlayerId(), newPlayerRank++);
						PlayerService.getInstance().insertSbfRank(rank);
					}
				}

				Notification.show("Player list update successfully!");;		
			}

		});

		ConfirmButton resetMyRanks = new ConfirmButton("Reset My Ranks");
		resetMyRanks.setConfirmationText("This will reset custom ranks to the default pro ranks.");
		resetMyRanks.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				//delete current ranks
				PlayerService.getInstance().deleteAllSbfRanks(1);

				//add default ranks based on pro ranks
				for(Player player : PlayerService.getInstance().getAllPlayers()){
					SbfRank rank = new SbfRank(1, player.getPlayerId(), player.getProRank());
					PlayerService.getInstance().insertSbfRank(rank);
				}

				Notification.show("Ranks updated successfully!");
			}

		});

		processTrade.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				if(team2PickSelector.getValue()!= null){
					LeagueInfoManager.getInstance().addPickToTeam(team1Selector.getValue(), team2PickSelector.getValue());
				}
				if(team1PickSelector.getValue()!=null){
					LeagueInfoManager.getInstance().addPickToTeam(team2Selector.getValue(), team1PickSelector.getValue());
				}
				team1PickSelector.clear();
				team2PickSelector.clear();
				team1Selector.clear();
				team2Selector.clear();
				Notification.show("Trade Processed successfully!");
			}

		});
		processTrade.setVisible(false);
		tradeLayout.addComponent(team1Selector,0,0);
		tradeLayout.addComponent(team2Selector,1,0);
		tradeLayout.addComponent(processTrade, 1,2);
		team1PickSelector.setVisible(false);
		team2PickSelector.setVisible(false);
		tradeLayout.addComponent(team1PickSelector,0,1);
		tradeLayout.addComponent(team2PickSelector,1,1);
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponents(resetPlayerList, resetMyRanks);
		addComponents(buttonLayout, new Label("PROCESS TRADE"), tradeLayout);
	}

	public ComboBox<Integer> createTradeBox(int teamId){
		ComboBox<Integer> teamPicksCB = new ComboBox<Integer>("Picks");
		teamPicksCB.setItemCaptionGenerator(
				i->"Pick: " + Integer.toString(i) + " (r" + 
						Integer.toString(LeagueInfoManager.getInstance().getRound(i))+
						" p" + LeagueInfoManager.getInstance().getPickInRound(i) + ")");
		teamPicksCB.setItems(LeagueInfoManager.getInstance().getPicksForTeam(teamId));
		teamPicksCB.addValueChangeListener(event-> {
			setSubmitVisible();
		});
		return teamPicksCB;
	}

	public ComboBox<SbfTeam> createTeamSelectorCB(String name, ComboBox<Integer> connectedBox){
		ComboBox<SbfTeam> teamCB = new ComboBox<SbfTeam>(name);
		teamCB.setItems(SbfTeamService.getInstance().getAllSbfTeams());
		teamCB.setItemCaptionGenerator(SbfTeam::getOwnerName);
		teamCB.addValueChangeListener(event-> {
			if(event.getValue()!= null){
				connectedBox.clear();
				connectedBox.setItems(LeagueInfoManager.getInstance().getPicksForTeam(event.getValue().getSbfId()));
				connectedBox.setVisible(true);
				setSubmitVisible();
			}else{
				connectedBox.clear();
				connectedBox.setVisible(false);
				setSubmitVisible();
			}
		});

		return teamCB;
	}

	public void setSubmitVisible(){
		if ((team1PickSelector.getValue() != null || team2PickSelector.getValue() != null) &&
				(team1Selector.getValue() !=null && team2Selector.getValue() !=null)){
			processTrade.setVisible(true);
		}else{
			processTrade.setVisible(false);
		}
	}

}
