package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;
	
	private double bestMedia = 0.0;
	private Set<Esame> bestSoluzione = null;
	
	
	public Model() {
		
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		// chiamata alla ricorsione
		Set<Esame> parziale = new HashSet<>();
		
		cerca(parziale, 0, numeroCrediti);
		
		return bestSoluzione;
	}
	
	private void cerca(Set<Esame> parziale, int L, int m) {
		
		/*
		 * Casi terminali:
		 * il primo da aggiungere, per non arrivare inutilmente al fondo, 
		 * è il vincolo sulla somma dei crediti
		 */
		
		int crediti = sommaCrediti(parziale);
		if(crediti > m) {
			return;
		}
			
		if(crediti == m) {
			double media = calcolaMedia(parziale);
			// devo controllare se 'media' è > di tutte le medie incontrate fino ad ora
			if(media > bestMedia) {
				bestSoluzione = new HashSet<>(parziale);
				bestMedia = media;
			}
		}
		
		//sicuramente crediti < m
		
		if(L == esami.size()) {
			return;
		}
		
		
		/*
		 *  Generare il sottoproblemi; esami[L] è da aggiungere o no?
		 *  Provo entrambe le cose!
		 */
		
		// 1. provo ad aggiungerlo
		parziale.add(esami.get(L));
		cerca(parziale, L+1, m);
		parziale.remove(esami.get(L));
		
		// 2. provo a non aggiungerlo
		cerca(parziale, L+1, m);
		
	}


	public double calcolaMedia(Set<Esame> parziale) {
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : parziale) {
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}


	private int sommaCrediti(Set<Esame> parziale) {
		int somma = 0;
		for(Esame e : parziale) {
			somma += e.getCrediti();
		}
		return somma;
	}

}
