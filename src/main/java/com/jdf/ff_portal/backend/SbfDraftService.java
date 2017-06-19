package com.jdf.ff_portal.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdf.ff_portal.backend.data.SbfDraftRecord;
import com.jdf.ff_portal.utils.PropertyReader;
import com.jdf.ff_portal.utils.SessionAttributes;
import com.vaadin.ui.UI;

public class SbfDraftService {
	protected static SbfDraftService INSTANCE;
	protected List<SbfDraftRecord> sbfDraftRecords; 

	public synchronized static SbfDraftService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SbfDraftService();
		}
		return INSTANCE;
	}

	public synchronized List<SbfDraftRecord> getAllSbfDraftRecords() {
		Integer leagueId = (Integer) UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID);
		if (sbfDraftRecords == null) {
			sbfDraftRecords = new ArrayList<SbfDraftRecord>();
			Statement stmt=null;
			ResultSet rs=null;
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
				stmt = conn.createStatement();
				String sql = "select "
						+ "SBF_ID, PLAYER_ID, SLOT_DRAFTED, DRAFTED_TS "
						+ "from SBF_DRAFT where LEAGUE_ID = " + leagueId;

				rs = stmt.executeQuery(sql);
				while (rs.next()){
					SbfDraftRecord drafRecord = new SbfDraftRecord(leagueId, 
							rs.getInt("SBF_ID"),
							rs.getInt("PLAYER_ID"),
							rs.getInt("SLOT_DRAFTED"),
							rs.getTimestamp("DRAFTED_TS")
							);
					sbfDraftRecords.add(drafRecord);    				
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

		return this.sbfDraftRecords;
	}

	public synchronized void insertDraftRecord(SbfDraftRecord record) {
		Integer leagueId = (Integer) UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID);
		Connection conn = null;
		PreparedStatement prepStmt;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));

			String sql = "insert into SBF_DRAFT (LEAGUE_ID, SBF_ID, PLAYER_ID, SLOT_DRAFTED, DRAFTED_TS) "
					+ "values (?,?,?,?,?)";

			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, leagueId);
			prepStmt.setInt(2, record.getSbfId());
			prepStmt.setInt(3, record.getPlayerId());
			prepStmt.setInt(4, record.getSlotDrafted());
			prepStmt.setTimestamp(5, record.getTimeDrafted());
			prepStmt.execute();
			this.getAllSbfDraftRecords().add(record);
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
	
	public synchronized void deleteDraftRecord(SbfDraftRecord record) {
		Integer leagueId = (Integer) UI.getCurrent().getSession().getAttribute(SessionAttributes.LEAGUE_ID);
		Connection conn = null;
		PreparedStatement prepStmt;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));

			String sql = "DELETE FROM SBF_DRAFT "
					+ "WHERE SBF_ID = ? and PLAYER_ID = ? and LEAGUE_ID = ?";

			prepStmt = conn.prepareStatement(sql);

			prepStmt.setInt(1, record.getSbfId());
			prepStmt.setInt(2, record.getPlayerId());
			prepStmt.setInt(3, leagueId);
			prepStmt.execute();
			this.getAllSbfDraftRecords().remove(record);
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

	public synchronized SbfDraftRecord getSbfDraftRecordByPlayerId(int playerId){
		return getAllSbfDraftRecords().stream().
				filter(r->r.getPlayerId()==playerId).findFirst().orElse(null);
	}


}
