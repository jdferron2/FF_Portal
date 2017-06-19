package com.jdf.ff_portal.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.SbfDraftRecord;
import com.jdf.ff_portal.backend.data.SbfRank;
import com.jdf.ff_portal.utils.PropertyReader;

public class PlayerService {
	private List<Player> players;
	private List<SbfRank> sbfRanks;
	protected static PlayerService INSTANCE;

	public synchronized static PlayerService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlayerService();
		}
		return INSTANCE;
	}
	public synchronized List<Player> getAllPlayers() {
		if (players == null) {
			players = new ArrayList<Player>();
			Statement stmt=null;
			ResultSet rs=null;
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
				stmt = conn.createStatement();
				String sql = "select "
						+ "player_id, pro_rank, full_name, first_name, last_name, height, jersey_num, position, nfl_team, weight, DOB "
						+ "from players";

				rs = stmt.executeQuery(sql);
				while (rs.next()){
					Player player = new Player(rs.getInt("player_id"),
							rs.getInt("jersey_num"),
							rs.getString("last_name"),
							rs.getString("first_name"),
							rs.getString("full_name"),
							rs.getString("nfl_team"),
							rs.getString("position"),
							rs.getString("height"),
							rs.getInt("weight"),
							rs.getDate("DOB"),
							rs.getInt("pro_rank")
							);
					players.add(player);    				
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
			for (SbfRank rank : getSbfRanks(1)){
				Player player = getPlayerById(rank.getPlayerId());
				player.setSbfRank(rank);
			}

		}

		return this.players;
	}

	public void setPlayers( List<Player> players){
		this.players = players;
	}

	public synchronized List<SbfRank> getSbfRanks(int sbfId) {
		if (sbfRanks == null) {
			sbfRanks = new ArrayList<SbfRank>();
			Statement stmt=null;
			ResultSet rs=null;
			Connection conn = null;
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
				stmt = conn.createStatement();
				String sql = "select "
						+ "SBF_ID, PLAYER_ID, RANK "
						+ "from SBF_RANKS "
						+ "where SBF_ID = " + sbfId;

				rs = stmt.executeQuery(sql);
				while (rs.next()){
					SbfRank rank = new SbfRank();
					rank.setSbfId(rs.getInt("SBF_ID"));
					rank.setPlayerId(rs.getInt("PLAYER_ID"));
					rank.setRank(rs.getInt("RANK"));    				
					sbfRanks.add(rank);    				
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

		return this.sbfRanks ;
	}

	public synchronized void updateSbfRanks() {
		Connection conn = null;
		PreparedStatement prepStmt;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			
			String sql = "update SBF_RANKS set "
					+ "RANK "
					+ "= ? "
					+ "where player_id = ? and SBF_ID = ?";

			prepStmt = conn.prepareStatement(sql);
			for(SbfRank rank : getSbfRanks(1)){
				if(rank.isFlagForUpdate()){
					prepStmt.setInt(1,rank.getRank());
					prepStmt.setInt(2, rank.getPlayerId());
					prepStmt.setInt(3, rank.getSbfId());
					prepStmt.executeUpdate();
				}
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

	public synchronized SbfRank getSbfRankById(int playerId) {
		return sbfRanks.stream().filter(
				s->s.getPlayerId()==playerId).findFirst().orElse(null);
	}

	public synchronized SbfRank getSbfRankByRank(int rank){
		return sbfRanks.stream().filter(
				s->s.getRank()==rank).findFirst().orElse(null);
	}


	public synchronized Player getPlayerBySbfRank(int rank){
		return players.stream().filter(
				p->p.getSbfRank().getRank()==rank).findFirst().orElse(null);
	}

	public synchronized Player getPlayerById(int playerId) {
		return players.stream().filter(
				p->p.getPlayerId()==playerId).findFirst().orElse(null);
	}	

}
