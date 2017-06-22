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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.jdf.ff_portal.backend.data.Player;
import com.jdf.ff_portal.backend.data.SbfDraftRecord;
import com.jdf.ff_portal.backend.data.SbfRank;
import com.jdf.ff_portal.utils.PropertyReader;
@Path("/PlayerService") 

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
	@GET 
	@Path("/players") 
	@Produces(MediaType.APPLICATION_XML) 
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

		}
		//		ArrayList<Player> playersClone = new ArrayList<Player>();
		//		for(Player p : players){
		//			playersClone.add(p);
		//		}
		//		return playersClone;
		return players;
	}

	public synchronized void insertPlayer(Player p) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();

			String sql = "insert into players "
					+ "(player_id, pro_rank, full_name, first_name, last_name, height, jersey_num, position, nfl_team, weight, DOB) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?)";
			prepStmt = conn.prepareStatement(sql);

			prepStmt.setInt(1, p.getPlayerId());
			prepStmt.setInt(2, p.getProRank());
			prepStmt.setString(3, p.getDisplayName());
			prepStmt.setString(4, p.getFname());
			prepStmt.setString(5, p.getLname());
			prepStmt.setString(6, p.getHeight());
			prepStmt.setInt(7, p.getJersey());	
			prepStmt.setString(8, p.getPosition());
			prepStmt.setString(9, p.getTeam());
			prepStmt.setInt(10, p.getWeight());	
			prepStmt.setDate(11, p.getDob());
			prepStmt.execute();
			getAllPlayers().add(p);
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

	public synchronized void deletePlayer(Player p) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();

			String sql = "delete from players "
					+ "where player_id = ?";
			prepStmt = conn.prepareStatement(sql);

			prepStmt.setInt(1, p.getPlayerId());
			prepStmt.execute();
			getAllPlayers().remove(p);
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

	public synchronized void deleteAllPlayers() {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();

			String sql = "delete from players";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.execute();
			players.clear();
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
	public synchronized void updatePlayer(Player p) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();

			String sql = "update players "
					+ "set "
					+ "pro_rank=?, "
					+ "full_name=?, "
					+ "first_name=?, "
					+ "last_name=?, "
					+ "height=?, "
					+ "jersey_num=?, "
					+ "position=?, "
					+ "nfl_team=?, "
					+ "weight=?,"
					+ "DOB=? "
					+ "where player_id = ? ";
			prepStmt = conn.prepareStatement(sql);


			prepStmt.setInt(1, p.getProRank());
			prepStmt.setString(2, p.getDisplayName());
			prepStmt.setString(3, p.getFname());
			prepStmt.setString(4, p.getLname());
			prepStmt.setString(5, p.getHeight());
			prepStmt.setInt(6, p.getJersey());	
			prepStmt.setString(7, p.getPosition());
			prepStmt.setString(8, p.getTeam());
			prepStmt.setInt(9, p.getWeight());	
			prepStmt.setDate(10, p.getDob());
			prepStmt.setInt(11, p.getPlayerId());
			prepStmt.executeUpdate();
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

	public synchronized void setPlayers( List<Player> players){
		this.players = players;
	}

	public synchronized List<SbfRank> getSbfRanks(int sbfId) {
		if (sbfRanks == null ) {
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
					SbfRank rank = new SbfRank(rs.getInt("SBF_ID"),
							rs.getInt("PLAYER_ID"),
							rs.getInt("RANK"));			
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

		//		ArrayList<SbfRank> rankClone = new ArrayList<SbfRank>();
		//		for(SbfRank r : sbfRanks){
		//			rankClone.add(r);
		//		}
		//		return rankClone;
		return sbfRanks;
	}

	public synchronized void insertSbfRank(SbfRank s) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();

			String sql = "insert into sbf_ranks "
					+ "(player_id, sbf_id, rank) "
					+ "values (?,?,?)";
			prepStmt = conn.prepareStatement(sql);

			prepStmt.setInt(1, s.getPlayerId());
			prepStmt.setInt(2, s.getSbfId());
			prepStmt.setInt(3, s.getRank());	
			prepStmt.execute();
			getSbfRanks(1).add(s);
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

	public synchronized void deleteSbfRank(SbfRank s) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();

			String sql = "delete from sbf_ranks "
					+ "where sbf_id = ? and player_id = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, s.getSbfId());
			prepStmt.setInt(2, s.getPlayerId());
			prepStmt.execute();
			getSbfRanks(1).remove(s);
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

	public synchronized void deleteAllSbfRanks(int sbfId) {
		PreparedStatement prepStmt=null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root",PropertyReader.getProperty("adminPass"));
			//stmt = conn.createStatement();

			String sql = "delete from sbf_ranks "
					+ "where sbf_id = ?";
			prepStmt = conn.prepareStatement(sql);
			prepStmt.setInt(1, sbfId);
			prepStmt.execute();
			sbfRanks.clear();
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
				p->getSbfRankById(p.getPlayerId()).getRank()==rank).findFirst().orElse(null);
	}

	public synchronized Player getPlayerById(int playerId) {
		return players.stream().filter(
				p->p.getPlayerId()==playerId).findFirst().orElse(null);
	}	

	public synchronized int getMaxProRank() {
		return players.stream().max(
				(p1,p2)->Integer.compare(p1.getProRank(), p2.getProRank())
				).get().getProRank();
	}

	public synchronized int getMaxSbfRank() {
		return this.getSbfRanks(1).stream().max(
				(r1,r2)->Integer.compare(r1.getRank(), r2.getRank())
				).get().getRank();
	}	

}
