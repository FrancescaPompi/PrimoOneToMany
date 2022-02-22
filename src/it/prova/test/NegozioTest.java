package it.prova.test;

import java.util.List;

import it.prova.dao.ArticoloDAO;
import it.prova.dao.NegozioDAO;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioTest {

	public static void main(String[] args) {
		NegozioDAO negozioDAOInstance = new NegozioDAO();
		ArticoloDAO articoloDAOInstance = new ArticoloDAO();

		// ora con i dao posso fare tutte le invocazioni che mi servono
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testInserimentoNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testFindByIdNegozio(negozioDAOInstance);

		testInsertArticolo(negozioDAOInstance, articoloDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testFindByIdArticolo(articoloDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testUpdateNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testDeleteNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testFindAllByIniziali(negozioDAOInstance);

		testSelectByIdWithJoin(articoloDAOInstance);

		testUpdateArticolo(articoloDAOInstance);

		System.out.println(
				"Prima della delete: In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");
		testDeleteArticolo(articoloDAOInstance);
		System.out.println(
				"Dopo la delete: In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testFindAllByNegozio(articoloDAOInstance);

		testFindAllByMatricola(articoloDAOInstance);

		testFindAllByIndirizzoNegozio(articoloDAOInstance);

		testPopulateArticoli(negozioDAOInstance);

		// ESERCIZIO: COMPLETARE DAO E TEST RELATIVI

		// ESERCIZIO SUCCESSIVO
		/*
		 * se io voglio caricare un negozio e contestualmente anche i suoi articoli
		 * dovrò sfruttare il populateArticoli presente dentro negoziodao. Per esempio
		 * Negozio negozioCaricatoDalDb = negozioDAOInstance.selectById...
		 * 
		 * negozioDAOInstance.populateArticoli(negozioCaricatoDalDb);
		 * 
		 * e da qui in poi il negozioCaricatoDalDb.getArticoli() non deve essere più a
		 * size=0 (se ha articoli ovviamente) LAZY FETCHING (poi ve lo spiego)
		 */

	}

	private static void testInserimentoNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testInserimentoNegozio inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testFindByIdNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindByIdNegozio inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		Negozio negozioCheRicercoColDAO = negozioDAOInstance.selectById(primoNegozioDellaLista.getId());
		if (negozioCheRicercoColDAO == null
				|| !negozioCheRicercoColDAO.getNome().equals(primoNegozioDellaLista.getNome()))
			throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdNegozio fine: PASSED.............");
	}

	private static void testUpdateNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testUpdateNegozio inizio.............");
		Negozio negozioDaAggiornare = negozioDAOInstance.list().get(0);
		int quantiNegoziAggiornati = negozioDAOInstance.update(negozioDaAggiornare);
		if (quantiNegoziAggiornati < 1) {
			throw new RuntimeException("testUpdateNegozio : FAILED");
		}
		System.out.println(".......testUpdateNegozio fine: PASSED.............");
	}

	private static void testDeleteNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testDeleteNegozio inizio.............");
		Negozio negozioDaEliminare = negozioDAOInstance.list().get(3);
		int quantiNegoziEliminati = negozioDAOInstance.delete(negozioDaEliminare);
		if (quantiNegoziEliminati < 1) {
			throw new RuntimeException("testDeleteNegozio : FAILED");
		}
		System.out.println(".......testDeleteNegozio fine: PASSED.............");
	}

	public static void testFindAllByIniziali(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindAllByIniziali inizio.............");
		String iniziale = "n";
		List<Negozio> negoziConStessaInizialeInput = negozioDAOInstance.findAllByIniziali(iniziale);
		if (negoziConStessaInizialeInput.size() < 1) {
			throw new RuntimeException("testFindAllByIniziali : FAILED, non ci sono negozi con questa iniziale");
		}
		System.out.println(".......testFindAllByIniziali fine: PASSED.............");
	}

	private static void testInsertArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testInsertArticolo inizio.............");
		// mi serve un negozio esistente
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", primoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		System.out.println(".......testInsertArticolo fine: PASSED.............");
	}

	private static void testFindByIdArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindByIdArticolo inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticolo : FAILED, non ci sono articoli sul DB");

		Articolo primoArticoloDellaLista = elencoArticoliPresenti.get(0);

		Articolo articoloCheRicercoColDAO = articoloDAOInstance.selectById(primoArticoloDellaLista.getId());
		if (articoloCheRicercoColDAO == null
				|| !articoloCheRicercoColDAO.getNome().equals(primoArticoloDellaLista.getNome()))
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdArticolo fine: PASSED.............");
	}

	private static void testSelectByIdWithJoin(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testSelectByIdWithJoin inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testSelectByIdWithJoin : FAILED, non ci sono articoli nel DB");

		Articolo articoloInLista = articoloDAOInstance.list().get(0);

		Articolo articoloCaricatoConJoin = articoloDAOInstance.selectByIdWithJoin(articoloInLista.getId());

		if (articoloCaricatoConJoin == null || articoloCaricatoConJoin.getId() != articoloInLista.getId()
				|| articoloCaricatoConJoin.getNegozio() == null) {
			throw new RuntimeException("testSelectByIdWithJoin : FAILED, gli articoli non corrispondono");
		}
		System.out.println(".......testSelectByIdWithJoin fine: PASSED.............");
	}

	private static void testUpdateArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testUpdateArticolo inizio.............");
		Articolo articoloPerModifica = articoloDAOInstance.list().get(0);
		int quantiArticoliAggiornati = articoloDAOInstance.update(articoloPerModifica);
		if (quantiArticoliAggiornati < 1) {
			throw new RuntimeException("testUpdateArticolo : FAILED, articolo non trovato");
		}
		System.out.println(".......testUpdateArticolo fine: PASSED.............");
	}

	private static void testDeleteArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testDeleteArticolo inizio.............");
		Articolo articoloDaEliminare = articoloDAOInstance.list().get(0);
		int quantiNegoziEliminati = articoloDAOInstance.delete(articoloDaEliminare);
		if (quantiNegoziEliminati < 1 || articoloDaEliminare == null) {
			throw new RuntimeException("testDeleteNegozio : FAILED, non ho trovato articoli");
		}
		System.out.println(".......testDeleteNegozio fine: PASSED.............");
	}

	private static void testFindAllByNegozio(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindAllByNegozio inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non ci sono articoli nel DB");

		Negozio negozioInput = elencoArticoliPresenti.get(0).getNegozio();

		List<Articolo> articoliTrovatiPerNegozioInput = articoloDAOInstance.findAllByNegozio(negozioInput);
		if (articoliTrovatiPerNegozioInput.size() < 1) {
			throw new RuntimeException("testFindAllByNegozio : FAILED, non ci sono articoli per quel negozio");
		}
		System.out.println(".......testFindAllByNegozio fine: PASSED.............");
	}

	private static void testFindAllByMatricola(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindAllByMatricola inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindAllByNegozio : FAILED, non ci sono articoli nel DB");

		String matricolaPerSelezione = "matricola1";
		List<Articolo> articoliSelezionePerMatricola = articoloDAOInstance.findAllByMatricola(matricolaPerSelezione);

		if (articoliSelezionePerMatricola.size() < 1) {
			throw new RuntimeException("testFindAllByMatricola : FAILED, non ci sono articoli con quella matricola");
		}

		System.out.println(".......testFindAllByMatricola fine: PASSED.............");
	}

	private static void testFindAllByIndirizzoNegozio(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindAllByIndirizzoNegozio inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindAllByIndirizzoNegozio : FAILED, non ci sono articoli nel DB");

		String indirizzoNegozioPerSelezione = elencoArticoliPresenti.get(0).getNegozio().getIndirizzo();
		List<Articolo> articoliPerSelezioneIndirizzoNegozio = articoloDAOInstance
				.findAllByIndirizzoNegozio(indirizzoNegozioPerSelezione);

		if (articoliPerSelezioneIndirizzoNegozio.size() < 1) {
			throw new RuntimeException(
					"testFindAllByIndirizzoNegozio : FAILED, non ci sono articoli nel negozio con quell'indirizzo");
		}
		System.out.println(".......testFindAllByIndirizzoNegozio fine: PASSED.............");
	}

	private static void testPopulateArticoli(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testPopulateArticoli inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testPopulateArticoli : FAILED, non ci sono negozi sul DB");

		Negozio negozioCaricatoDalDB = negozioDAOInstance.selectById(elencoNegoziPresenti.get(0).getId());
		negozioDAOInstance.populateArticoli(negozioCaricatoDalDB);
		if (negozioCaricatoDalDB.getArticoli().size() < 1) {
			throw new RuntimeException("testPopulateArticoli : FAILED, la lista articoli per questo negozio è vuota");
		}
		System.out.println(".......testPopulateArticoli fine: PASSED.............");
	}
}
