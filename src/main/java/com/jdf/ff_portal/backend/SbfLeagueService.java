package com.jdf.ff_portal.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdf.ff_portal.backend.data.SbfLeague;
import com.jdf.ff_portal.utils.PropertyReader;

public class SbfLeagueService {
	protected static SbfLeagueService INSTANCE;
	protected List<SbfLeague> sbfLeagues; 

	public synchronized static SbfLeagueService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SbfLeagueService();
		}
		return INSTANCE;
	}

	public synchronized List<SbfLeague> getAllSbfLeagues() {
		if (sbfLeagues == null) {
			sbfLeagues = new ArrayList<SbfLeague>();
			Statement stmt=null;
			ResultSet rs=null;
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
				stmt = conn.createStatement();
				String sql = "select "
						+ "LEAGUE_ID, LEAGUE_NAME, NUM_TEAMS "
						+ "from SBF_LEAGUE";

				rs = stmt.executeQuery(sql);
				while (rs.next()){
					SbfLeague league = new SbfLeague(rs.getInt("LEAGUE_ID"),
							rs.getString("LEAGUE_NAME"),
							rs.getInt("NUM_TEAMS")
							);
					sbfLeagues.add(league);    				
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

		return this.sbfLeagues;
	}
	
	public synchronized SbfLeague getLeagueById(int id) {
		return sbfLeagues.stream().filter(
				l->l.getLeagueId()==id).findFirst().orElse(null);
	}	
}
