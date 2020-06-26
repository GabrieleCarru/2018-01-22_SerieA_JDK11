package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	private SerieADAO dao;
	private List<Match> partite;
	private List<AnnoPunti> punteggioAnnate;
	private Graph<Season, DefaultWeightedEdge> graph;
	private Map<Season, Integer> idMapSeason;
	private Integer bestDifferenzaPunteggio;
	private Season bestAnnata;
	
	public Model() {
		dao = new SerieADAO();
	}
	
	public List<AnnoPunti> getPuntiForYear(Team team) {
		
		punteggioAnnate = new ArrayList<>();
		idMapSeason = new HashMap<>();
		
		Map<Integer, List<Match>> risultati = new HashMap<>(dao.getPartiteAndSeason(team));
		for(Integer anno : risultati.keySet()) {
			int punteggioAnnata = calcolaPuntiAnnata(team, risultati.get(anno));
			punteggioAnnate.add(new AnnoPunti(anno, punteggioAnnata));
			
			// Aggiungo anche alla mappa
			Season s = new Season(anno, anno-1 + "/" + anno);
			idMapSeason.put(s, punteggioAnnata);
		}
		
		Collections.sort(punteggioAnnate);
		return punteggioAnnate;
	}
	
	public List<AnnoPunti> getPuntiPerAnno(Team team) {
		
		List<AnnoPunti> punteggioAnni = new ArrayList<>();
		List<Integer> anni = dao.getAnniByTeam(team);
		
		for(Integer anno : anni) {
			List<Match> partite = dao.getPartiteByAnno(anno, team);
			//int punteggio = calcolaPuntiAnnata(team, partite);
			int punteggio = 0;
			
			for(Match m : partite) {
				if(m.getFtr().equals("D")) {
					punteggio = punteggio + 1;
				}
				
				if((m.getFtr().equals("A")) && (m.getAwayTeam().equals(team))) {
					punteggio = punteggio + 3;
				}
				
				if((m.getFtr().equals("H")) && (m.getHomeTeam().equals(team))) {
					punteggio = punteggio + 3;
				}
			}
			punteggioAnni.add(new AnnoPunti(anno, punteggio));
		}
		
		return punteggioAnni;
	}
	
//	public Map<Season, Integer> getPuntiAnnata (Team team) {
//		
//		partite = dao.getPartiteByTeam2(team);
//		Map<Season, Integer> result = new HashMap<>();
//		
//		for(Match m : partite) {
//			if(!result.containsKey(m.getSeason())) {
//				int punteggioAnnata = calcolaPuntiAnnata(m.getSeason(), team);
//			}
//		}
//	}
	
	public void creaGrafo() {
		graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
//		List<Season> seasons = new ArrayList<>();
//		
//		for(AnnoPunti ap : punteggioAnnate) {
//			Season s = new Season(ap.getAnno(), ap.getAnno()-1 + "/" + ap.getAnno());
//			if(!seasons.contains(s)) {
//				seasons.add(s);
//			}
//		}
		
		// Aggiungo i vertici 
		Graphs.addAllVertices(this.graph, idMapSeason.keySet());
		
		List<Season> seasons = new ArrayList<>(this.graph.vertexSet());
		
		// Aggiungo gli archi
//		for(int i = 1; i < seasons.size(); i++) {
//			if(idMapSeason.get(seasons.get(i)) > idMapSeason.get(seasons.get(i-1))) {
//				Graphs.addEdge(this.graph, 
//						seasons.get(i), seasons.get(i-1), 
//						(idMapSeason.get(seasons.get(i)) - idMapSeason.get(seasons.get(i-1))));
//			} 
//			
//			if(idMapSeason.get(seasons.get(i)) < idMapSeason.get(seasons.get(i-1))) {
//				Graphs.addEdge(this.graph, 
//						seasons.get(i-1), seasons.get(i), 
//						(idMapSeason.get(seasons.get(i-1)) - idMapSeason.get(seasons.get(i))));
//			}
//		}
		
		for(Season s1 : seasons) {
			for(Season s2 : seasons) {
				if(!s1.equals(s2)) {
					
					if(idMapSeason.get(s1) < idMapSeason.get(s2)) {
						Graphs.addEdge(this.graph, 
								s1, s2, 
								(idMapSeason.get(s1) - idMapSeason.get(s2)));
					} 
					
					if(idMapSeason.get(s1) > idMapSeason.get(s2)) {
						Graphs.addEdge(this.graph, 
								s2, s1, 
								(idMapSeason.get(s2) - idMapSeason.get(s1)));
					}
				}
			}
		}
	}
	
	public void findBestAnnata() {
		
		bestDifferenzaPunteggio = 0;
		bestAnnata = null;
		
		List<Season> seasons = new ArrayList<>(this.graph.vertexSet());
		
		for(int i = 0; i < seasons.size(); i++) {
			List<DefaultWeightedEdge> archiUscenti = new ArrayList<>(this.graph.outgoingEdgesOf(seasons.get(i)));
			List<DefaultWeightedEdge> archiEntranti = new ArrayList<>(this.graph.incomingEdgesOf(seasons.get(i)));
			
			int puntiUscenti = calcolaPuntiArchi(archiUscenti);
			int puntiEntranti = calcolaPuntiArchi(archiEntranti);
			int differenza = puntiEntranti - puntiUscenti;
			
			if(differenza > bestDifferenzaPunteggio) {
				bestDifferenzaPunteggio = differenza;
				bestAnnata = seasons.get(i);
			}
		}
	}
	
	private int calcolaPuntiArchi(List<DefaultWeightedEdge> listaArchi) {
		
		int punteggio = 0;
		
		for(DefaultWeightedEdge e : listaArchi) {
			punteggio += this.graph.getEdgeWeight(e);
		}
		
		return punteggio;
	}

	public int getNumberVertex() {
		return this.graph.vertexSet().size();
	}
	
	public int getNumberEdge() {
		return this.graph.edgeSet().size();
	}
	
	

	public Integer getBestDifferenzaPunteggio() {
		return bestDifferenzaPunteggio;
	}

	public Season getBestAnnata() {
		return bestAnnata;
	}

	private int calcolaPuntiAnnata(Team team, List<Match> list) {
		
		int punteggioFinale = 0;
		
		for(Match match : list) {
			if(match.getFtr().equals("D")) {
				punteggioFinale = punteggioFinale + 1;
			}
			
			if((match.getFtr().equals("A")) && (match.getAwayTeam().equals(team))) {
				punteggioFinale = punteggioFinale + 3;
			}
			
			if((match.getFtr().equals("H")) && (match.getHomeTeam().equals(team))) {
				punteggioFinale = punteggioFinale + 3;
			}
		}
		
		return punteggioFinale;
		
	}
	

	public List<Team> getAllTeams() {
		return dao.listTeams();
	}
	
}
