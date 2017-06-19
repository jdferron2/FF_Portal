package com.jdf.ff_portal.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdf.ff_portal.backend.data.SbfTeam;
import com.jdf.ff_portal.utils.PropertyReader;
import com.jdf.ff_portal.utils.SessionAttributes;
import com.vaadin.ui.UI;

public class SbfTeamService {
	protected static SbfTeamService INSTANCE;
	protected List<SbfTeam> sbfTeams; 
	
	public synchronized static SbfTeamService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SbfTeamService();
		}
		return INSTANCE;
	}
	
	public synchronized List<SbfTeam> getAllSbfTeams() {
		Integer leagueId = (Integer) UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID);
		if (sbfTeams == null || sbfTeams.isEmpty()) {		
			sbfTeams = new ArrayList<SbfTeam>();
			Statement stmt=null;
			ResultSet rs=null;
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
				stmt = conn.createStatement();
				String sql = "select "
						+ "SBF_ID, OWNER_NAME, DRAFT_SLOT "
						+ "from SBF_TEAMS "
						+ "where LEAGUE_ID = " + leagueId;

				rs = stmt.executeQuery(sql);
				while (rs.next()){
					SbfTeam team = new SbfTeam(rs.getString("OWNER_NAME"),
							rs.getInt("DRAFT_SLOT"),
							rs.getInt("SBF_ID")
							);
					sbfTeams.add(team);    				
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				// handle the error
			}
			finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException sqlEx) { } // ignore

					conn = null;
				}

			}	

		}

		return this.sbfTeams;
	}
	
	public synchronized SbfTeam getSbfTeamBySbfId(int id){
		return this.getAllSbfTeams().stream().filter(
				t->t.getSbfId()==id).findFirst().orElse(null);
	}
	
	public synchronized SbfTeam getSbfTeamByName(String name){
		return this.getAllSbfTeams().stream().filter(
				t->t.getOwnerName().toUpperCase().equals(name.toUpperCase())).findFirst().orElse(null);
	}
}
