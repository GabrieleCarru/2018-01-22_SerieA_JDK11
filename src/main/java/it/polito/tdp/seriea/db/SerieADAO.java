package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<Integer, List<Match>> getPartiteAndSeason(Team team) {
		
		String sql = "select `Season` as year, `HomeTeam` as h, `AwayTeam` as a, `FTR` as winner " + 
				"from matches " + 
				"where `HomeTeam` = ? or `AwayTeam` = ? " + 
				"order by `Season` "; 
		
		Map<Integer, List<Match>> result = new HashMap<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(!result.containsKey(rs.getInt("year"))) {
					// La mappa non contiene ancora l'anno preso in esame
					List<Match> partite = new ArrayList<>();
					Team ht = new Team(rs.getString("h"));
					Team at = new Team(rs.getString("a"));
					Match m = new Match(rs.getInt("year"), ht, at, rs.getString("winner")); 
					partite.add(m);
					result.put(rs.getInt("year"), partite);
					
				} else {
					// Contiente già l'anno preso in esame
					Team ht = new Team(rs.getString("h"));
					Team at = new Team(rs.getString("a"));
					Match m = new Match(rs.getInt("year"), ht, at, rs.getString("winner"));
					//result.get(rs.getInt("year")).add(m);
					List<Match> lista = new ArrayList<>(result.get(rs.getInt("year")));
					lista.add(m);
					result.remove(rs.getInt("year"));
					result.put(rs.getInt("year"), lista);
				}
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Integer> getAnniByTeam(Team team) {
		
		String sql = "select distinct(`Season`) as year " + 
				"from matches " + 
				"where `HomeTeam` = ? or `AwayTeam` = ? ";
		
		List<Integer> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getInt("year"));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Match> getPartiteByAnno(Integer anno, Team team) {
		
		String sql = "select `Season` as anno, `HomeTeam` as h, " + 
				"`AwayTeam` as a, `FTR` as winner " + 
				"from matches " + 
				"where `Season` = ? " + 
				"and (`HomeTeam` = ? or `AwayTeam` = ? "; 
		
		List<Match> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, team.getTeam());
			st.setString(3, team.getTeam());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {		
				Match m = new Match(anno, 
						new Team(rs.getString("h")), 
						new Team(rs.getString("a")), 
						rs.getString("winner"));
				result.add(m);
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<Match> getPartiteByTeam2(Team team) {
		
		String sql = "select `match_id`, `Season`, `Div`, `Date`, `HomeTeam`, " + 
				"`AwayTeam`, `FTHG`, `FTAG`, `FTR` as winner " + 
				"from matches " + 
				"where `HomeTeam` = ? or `AwayTeam` = ? ";
		
		List<Match> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(new Match(rs.getInt("match_id"), 
								new Season(rs.getInt("Season"), (rs.getInt("Season") -1) + "/" + rs.getInt("Season")), 
								rs.getString("Div"), 
								rs.getDate("Date").toLocalDate(), 
								new Team(rs.getString("HomeTeam")), 
								new Team(rs.getString("AwayTeam")), 
								rs.getInt("FTHG"), 
								rs.getInt("FTAG"), 
								rs.getString("FTR")));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}

