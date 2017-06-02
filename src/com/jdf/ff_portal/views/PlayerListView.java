package com.jdf.ff_portal.views;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Table.TableDragMode;

@SuppressWarnings("serial")
public class PlayerListView extends HorizontalLayout implements View {

	@Override
	public void enter(ViewChangeEvent event) {
//		final VerticalLayout layout = new VerticalLayout();
//		
//		layout.setMargin(true);
//
//		RestAPIUtils restUtils = RestAPIUtils.getInstance();
//		
//		Players playerList = restUtils.invokeQueryPlayers();
//		
//		SortableTable cheatSheetTable = new SortableTable();
//	
//		cheatSheetTable.addContainerProperty("Current Rank", Integer.class, null);
//		cheatSheetTable.addContainerProperty("Starting Rank", Integer.class, null);
//		cheatSheetTable.addContainerProperty("Name", String.class, null);
//		cheatSheetTable.addContainerProperty("Team", String.class, null);
//		cheatSheetTable.addContainerProperty("Position", String.class, null);
//		int rank = 1;
//		for (Player currentPlayer : playerList.getPlayers()){
//			if (currentPlayer.getActive() == 1){
//				cheatSheetTable.addItem(new Object[]{rank, rank, currentPlayer.getDisplayName(), currentPlayer.getTeam(),
//						currentPlayer.getPosition()}, rank);
//				rank++;
//			}
//			
//		}		
//		
//		cheatSheetTable.setColumnReorderingAllowed(true);
//		cheatSheetTable.setSortEnabled(true);
//		cheatSheetTable.setDragMode(TableDragMode.ROW);
//		layout.addComponent(cheatSheetTable);
//			
//		Button updateJdfRanks = new Button ("Update JDF Ranks");
//		updateJdfRanks.addClickListener(new Button.ClickListener() {
//		    public void buttonClick(ClickEvent event) {
//		    	
//		        playerList.updatePlayerRanks(cheatSheetTable);
//		        restUtils.marshalPlayerList(playerList);
//		    }
//		});
//		
//		layout.addComponent(updateJdfRanks);	
//		addComponent(layout);
//		setSizeFull();
	}

}
