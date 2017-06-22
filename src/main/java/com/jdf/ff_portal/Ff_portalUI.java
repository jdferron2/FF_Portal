package com.jdf.ff_portal;

import javax.servlet.annotation.WebServlet;
import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.utils.SessionAttributes;
import com.jdf.ff_portal.views.AdminView;
import com.jdf.ff_portal.views.CheatSheetView;
import com.jdf.ff_portal.views.DraftDayView;
import com.jdf.ff_portal.views.LoginView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
@Theme("ff_portal")
public class Ff_portalUI extends UI {
	private VerticalLayout 		viewLayout 		= new VerticalLayout();
	private VerticalLayout 		rootLayout 		= new VerticalLayout();
	private HorizontalLayout 	menuLayout 		= new HorizontalLayout();
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
		getSession().setAttribute(SessionAttributes.USER_NAME, "Jeff");
		getSession().setAttribute(SessionAttributes.LEAGUE_ID, 1);
		PlayerService.getInstance().getAllPlayers();
		CheatSheetView cheatSheetView =  new CheatSheetView();
		setContent(rootLayout);
		viewLayout.setMargin(false);
		viewLayout.setSizeFull();
		rootLayout.setSpacing(false);
		rootLayout.setSizeFull();
		menuLayout.setWidth("100%");
		MenuBar menu = new MenuBar();
		menu.addItem("Cheatsheet", menuCommand);
		menu.addItem("Draft Day", menuCommand);
		menu.addItem(AdminView.NAME, menuCommand);
		
		Button logout = new Button("Logout");
		logout.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				//navigate to root and close session
				getSession().close();
				getUI().getPage().setLocation("/FF_Portal/?restartApplication" );	
			}
			
		});
		
		menuLayout.addComponents(menu, logout);
		menuLayout.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);
		rootLayout.addComponents(menuLayout,viewLayout);
		rootLayout.setExpandRatio(viewLayout, 1f);
		
		navigator = new Navigator(this,viewLayout);
		navigator.addView("", cheatSheetView);
		navigator.addView("Draft Day", new DraftDayView());
		navigator.addView(LoginView.NAME, new LoginView());
		navigator.addView(AdminView.NAME, new AdminView());
		//cheatSheetView.setTableEditing(false);
		navigator.addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {

				boolean isLoggedIn = getSession().getAttribute(SessionAttributes.USER_NAME) != null;
				boolean isLoginView = (event.getNewView() instanceof LoginView);
				menuLayout.setVisible(true);
				
				if (!isLoggedIn && !isLoginView) {
					// Redirect to login view always if a user has not yet
					// logged in
					getNavigator().navigateTo(LoginView.NAME);
					return false;

				} else if (isLoggedIn && isLoginView) {
					// If someone tries to access to login view while logged in,
					// then cancel
					return false;
				} else if (isLoginView){
					menuLayout.setVisible(false);
				}
				
				return true;
			}

//			@Override
//			public void afterViewChange(ViewChangeEvent event) {
//				if(event.getNewView() instanceof CheatSheetView){
//					((CheatSheetView)event.getNewView()).setTableEditing(false);
//				}
//			}
			
		});
		navigator.navigateTo("");
	}

}