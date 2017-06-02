package com.jdf.ff_portal.views;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.jdf.ff_portal.bindings.FantasyLeague;
import com.jdf.ff_portal.bindings.FantasyTeam;
import com.jdf.ff_portal.bindings.Player;
import com.jdf.ff_portal.bindings.Players;
import com.jdf.ff_portal.utils.LeagueInfoManager;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class DraftDayView extends HorizontalLayout implements View {
	private FantasyLeague SBF;
	private Label onTheClock =  new Label();
	//private int currentRound = 1;
	private HashMap<Integer, FantasyTeam> draftOrder = new HashMap<Integer, FantasyTeam>();
	private Grid<Player> availableGrid;
	private Grid<Player> draftedGrid;
	private ListDataProvider<Player> playersDataProvider;
	private ListDataProvider<Player> draftedPlayersDataProvider;
	Players playerList;
	private LeagueInfoManager leagueInfoManager;

	private String availPlayerNameFilterValue="";
	private String availPositionFilterValue="";
	private String availIsDraftedFilterValue="Available";

	private final Command filterCommand = new Command() {
		@Override
		public void menuSelected(final MenuItem selectedItem) {
			String filter = selectedItem.getParent().getDescription();
			String filterValue = selectedItem.getText();
			if(filter != null ){
				if (filter.equals("Position Filter")){
					setAvailPositionFilterValue(filterValue);
					selectedItem.getParent().setText(filterValue);
				}
				if (filter.equals("Is Drafted Filter")){
					setAvailIsDraftedFilterValue(filterValue);
					selectedItem.getParent().setText(filterValue);
				}
			}
			playersDataProvider.refreshAll();
		}};

		public DraftDayView(){
			leagueInfoManager = LeagueInfoManager.getInstance();
			SBF = leagueInfoManager.getLeague();
			playerList = leagueInfoManager.getPlayers();

			for(FantasyTeam currentTeam : SBF.getTeams()){
				draftOrder.put(currentTeam.getDraftPosition(), currentTeam);
			}
			//int currentRound = leagueInfoManager.getRoundFromPick(SBF.getCurrentPick());
			onTheClock.setCaptionAsHtml(true);
			setOnTheClockCaption();
			setSizeFull();


			final VerticalLayout layout = new VerticalLayout();
			layout.setMargin(false);
			layout.setSizeFull();
			layout.setSpacing(false);
			
			final HorizontalLayout bannerLayout = new HorizontalLayout();
			bannerLayout.setSizeFull();

			final HorizontalLayout gridsLayout = new HorizontalLayout();
			gridsLayout.setSizeFull();

			List<Player> players = playerList.getPlayers();

			availableGrid = configureAvailableGrid(players);
			availableGrid.setSizeFull();

			draftedGrid = configureDraftedGrid(players);
			
			Button undoButton = new Button("Undo");
			undoButton.addClickListener(new Button.ClickListener()
			{ @Override public void buttonClick(Button.ClickEvent clickEvent)
			{
				LeagueInfoManager.getInstance().undoLastDraftPick();
				draftedPlayersDataProvider.refreshAll();
				playersDataProvider.refreshAll();
				setOnTheClockCaption();
			} });

			bannerLayout.addComponents(onTheClock, undoButton);
			bannerLayout.setComponentAlignment(undoButton, Alignment.TOP_RIGHT);
			bannerLayout.setHeight("70px");
			gridsLayout.addComponents(availableGrid,draftedGrid);
			layout.addComponents(bannerLayout,gridsLayout);
			layout.setExpandRatio(gridsLayout, 1);
			addComponent(layout);
		}

		public void enter(ViewChangeEvent event) {
		}

		@SuppressWarnings("unchecked")
		public Grid<Player> configureDraftedGrid(List<Player> players){
			Grid<Player> draftedGrid = new Grid<>();
			draftedGrid.setItems(players);
			draftedGrid.setSizeFull();
			draftedGrid.setSelectionMode(SelectionMode.SINGLE);
			draftedGrid.addColumn(Player::getPosition).setCaption("Position");
			draftedGrid.addColumn(Player::getDisplayName).setCaption("Name");
			draftedGrid.addColumn(Player::getOwner).setCaption("Drafted By");
			draftedGrid.addColumn(Player::getDraftedSlot).setCaption("Drafted");
			draftedPlayersDataProvider = (ListDataProvider<Player>) draftedGrid.getDataProvider();
			draftedPlayersDataProvider.setFilter(Player::getOwner, a -> a!=null);
			return draftedGrid;

		}

		@SuppressWarnings("unchecked")
		public Grid<Player> configureAvailableGrid(List<Player> players){
			Grid<Player> availableGrid = new Grid<>();
			availableGrid.setItems(players);

			availableGrid.setSizeFull();
			availableGrid.setSelectionMode(SelectionMode.SINGLE);
			availableGrid.addColumn(Player::getCurrentRank).setCaption("My Rank");
			availableGrid.addColumn(Player::getPosition).setCaption("Position").setId("PositionColumn");
			availableGrid.addColumn(Player::getDisplayName).setCaption("Name").setId("PlayerNameColumn");
			availableGrid.addColumn(Player::getTeam).setCaption("Team");
			availableGrid.addColumn(player->"Draft!", draftedButtonRenderer()).setId("DraftedColumn");

			playersDataProvider = (ListDataProvider<Player>) availableGrid.getDataProvider();

			playersDataProvider.setFilter(Player::getThisPlayer, a -> availableGridFilter(a));
			HeaderRow filterRow = availableGrid.appendHeaderRow();

			TextField availPlayerNameFilter = getTextFilter();
			availPlayerNameFilter.addValueChangeListener(event -> {
				setAvailPlayerNameFilterValue(event.getValue());
				playersDataProvider.refreshAll();
			});
			
			availableGrid.getColumn("DraftedColumn").setStyleGenerator(p -> {
				if (p.getOwner() != null) {
					return "hidden" ;
				}else{
					return null;
				}
			});

			MenuBar availPositionFilter = getPositionFilter();

			MenuBar availIsDraftedFilter = getIsDraftedFilter();

			filterRow.getCell("PlayerNameColumn").setComponent(availPlayerNameFilter);
			filterRow.getCell("PositionColumn").setComponent(availPositionFilter);
			filterRow.getCell("DraftedColumn").setComponent(availIsDraftedFilter);

			return availableGrid;

		}

		public boolean availableGridFilter(Player player){
			//Player Name
			if (availPlayerNameFilterValue != null && !availPlayerNameFilterValue.equals("")){
				String playerLower = player.getDisplayName().toLowerCase(Locale.ENGLISH);
				if(!playerLower.contains(availPlayerNameFilterValue)) return false;
			}
			//are they drafted?
			if (availIsDraftedFilterValue.equals("Available")){
				if (player.getDraftedSlot() > 0) return false;
			}else if (availIsDraftedFilterValue.equals("Drafted")){
				if (player.getDraftedSlot() == 0) return false;
			}

			//player position
			if (availPositionFilterValue != null && !availPositionFilterValue.equals("") && !availPositionFilterValue.equalsIgnoreCase("All")){
				if (!player.getPosition().toLowerCase().equals(availPositionFilterValue)) return false;
			}
			return true;
		}

		public void removeNonActivePlayers(List<Player> players){//just for when I import a new list and want to get rid of them.
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
				}
			}
			for (Player currentPlayer : nonActivePlayers){
				players.remove(currentPlayer);
			}

		}

		public void setAvailPlayerNameFilterValue(String name){
			this.availPlayerNameFilterValue = name.toLowerCase();
		}

		public void setAvailPositionFilterValue(String position){
			if (position == null) {
				this.availPositionFilterValue = position;
			}
			else {
				this.availPositionFilterValue = position.toLowerCase();
			}
		}

		public void setAvailIsDraftedFilterValue(String isDraftedFilterSelection){
			this.availIsDraftedFilterValue = isDraftedFilterSelection;
		}


		public TextField getTextFilter(){
			TextField filter = new TextField();
			filter.setWidth("100%");
			filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
			filter.setPlaceholder("Filter");
			return filter;
		}


		public MenuBar getPositionFilter(){
			MenuBar posFilterMenuBar = new MenuBar();
			posFilterMenuBar.addStyleName("borderless");
			MenuItem availPlayerPosFilter = posFilterMenuBar.addItem("Filter", null);
			availPlayerPosFilter.addItem("All", filterCommand);
			availPlayerPosFilter.addItem("QB", filterCommand);
			availPlayerPosFilter.addItem("RB", filterCommand);
			availPlayerPosFilter.addItem("TE", filterCommand);
			availPlayerPosFilter.addItem("K", filterCommand);
			availPlayerPosFilter.addItem("DEF", filterCommand);
			availPlayerPosFilter.setDescription("Position Filter");
			return posFilterMenuBar;
		}

		public MenuBar getIsDraftedFilter(){
			MenuBar isDraftedMenubar = new MenuBar();
			isDraftedMenubar.addStyleName("borderless");
			MenuItem availIsDraftedItem = isDraftedMenubar.addItem("Filter", null);
			availIsDraftedItem.addItem("Drafted", filterCommand);
			availIsDraftedItem.addItem("Available", filterCommand);
			availIsDraftedItem.addItem("All", filterCommand);
			availIsDraftedItem.setDescription("Is Drafted Filter");
			return isDraftedMenubar;
		}

		@SuppressWarnings({ "rawtypes" })
		public ButtonRenderer draftedButtonRenderer (){
			ButtonRenderer<Object> test = new ButtonRenderer<Object>();
			test.addClickListener(clickEvent -> {
				Player selectedPlayer = (Player)clickEvent.getItem();
				leagueInfoManager.draftPlayer(selectedPlayer);
				availableGrid.getDataProvider().refreshAll();
				draftedGrid.getDataProvider().refreshAll();
				
				setOnTheClockCaption();
			});
			return test;
		}
		
		public void setOnTheClockCaption(){
			String teamOnTheClock = leagueInfoManager.getTeamOnTheClockFromPick(SBF.getCurrentPick()).getOwner();
			int round = leagueInfoManager.getRoundFromPick(SBF.getCurrentPick());
			int pickInRound =leagueInfoManager.getPickInRoundFromPick(SBF.getCurrentPick());
			onTheClock.setCaption("<h2>On The Clock: " + teamOnTheClock + 
					"<br/>Round " + round + ", pick " + pickInRound + "</h2>");
		}
}