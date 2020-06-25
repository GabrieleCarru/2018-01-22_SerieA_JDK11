package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	private SerieADAO dao;
	
	public Model() {
		dao = new SerieADAO();
	}
	
	public List<AnnoPunti> getPuntiForYear(Team team) {
		
		List<AnnoPunti> result = new ArrayList<>();
		
		Map<Integer, List<Match>> risultati = new HashMap<>(dao.getPartiteAndSeason(team));
		for(Integer anno : risultati.keySet()) {
			int punteggioAnnata = calcolaPuntiAnnata(team, risultati.get(anno));
			result.add(new AnnoPunti(anno, punteggioAnnata));
		}
		
		Collections.sort(result);
		return result;
	}
	
	public void getPuntiPerAnno(Team team) {
		
		List<Integer> anni = dao.getAnniByTeam(team);
		for(Integer anno : anni) {
			List<Match> partite = dao.getPartiteByAnno(anno, team);
		}
		
	}
	
	private int calcolaPuntiAnnata(Team team, List<Match> list) {
		
		int punteggioFinale = 0;
		
		for(Match match : list) {
			if(match.getFtr().equals("D")) {
				punteggioFinale += punteggioFinale + 1;
			}
			
			if((match.getFtr().equals("A")) && (match.getAwayTeam().equals(team))) {
				punteggioFinale += punteggioFinale + 3;
			}
			
			if((match.getFtr().equals("H")) && (match.getHomeTeam().equals(team))) {
				punteggioFinale += punteggioFinale + 3;
			}
		}
		
		return punteggioFinale;
		
	}

	public List<Team> getAllTeams() {
		return dao.listTeams();
	}
	
}
