package com.jdf.ff_portal.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.SbfDraftPick;
import com.jdf.ff_portal.backend.data.SbfLeague;
import com.jdf.ff_portal.utils.PropertyReader;
import com.jdf.ff_portal.utils.SessionAttributes;
import com.vaadin.ui.UI;

public class SbfDraftPickService {
	protected static SbfDraftPickService INSTANCE;
	static ThreadLocal<SbfDraftPickService> threadLocalSbfDraftPickService = ThreadLocal.withInitial( () -> new SbfDraftPickService() );
	protected List<SbfDraftPick> sbfDraftPicks; 

	public synchronized static SbfDraftPickService getInstance() {
		return threadLocalSbfDraftPickService.get();
//		if (INSTANCE == null) {
//			INSTANCE = new SbfDraftPickService();
//		}
//		return INSTANCE;
	}

	public synchronized List<SbfDraftPick> getAllSbfDraftPicks() {
		Integer leagueId = (Integer) UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID);
		if (sbfDraftPicks == null) {
			sbfDraftPicks = new ArrayList<SbfDraftPick>();
			Statement stmt=null;
			ResultSet rs=null;
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
				stmt = conn.createStatement();
				String sql = "select "
						+ "LEAGUE_ID, SBF_ID, PICK_NUM "
						+ "from SBF_DRAFT_PICKS "
						+ "where LEAGUE_ID = " + leagueId;

				rs = stmt.executeQuery(sql);
				while (rs.next()){
					SbfDraftPick pick = new SbfDraftPick(rs.getInt("LEAGUE_ID"),
							rs.getInt("SBF_ID"),
							rs.getInt("PICK_NUM")
							);
					sbfDraftPicks.add(pick);    				
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

		return this.sbfDraftPicks;
	}
	
	public synchronized void insertDraftPick(SbfDraftPick p) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();
			
			String sql = "insert into sbf_draft_picks "
					+ "(SBF_ID, LEAGUE_ID, PICK_NUM) "
					+ "values (?,?,?)";
			prepStmt = conn.prepareStatement(sql);

			prepStmt.setInt(1, p.getSbfId());
			prepStmt.setInt(2, p.getLeagueId());
			prepStmt.setInt(3, p.getPick());
			prepStmt.execute();
			getAllSbfDraftPicks().add(p);
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
	
	public synchronized void updateDraftPick(SbfDraftPick p) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();
			
			String sql = "update sbf_draft_picks set "
					+ "SBF_ID "
					+ "=?";
			prepStmt = conn.prepareStatement(sql);

			prepStmt.setInt(1, p.getSbfId());
			prepStmt.execute();
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
	
	public synchronized Integer getPickOwnerId(int pick) {
		SbfDraftPick test= getAllSbfDraftPicks().stream().filter(
				p->p.getPick()==pick).findFirst().orElse(null);
		if(test==null) return null;
		return test.getSbfId();
	}	
	
	public synchronized SbfDraftPick getPickBySbfIdPick(int sbfId, int pick) {
		SbfDraftPick test= getAllSbfDraftPicks().stream().filter(
				p->p.getSbfId()==sbfId && p.getPick() == pick).findFirst().orElse(null);
		if(test==null) return null;
		return test;
	}
}
