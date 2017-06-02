package com.jdf.ff_portal.views;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jdf.ff_portal.bindings.Player;
import com.jdf.ff_portal.bindings.Players;
import com.jdf.ff_portal.utils.LeagueInfoManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class CheatSheetView extends HorizontalLayout implements View {
	public CheatSheetView(){
		setSizeFull();
		final VerticalLayout layout = new VerticalLayout();
		final HorizontalLayout upDownLayout = new HorizontalLayout();

		layout.setSizeFull();

		Players playerList = LeagueInfoManager.getInstance().getPlayers();

		List<Player> players = playerList.getPlayers();

		Grid<Player> grid = new Grid<>();
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);

		grid.addSelectionListener(selectEvent -> {
			Set<Player> selected = selectEvent.getAllSelectedItems();
			Notification.show(selected.size() + " items selected");
		});

		grid.setItems(players);
		Column<Player, ?> currentRankCol = grid.addColumn(Player::getCurrentRank).setCaption("Current Rank");
		grid.addColumn(Player::getStartingRank).setCaption("Starting Rank");
		grid.addColumn(Player::getDisplayName).setCaption("Name");
		grid.addColumn(Player::getTeam).setCaption("Team");
		grid.addColumn(Player::getPosition).setCaption("Position");

		Button up = new Button("Up");
		up.addClickListener(new Button.ClickListener()
		{ @Override public void buttonClick(Button.ClickEvent clickEvent)
		{
			final List<Player> PlayerList =  new ArrayList<Player>(grid.getSelectedItems());
			if (PlayerList.size() == 0) return;
			Player selectedPlayer = PlayerList.get(0);
			playerList.movePlayerUp(selectedPlayer);		
			grid.sort(currentRankCol, SortDirection.ASCENDING);		
			grid.scrollTo(selectedPlayer.getCurrentRank()-1);
		} });

		Button down = new Button("Down");
		down.addClickListener(new Button.ClickListener()
		{ @Override public void buttonClick(Button.ClickEvent clickEvent)
		{
			final List<Player> PlayerList =  new ArrayList<Player>(grid.getSelectedItems());
			if (PlayerList.size() == 0) return;
			Player selectedPlayer = PlayerList.get(0);
			playerList.movePlayerDown(selectedPlayer);		
			grid.sort(currentRankCol, SortDirection.ASCENDING);		
			grid.scrollTo(selectedPlayer.getCurrentRank()-1);
		} });

		Button updateJdfRanks = new Button ("Update JDF Ranks");
		updateJdfRanks.addClickListener(new Button.ClickListener() {
			@Override public void buttonClick(Button.ClickEvent clickEvent){
				playerList.resetStartingRanks();
				LeagueInfoManager.getInstance().saveLeagueInfo();
				grid.getDataProvider().refreshAll();
			}
		});
		grid.setSizeFull();
		//ListDataProvider<Player> dataProvider = (ListDataProvider<Player>) grid.getDataProvider();
		// dataProvider.setFilter(Player::getActive, a -> a==1);

		int rank = 1;
		List<Player> nonActivePlayers = new ArrayList<Player>();
		for (Player currentPlayer : players){
			if (currentPlayer.getActive()==1){
				currentPlayer.setCurrentRank(rank);
				rank++;
			}
			else
			{
				nonActivePlayers.add(currentPlayer);
				//.remove(currentPlayer);
			}
		}
		for (Player currentPlayer : nonActivePlayers){
			players.remove(currentPlayer);
		}

		upDownLayout.addComponents(up,down,updateJdfRanks);
		layout.addComponents(grid,upDownLayout);
		layout.setExpandRatio(grid, 1f);
		addComponent(layout);
	}
	//private SortableTable cheatSheetTable = new SortableTable();
	public void enter(ViewChangeEvent event) {

	}

}