package br.com.senac.pi4.dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.senac.pi4.model.UsuarioDTO;
import br.com.senac.pi4.services.Database;

public class UsuarioDAO {

	public UsuarioDTO selectUsuario(String produtoId) throws Exception {
		// exemplo de select
		Connection conn = null;
		PreparedStatement psta = null;

		UsuarioDTO pg = null;
		Integer pID = null;
		pID = Integer.parseInt(produtoId);
		try {
			conn = Database.get().conn();
			psta = conn.prepareStatement("select * from Usuario where id = ?");
			psta.setInt(1, pID);

			//
			ResultSet rs = psta.executeQuery();

			while (rs.next()) {
				pg = new UsuarioDTO();
				pg.setNome(rs.getString("nome"));
				pg.setSenha(rs.getString("senha"));
				pg.setId(rs.getLong("id"));
				pg.setEmail(rs.getString("email"));
				pg.setFoto(rs.getBlob("foto"));

			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (psta != null)
				psta.close();
			if (conn != null)
				conn.close();
		}
		return pg;
	}

	public byte[] selectImage(String usuarioId) throws Exception {
		// exemplo de select
		Connection conn = null;
		PreparedStatement psta = null;

		UsuarioDTO pg = null;
		Integer pID = null;
		pID = Integer.parseInt(usuarioId);
		byte[] fileBytes = null;
		try {
			conn = Database.get().conn();
			psta = conn.prepareStatement("select * from Usuario where id = ?");
			psta.setInt(1, pID);

			//
			ResultSet rs = psta.executeQuery();

			while (rs.next()) {
				fileBytes = rs.getBytes("imagem");

			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (psta != null)
				psta.close();
			if (conn != null)
				conn.close();
		}
		return fileBytes;
	}

	public List<UsuarioDTO> selectAllUsuario() throws Exception {
		// exemplo de select
		Connection conn = null;
		PreparedStatement psta = null;
		List<UsuarioDTO> listPg = new ArrayList<UsuarioDTO>();

		try {
			conn = Database.get().conn();
			psta = conn.prepareStatement("select * from Usuario");

			ResultSet rs = psta.executeQuery();

			while (rs.next()) {
				UsuarioDTO pg = new UsuarioDTO();
				pg.setNome(rs.getString("nome"));
				pg.setSenha(rs.getString("senha"));
				pg.setId(rs.getLong("id"));
				pg.setEmail(rs.getString("email"));
				pg.setFoto(rs.getBlob("foto"));

				listPg.add(pg);
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (psta != null)
				psta.close();
			if (conn != null)
				conn.close();
		}
		return listPg;
	}


	@GET
	@Path("/image/{param}")
	@Produces("image/jpg")
	public Response getUsuarioImage(@PathParam("param") String usuarioId) {

		byte[] image = null;

		try {
			image = selectImage(usuarioId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.ok(new ByteArrayInputStream(image)).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD").build();

	}

	public UsuarioDTO save(UsuarioDTO usuario) throws Exception {

		Connection conn = null;
		PreparedStatement psta = null;
		try {
			
			String email = null;
			final Iterator<UsuarioDTO> usuarioIterator = selectAllUsuario().iterator();
			
			while(usuarioIterator.hasNext()) {
				UsuarioDTO usuarioDTOs = usuarioIterator.next();
				email = usuarioDTOs.getEmail();
				if(usuario.getEmail().equalsIgnoreCase(email)) {
					System.out.println("E-mail já existe. Cadastre outro e-mail");
					return null;
				}else if (usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty()) {
					System.out.println("E-mail e senha são obrigatórios.");
					return null;
				}
			}
			
			conn = Database.get().conn();
			psta = conn.prepareStatement("INSERT INTO Usuario"
					+  "(nome, senha, email) VALUES"
					+  "(?,?,?)");
			
			psta.setString(1, usuario.getNome());
			psta.setString(2, usuario.getSenha());
			psta.setString(3, usuario.getEmail());

			psta.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (psta != null)
				psta.close();
			if (conn != null)
				conn.close();
		}
		return usuario;
	}
	
	public UsuarioDTO login(UsuarioDTO usuarioDTO) throws Exception {
		
		String email = null;
		String senha = null;
		final Iterator<UsuarioDTO> usuarioIterator = selectAllUsuario().iterator();
		
		while(usuarioIterator.hasNext()) {
			UsuarioDTO usuarioDTOs = usuarioIterator.next();
			email = usuarioDTOs.getEmail();
			senha = usuarioDTOs.getSenha();
			
			if(usuarioDTO.getEmail().equalsIgnoreCase(email) && usuarioDTO.getSenha().equalsIgnoreCase(senha)) {
				return usuarioDTOs;
			}
			
		}	
		return null;
		
	}

}