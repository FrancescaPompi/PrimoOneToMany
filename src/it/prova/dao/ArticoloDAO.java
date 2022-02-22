package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class ArticoloDAO {

	public List<Articolo> list() {

		List<Articolo> result = new ArrayList<Articolo>();

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				//STRATEGIA EAGER FETCHING
				ResultSet rs = s
						.executeQuery("select * from articolo a inner join negozio n on n.id=a.negozio_id")) {

			while (rs.next()) {
				Articolo articoloTemp = new Articolo();
				articoloTemp.setNome(rs.getString("NOME"));
				articoloTemp.setMatricola(rs.getString("matricola"));
				articoloTemp.setId(rs.getLong("a.id"));

				Negozio negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("n.id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				articoloTemp.setNegozio(negozioTemp);
				result.add(articoloTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public Articolo selectById(Long idArticoloInput) {

		if (idArticoloInput == null || idArticoloInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a where a.id=?")) {

			ps.setLong(1, idArticoloInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("NOME"));
					result.setMatricola(rs.getString("matricola"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Articolo articoloInput) {

		if (articoloInput.getNegozio() == null || articoloInput.getNegozio().getId() < 1)
			return -1;

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c
						.prepareStatement("INSERT INTO articolo (nome, matricola,negozio_id) VALUES (?, ?, ?)")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getNegozio().getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	// TODO

	public Articolo selectByIdWithJoin(Long idInput) {
		if(idInput == null || idInput == 0)
			return null;
		
		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a inner join negozio n on n.id=a.negozio_id where a.id=?;")) {
			
			ps.setLong(1, idInput);
			try(ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("nome"));
					result.setMatricola(rs.getString("matricola"));
					result.setId(rs.getLong("a.id"));

					Negozio negozioTemp = new Negozio();
					negozioTemp.setId(rs.getLong("n.id"));
					negozioTemp.setNome(rs.getString("nome"));
					negozioTemp.setIndirizzo(rs.getString("indirizzo"));

					result.setNegozio(negozioTemp);
				} else {
					result = null;
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int update(Articolo articoloInput) {
		if(articoloInput == null)
			return 0;
		
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("UPDATE articolo SET nome=?, matricola=?, negozio_id=? WHERE id=?;")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getNegozio().getId());
			ps.setLong(4, articoloInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int delete(Articolo articoloInput) {
		if(articoloInput == null)
			return 0;
		
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM articolo WHERE id=?;")) {

			ps.setLong(1, articoloInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// implementare inoltre
	public List<Articolo> findAllByNegozio(Negozio negozioInput) {
		if(negozioInput == null)
			return null;
		
		List<Articolo> result = new ArrayList<Articolo>();
		Articolo temp = null;
		
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM articolo WHERE negozio_id=?;")) {
			
			ps.setLong(1, negozioInput.getId());
			try(ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					temp = new Articolo();
					temp.setId(rs.getLong("id"));
					temp.setNome(rs.getString("nome"));
					temp.setMatricola(rs.getString("matricola"));
					result.add(temp);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Articolo> findAllByMatricola(String matricolaInput) {
		if(matricolaInput == null) {
			return null;
		}
		
		List<Articolo> result = new ArrayList<Articolo>();
		Articolo temp = null;
		
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM articolo WHERE matricola=?;")) {
			
			ps.setString(1, matricolaInput);
			try(ResultSet rs = ps.executeQuery()) {
				
				while(rs.next()) {
					temp = new Articolo();
					temp.setId(rs.getLong("id"));
					temp.setNome(rs.getString("nome"));
					temp.setMatricola(rs.getString("matricola"));
					result.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Articolo> findAllByIndirizzoNegozio(String indirizzoNegozioInput) {
		if(indirizzoNegozioInput == null) {
			return null;
		}
		
		List<Articolo> result = new ArrayList<Articolo>();
		Articolo temp = null;
		
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a inner join negozio n on n.id=a.negozio_id where n.indirizzo=?;")) {
			
			ps.setString(1, indirizzoNegozioInput);
			try(ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					temp = new Articolo();
					temp.setNome(indirizzoNegozioInput);
					temp.setMatricola(rs.getString("a.matricola"));
					temp.setId(rs.getLong("a.id"));

					Negozio negozioTemp = new Negozio();
					negozioTemp.setId(rs.getLong("n.id"));
					negozioTemp.setNome(rs.getString("nome"));
					negozioTemp.setIndirizzo(rs.getString("indirizzo"));

					temp.setNegozio(negozioTemp);
					
					result.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

}
