package com.jdf.ff_portal.views;


import java.util.ArrayList;
import java.util.List;

import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.backend.data.Player;
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

		List<Player> players = PlayerService.getInstance().getAllPlayers();

		Grid<Player> grid = new Grid<>();
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setItems(players);
		Column<Player, ?> currentRankCol = grid.addColumn(p->PlayerService.getInstance()
				.getSbfRankById(p.getPlayerId()).getRank())
				.setCaption("My Rank");
		grid.addColumn(Player::getProRank).setCaption("Pro Rank");
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
			LeagueInfoManager.getInstance().movePlayerUp(selectedPlayer);		
			grid.sort(currentRankCol, SortDirection.ASCENDING);		
			grid.scrollTo(selectedPlayer.getSbfRank().getRank()-1);
		} });

		Button down = new Button("Down");
		down.addClickListener(new Button.ClickListener()
		{ @Override public void buttonClick(Button.ClickEvent clickEvent)
		{
			final List<Player> PlayerList =  new ArrayList<Player>(grid.getSelectedItems());
			if (PlayerList.size() == 0) return;
			Player selectedPlayer = PlayerList.get(0);
			LeagueInfoManager.getInstance().movePlayerDown(selectedPlayer);		
			grid.sort(currentRankCol, SortDirection.ASCENDING);		
			grid.scrollTo(selectedPlayer.getSbfRank().getRank()-1);
		} });

		Button updateJdfRanks = new Button ("Update JDF Ranks");
		updateJdfRanks.addClickListener(new Button.ClickListener() {
			@Override public void buttonClick(Button.ClickEvent clickEvent){
				PlayerService.getInstance().updateSbfRanks();
				Notification.show("Ranks updated successfully");
			}
		});
		
		grid.sort(currentRankCol, SortDirection.ASCENDING);		
		grid.setSizeFull();

		upDownLayout.addComponents(up,down,updateJdfRanks);
		layout.addComponents(grid,upDownLayout);
		layout.setExpandRatio(grid, 1f);
		addComponent(layout);
	}
	public void enter(ViewChangeEvent event) {

	}

}