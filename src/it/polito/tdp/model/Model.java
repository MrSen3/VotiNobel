package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.dao.EsameDAO;

public class Model {

	//esami letti dal db
	private List<Esame> esami;
	
	// gestione della ricorsione
	private List<Esame> best;
	private double media_best;
	
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami=dao.getTuttiEsami();
	}
	
	/*
	 * Trova la combinazione di corsi avente la somma de crediti richiesta che abbia 
	 * la media dei voti massima
	 * @param numeroCrediti
	 * @return l'elenco dei corsi ottimale, oppure {@code null}
	 * alcuna combinazione di corsi che assomma al numero esatto
	 */
	
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		best=new ArrayList<Esame>();
		media_best = 0.0;
		 
		Set<Esame> parziale = new HashSet<Esame>();
		
		cerca(parziale, 0, numeroCrediti);
		
		return best;
	}
	
	private void cerca(Set<Esame> parziale, int L, int m) {
		
		//casi terminali?
		int crediti=sommaCrediti(parziale);
		
		if(crediti>m) {
			return;}
		
		if(crediti==m) {
			//Allora devo valutare la media
			double media = calcolaMedia(parziale);
			
			if(media>media_best) {
				//Questo significa che ho trovato una media  migliore di quella precedente.
				//Sostituisco in best la lista di esami che mi dà una media migliore
				best = new ArrayList<Esame>(parziale);//Come .clone()
				media_best= media;
				return;
				
			}else {
				return;}
		}
		
		//Metto questo dopo così  se ho finito gli esami, ma crediti==m entra nel primo if che è più importante
		if(L==esami.size()) {//L mi dice quanti esami ho già considerato
			return;
		}
		
		//generiamo sotto-problemi
		//esami[L] è da aggiungere o no?
		//Provo a non aggiugerlo
		cerca(parziale, L+1, m);
		//Qui non ha senso fare il backtracking perchè non faccio niente
		
		//provo ad aggiungerlo
		parziale.add(esami.get(L));
		cerca(parziale, L+1, m);
		//Backtracking
		parziale.remove(esami.get(L));
		System.out.println(L+" "+parziale.toString()+"\n");
		
	}

	private double calcolaMedia(Set<Esame> parziale) {
		// TODO Auto-generated method stub
		double media = 0.0;
		int crediti = 0;
		for(Esame e: parziale) {
			media+= e.getVoto()*e.getCrediti();
			crediti += e.getCrediti();
		}
		return media/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		// TODO Auto-generated method stub
		int somma=0;
		for (Esame e: parziale) {
			somma+=e.getCrediti();
		}
		return somma;
	}

}
