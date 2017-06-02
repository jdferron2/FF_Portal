package com.jdf.ff_portal;

import javax.servlet.annotation.WebServlet;

import com.jdf.ff_portal.views.CheatSheetView;
import com.jdf.ff_portal.views.DraftDayView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
@Theme("ff_portal")
public class Ff_portalUI extends UI {
	private VerticalLayout 		viewLayout 		= new VerticalLayout();
	private VerticalLayout 		rootLayout 		= new VerticalLayout();
	private Navigator 			navigator;	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Ff_portalUI.class)
	public static class Servlet extends VaadinServlet {
	}
	private final Command menuCommand = new Command() {
		@Override
		public void menuSelected(final MenuItem selectedItem) {
			String navString = selectedItem.getText();
			if (navString.equals("Cheatsheet")) navString = "";
			navigator.navigateTo(navString);
		}
	};

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		CheatSheetView cheatSheetView =  new CheatSheetView();
		setContent(rootLayout);
		viewLayout.setMargin(false);
		viewLayout.setSizeFull();
		rootLayout.setSpacing(false);
		rootLayout.setSizeFull();
		MenuBar menu = new MenuBar();
		menu.addItem("Cheatsheet", menuCommand);
		menu.addItem("Draft Day", menuCommand);
		rootLayout.addComponents(menu,viewLayout);
		rootLayout.setExpandRatio(viewLayout, 1f);
		
		navigator = new Navigator(this,viewLayout);
		navigator.addView("", cheatSheetView);
		navigator.addView("Draft Day", new DraftDayView());
		//cheatSheetView.setTableEditing(false);
//		navigator.addViewChangeListener(new ViewChangeListener() {
//
//			@Override
//			public boolean beforeViewChange(ViewChangeEvent event) {
//				return true;
//			}
//
//			@Override
//			public void afterViewChange(ViewChangeEvent event) {
//				if(event.getNewView() instanceof CheatSheetView){
//					((CheatSheetView)event.getNewView()).setTableEditing(false);
//				}
//			}
//			
//		});
		navigator.navigateTo("");
	}

}