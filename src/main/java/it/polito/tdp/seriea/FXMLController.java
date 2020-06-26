package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.AnnateVirtuose;
import it.polito.tdp.seriea.model.AnnoPunti;
import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {

    	txtResult.clear();
    	
    	Team team = boxSquadra.getValue();
    	
    	if(team == null) {
    		txtResult.appendText("Errore: selezionare una squadra.\n");
    		return;
    	}
    	
    	List<AnnoPunti> annipunti = this.model.getPuntiForYear(team);
    	
    	txtResult.appendText(String.format("Ecco i punti di ogni campionato per la squadra %s: \n", 
    											team.getTeam()));
    	
    	for(AnnoPunti ap : annipunti) {
    		txtResult.appendText(ap.toString() + "\n");
    	}
    	
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {

    	txtResult.appendText("\n");
    	
    	Team team = boxSquadra.getValue();
    	
    	if(team == null) {
    		txtResult.appendText("Errore: selezionare una squadra.\n");
    		return;
    	}
    	
    	this.model.creaGrafo();
    	
    	txtResult.appendText(String.format("Grafo creato! [#Vertici %d, #Archi %d] \n", 
    									this.model.getNumberVertex(), this.model.getNumberEdge()));
    	
    	txtResult.appendText("\n");
    	
    	this.model.findBestAnnata();
    	
    	txtResult.appendText(String.format("Migliore annata trovata: %s \n"
    			+ "Differenza dei pesi = %d\n", 
				this.model.getBestAnnata().getDescription(), this.model.getBestDifferenzaPunteggio()));
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {

    	txtResult.appendText("\n");
    	
    	AnnateVirtuose best = this.model.camminoVirtuoso();
    	List<AnnateVirtuose> elencoStagioni = this.model.getStagioniVirtuose();
    		
    	txtResult.appendText(String.format("Il cammino virtuoso comincia nella stagione \n"
    			+ "%s e la squadra per %d anni ha migliorato il suo piazzamento in classifica:\n", 
    						best.getSeason().getDescription(), best.getConsecutivi()));
    	
    	for(AnnateVirtuose av : elencoStagioni) {
    		txtResult.appendText(String.format(" - %s  | %d\n", 
    				av.getSeason().getDescription(), av.getConsecutivi()));
    	}
    	
    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxSquadra.getItems().addAll(this.model.getAllTeams());
	}
}
