package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.db.SerieADAO;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		SerieADAO dao = new SerieADAO();
		Team team = new Team("Milan");
		Map<Integer, List<Match>> risultati = dao.getPartiteAndSeason(team);
		System.out.println(risultati.keySet().size());
		List<Match> elenco2003 = new ArrayList<>(risultati.get(2003));
		System.out.println("\n" + elenco2003.size() + "\n");
		for(Match p : elenco2003) {
			System.out.println(p.toString() + "\n");
		}
	}

}
