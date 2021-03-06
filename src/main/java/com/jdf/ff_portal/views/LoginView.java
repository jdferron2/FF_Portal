package com.jdf.ff_portal.views;

import java.util.ArrayList;
import java.util.Optional;

import com.jdf.ff_portal.backend.PlayerService;
import com.jdf.ff_portal.backend.SbfLeagueService;
import com.jdf.ff_portal.backend.SbfTeamService;
import com.jdf.ff_portal.backend.data.DraftRank;
import com.jdf.ff_portal.backend.data.DraftRankings;
import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.Players;
import com.jdf.ff_portal.backend.data.SbfLeague;
import com.jdf.ff_portal.backend.data.SbfRank;
import com.jdf.ff_portal.utils.RestAPIUtils;
import com.jdf.ff_portal.utils.SessionAttributes;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class LoginView extends HorizontalLayout implements View  {
	public static final String NAME = "LOGIN"; 

	public LoginView (){
		Panel panel = new Panel("Login");
		panel.setSizeUndefined();
		addComponent(panel);
		
		FormLayout content = new FormLayout();
		TextField username = new TextField("Username");
		content.addComponent(username);
		PasswordField password = new PasswordField("Password");
		content.addComponent(password);
		
		ComboBox<SbfLeague> leagueComboBox = new ComboBox<SbfLeague>("Leagues");
		leagueComboBox.setItems(SbfLeagueService.getInstance().getAllSbfLeagues());
		leagueComboBox.setItemCaptionGenerator(SbfLeague::getLeagueName);
		
		Button send = new Button("Enter");
		send.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String message = "Nope";
				if(leagueComboBox.getValue() != null && !leagueComboBox.getValue().equals("")){
					getUI().getSession().setAttribute(SessionAttributes.LEAGUE_ID, leagueComboBox.getValue().getLeagueId());
				}else {
					message = "Pick a league, dummy";
				}
				if(SbfTeamService.getInstance().getSbfTeamByName(username.getValue()) != null && message.equals("Nope")){
					getUI().getSession().setAttribute(SessionAttributes.USER_NAME, username.getValue());
					getUI().getNavigator().navigateTo("");
				}else{
					Notification.show(message, Notification.Type.WARNING_MESSAGE);
				}
			}
			
		});
		
		
		
		content.addComponents(send,leagueComboBox);
		content.setSizeUndefined();
		content.setMargin(true);
		panel.setContent(content);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

	}
	@Override
	public void enter(ViewChangeEvent event) {

	}


}
