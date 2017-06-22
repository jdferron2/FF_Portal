package com.jdf.ff_portal.views;


import java.util.ArrayList;
import java.util.List;

import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.SbfDraftRecord;
import com.jdf.ff_portal.backend.data.SbfRank;
import com.jdf.ff_portal.utils.LeagueInfoManager;
import com.vaadin.data.provider.ListDataProvider;
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
	private ListDataProvider<SbfRank> playersDataProvider;
	boolean viewBuilt = false;
	public CheatSheetView(){
		
	}
	public void enter(ViewChangeEvent event) {
		if(!viewBuilt) {
			buildView();
			viewBuilt=true;
		}
		playersDataProvider.refreshAll();
	}
	
	private void buildView(){
		setSizeFull();
		final VerticalLayout layout = new VerticalLayout();
		final HorizontalLayout upDownLayout = new HorizontalLayout();

		layout.setSizeFull();
		List<SbfRank> myRanks = PlayerService.getInstance().getSbfRanks(1);

		Grid<SbfRank> grid = new Grid<>();
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setItems(myRanks);
		Column<SbfRank, ?> currentRankCol = grid.addColumn(SbfRank::getRank)
				.setCaption("My Rank");
		grid.addColumn(s->PlayerService.getInstance().getPlayerById(s.getPlayerId()).getProRank()
				).setCaption("Pro Rank");
		grid.addColumn(s->PlayerService.getInstance().getPlayerById(s.getPlayerId()).getDisplayName()
				).setCaption("Name");
		grid.addColumn(s->PlayerService.getInstance().getPlayerById(s.getPlayerId()).getTeam()
				).setCaption("Team");
		grid.addColumn(s->PlayerService.getInstance().getPlayerById(s.getPlayerId()).getPosition()
				).setCaption("Position");

		Button up = new Button("Up");
		up.addClickListener(new Button.ClickListener()
		{ @Override public void buttonClick(Button.ClickEvent clickEvent)
		{
			final List<SbfRank> rankList =  new ArrayList<SbfRank>(grid.getSelectedItems());
			if (rankList.size() == 0) return;
			Player selectedPlayer = PlayerService.getInstance().getPlayerById(rankList.get(0).getPlayerId());
			LeagueInfoManager.getInstance().movePlayerUp(selectedPlayer);		
			grid.sort(currentRankCol, SortDirection.ASCENDING);		
			grid.scrollTo(PlayerService.getInstance().getSbfRankById(selectedPlayer.getPlayerId()).getRank()-1);
		} });

		Button down = new Button("Down");
		down.addClickListener(new Button.ClickListener()
		{ @Override public void buttonClick(Button.ClickEvent clickEvent)
		{
			final List<SbfRank> rankList =  new ArrayList<SbfRank>(grid.getSelectedItems());
			if (rankList.size() == 0) return;
			Player selectedPlayer = PlayerService.getInstance().getPlayerById(rankList.get(0).getPlayerId());
			LeagueInfoManager.getInstance().movePlayerDown(selectedPlayer);		
			grid.sort(currentRankCol, SortDirection.ASCENDING);		
			grid.scrollTo(PlayerService.getInstance().getSbfRankById(selectedPlayer.getPlayerId()).getRank()-1);
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
		playersDataProvider = (ListDataProvider<SbfRank>) grid.getDataProvider();
	}

}