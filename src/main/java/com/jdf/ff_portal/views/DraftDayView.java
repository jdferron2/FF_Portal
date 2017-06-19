package com.jdf.ff_portal.views;


import java.util.List;
import java.util.Locale;

import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.backend.SbfDraftService;
import com.jdf.ff_portal.backend.SbfTeamService;
import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.SbfDraftRecord;
import com.jdf.ff_portal.backend.data.SbfTeam;
import com.jdf.ff_portal.utils.LeagueInfoManager;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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
	//private FantasyLeague SBF;
	private Label onTheClock =  new Label();
	//private int currentRound = 1;
	//private HashMap<Integer, FantasyTeam> draftOrder = new HashMap<Integer, FantasyTeam>();
	private Grid<Player> availableGrid;
	private Grid<SbfDraftRecord> draftedGrid;
	private ListDataProvider<Player> playersDataProvider;
	private ListDataProvider<SbfDraftRecord> draftedPlayersDataProvider;
	List<Player> playerList;
	//private LeagueInfoManager leagueInfoManager;

	private String availPlayerNameFilterValue="";
	private String availPositionFilterValue="";
	private String availIsDraftedFilterValue="Available";
	private boolean viewBuilt = false;

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

		}

		public void enter(ViewChangeEvent event) {
			if (!viewBuilt) buildView();
		}

		protected void buildView(){
			playerList =  PlayerService.getInstance().getAllPlayers();
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

			availableGrid = configureAvailableGrid(playerList);
			availableGrid.setSizeFull();

			draftedGrid = configureDraftedGrid();

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
		@SuppressWarnings("unchecked")
		public Grid<SbfDraftRecord> configureDraftedGrid(){
			Grid<SbfDraftRecord> draftedGrid = new Grid<>();
			draftedGrid.setItems(SbfDraftService.getInstance().getAllSbfDraftRecords());
			draftedGrid.setSizeFull();
			draftedGrid.setSelectionMode(SelectionMode.SINGLE);
			draftedGrid.addColumn(s->PlayerService.getInstance().getPlayerById(s.getPlayerId()).getDisplayName())
			.setCaption("Player Name");
			draftedGrid.addColumn(s->PlayerService.getInstance().getPlayerById(s.getPlayerId()).getPosition())
			.setCaption("Position");
			draftedGrid.addColumn(s->SbfTeamService.getInstance().getSbfTeamBySbfId(s.getSbfId()).getOwnerName())
			.setCaption("Drafted By");
			draftedGrid.addColumn(SbfDraftRecord::getSlotDrafted).setCaption("Drafted");
			draftedPlayersDataProvider = (ListDataProvider<SbfDraftRecord>) draftedGrid.getDataProvider();
			return draftedGrid;
		}

		@SuppressWarnings("unchecked")
		public Grid<Player> configureAvailableGrid(List<Player> players){
			Grid<Player> availableGrid = new Grid<>();
			availableGrid.setItems(players);

			availableGrid.setSizeFull();
			availableGrid.setSelectionMode(SelectionMode.SINGLE);
			availableGrid.addColumn(p->p.getSbfRank().getRank()).setCaption("My Rank");
			availableGrid.addColumn(Player::getPosition).setCaption("Position").setId("PositionColumn");
			availableGrid.addColumn(Player::getDisplayName).setCaption("Name").setId("PlayerNameColumn");
			availableGrid.addColumn(Player::getTeam).setCaption("Team");
			availableGrid.addColumn(player->"Draft!", draftedButtonRenderer()).setId("DraftedColumn");

			playersDataProvider = (ListDataProvider<Player>) availableGrid.getDataProvider();

			playersDataProvider.setFilter(p->p, p -> availableGridFilter(p));
			HeaderRow filterRow = availableGrid.appendHeaderRow();

			TextField availPlayerNameFilter = getTextFilter();
			availPlayerNameFilter.addValueChangeListener(event -> {
				setAvailPlayerNameFilterValue(event.getValue());
				playersDataProvider.refreshAll();
			});

			availableGrid.getColumn("DraftedColumn").setStyleGenerator(p -> {
				if (SbfDraftService.getInstance().getSbfDraftRecordByPlayerId(p.getPlayerId())!= null) {
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
				if (SbfDraftService.getInstance().getSbfDraftRecordByPlayerId(player.getPlayerId()) != null) {
					return false;
				}
			}else if (availIsDraftedFilterValue.equals("Drafted")){
				if (SbfDraftService.getInstance().getSbfDraftRecordByPlayerId(player.getPlayerId()) == null) {
					return false;
				}
			}

			//player position
			if (availPositionFilterValue != null && !availPositionFilterValue.equals("") && !availPositionFilterValue.equalsIgnoreCase("All")){
				if (!player.getPosition().toLowerCase().equals(availPositionFilterValue)) return false;
			}
			return true;
		}

		//		public void removeNonActivePlayers(List<Player> players){//just for when I import a new list and want to get rid of them.
		//			int rank = 1;
		//			List<Player> nonActivePlayers = new ArrayList<Player>();
		//			for (Player currentPlayer : players){
		//				if (currentPlayer.getActive()==1){
		//					currentPlayer.setCurrentRank(rank);
		//					rank++;
		//				}
		//				else
		//				{
		//					nonActivePlayers.add(currentPlayer);
		//				}
		//			}
		//			for (Player currentPlayer : nonActivePlayers){
		//				players.remove(currentPlayer);
		//			}
		//
		//		}

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
				LeagueInfoManager.getInstance().draftPlayer(selectedPlayer);
				availableGrid.getDataProvider().refreshAll();
				draftedGrid.getDataProvider().refreshAll();

				setOnTheClockCaption();
			});
			return test;
		}

		public void setOnTheClockCaption(){
			String teamOnTheClock = LeagueInfoManager.getInstance().getTeamOnTheClock().getOwnerName();
			int round = LeagueInfoManager.getInstance().getRound();
			int pickInRound =LeagueInfoManager.getInstance().getPickInRound();
			onTheClock.setCaption("<h2>On The Clock: " + teamOnTheClock + 
					"<br/>Round " + round + ", pick " + pickInRound + "</h2>");
		}
}