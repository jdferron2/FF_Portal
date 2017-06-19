//package com.jdf.ff_portal.utils;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import com.jdf.ff_portal.backend.PlayerService;
//import com.jdf.ff_portal.bindings.FantasyTeam;
//import com.jdf.ff_portal.bindings.Player;
//
//public class QueryHandler {
//	public QueryHandler(){
//		
//	}
//	
//	public void populatePlayers(){
//		Statement stmt=null;
//		ResultSet rs=null;
//		Connection conn = null;
//		try {
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//
//			conn = DriverManager.getConnection("jdbc:mysql://localhost/SBF","root","JF0291a@");
//			stmt = conn.createStatement();
//			//String sql = "insert into SBF_TEAMS (SBF_ID, OWNER_NAME, DRAFT_SLOT) values (?,?,?)";
//			String sql = "insert into PLAYERS"
//					+ " (player_id, pro_rank, full_name, first_name, last_name, height, jersey_num, position, nfl_team, weight, DOB) "
//					+ "values (?,?,?,?,?,?,?,?,?,?,?)";
//			PreparedStatement prepStmt = conn.prepareStatement(sql);
//			int id = 1;
//			for(com.jdf.ff_portal.backend.data.Player player: PlayerService.getInstance().getAllPlayers()){
//				String date = player.getDob();
//				if (date == null || date.equals("") || date.equals("0000-00-00")){
//					date="1900-01-01";
//				}
//				int weight ;
//				if (player.getWeight() == null || player.getWeight().equals("")){
//					weight = 0;
//				}else{
//					weight = Integer.valueOf(player.getWeight());
//				}
//				
//				prepStmt.setInt(1, player.getPlayerId());
//				prepStmt.setInt(2, player.getStartingRank());				
//				prepStmt.setString(3, player.getDisplayName());
//				prepStmt.setString(4, player.getFname());
//				prepStmt.setString(5, player.getLname());
//				prepStmt.setString(6, player.getHeight());
//				prepStmt.setInt(7, player.getJersey());
//				prepStmt.setString(8, player.getPosition());
//				prepStmt.setString(9, player.getTeam());
//				prepStmt.setInt(10, weight);
//				prepStmt.setDate(11, Date.valueOf(date));
//				if (prepStmt.execute()){
//					rs = prepStmt.getResultSet();
//				}
//				
//				
//			}
//			
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//			// handle the error
//		}
//		finally {
//		    if (conn != null) {
//		        try {
//		            conn.close();
//		        } catch (SQLException sqlEx) { } // ignore
//
//		       conn = null;
//		    }
//
//		}
//	}
//}
