package org.Model.dao.impl;

import org.Model.dao.DepartamentDao;
import org.Model.entities.Departament;
import org.Model.exception.DaoException;
import org.database.Db;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepartamentJDBC implements DepartamentDao {

    @Override
    public void insert(Departament obj) {
        if (obj != null && obj.getName() != null && !obj.getName().isEmpty()) {
            try (Connection conn = Db.getConnectionDb()) {
                String sql = "INSERT INTO public.department " +
                        "(\"name\")\n" +
                        "VALUES(?)";
                conn.setAutoCommit(false);
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, obj.getName());
                    int rows = ps.executeUpdate();
                    if (rows == 0) {
                        throw new DaoException("Nenhuma linha foi inserida");
                    }
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) obj.setId(rs.getInt(1));
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    throw new DaoException("Não foi possivel inserir o registro: "+e.getMessage());
                }
                conn.commit();
            } catch (Exception e) {
                throw new DaoException(e.getMessage());
            }
        } else   throw new IllegalArgumentException("Departamento inválido ou ID ausente.");
    }
    @Override
    public void update(Departament obj) {
        if (obj != null && obj.getId() != null && obj.getId() > 0) {
            String sql = "UPDATE department SET name = ? WHERE id = ?";
            try (Connection conn = Db.getConnectionDb()) {
                conn.setAutoCommit(false);
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, obj.getName());
                    ps.setInt(2, obj.getId());
                    int rows = ps.executeUpdate();
                    if (rows == 0) {
                        throw new DaoException("Nenhuma linha foi atualizada.");
                    }
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw new DaoException("Erro ao atualizar departamento: " + e.getMessage());
                }
            } catch (Exception e) {
                throw new DaoException("Erro na conexão com o banco: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Departamento inválido ou ID ausente.");
        }
    }
    @Override
    public void delete(Integer id) {
        if(id != null && id>0){
            try(Connection conn = Db.getConnectionDb()) {
                conn.setAutoCommit(false);
                String sql = "delete department where id = ?";
                try(PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int rows = ps.executeUpdate();
                    if (rows == 0){
                        throw new DaoException("Nenhum registro encontrado para deleção");
                    }
                } catch (Exception e) {
                    conn.rollback();
                    throw new DaoException("Não foi possivel deletar o registro"+e.getMessage());
                }
                conn.commit();
            } catch (Exception e) {
                throw new DaoException("Erro: "+ e.getMessage());
            }
        }else throw new IllegalArgumentException("Id invalido");
    }
    @Override
    public Departament findById(Integer id) {
        if (id != null && id > 0) {
            try (Connection conn = Db.getConnectionDb()) {
                String sql = "select * from department d where id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Departament dp = new Departament();
                            dp.setId(rs.getInt("id"));
                            dp.setName(rs.getString("name"));
                            return dp;
                        }

                    }

                }
            } catch (Exception e) {
                throw new DaoException(e.getMessage());
            }
        } else throw new IllegalArgumentException("Departamento invalido ou vazio");
        return null;

    }
    @Override
    public List<Departament> findAll() {
        List<Departament> departamentList = new ArrayList<>();
        try(Connection conn = Db.getConnectionDb()) {
            String sql="SELECT * FROM department ORDER BY id";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                try(ResultSet rs = ps.executeQuery()) {
                    while (rs.next()){
                        departamentList.add(new Departament(
                                rs.getInt("id"),
                                rs.getString("name")));
                    }
                }
            } catch (Exception e) {
                throw new DaoException(e.getMessage());
            }
        } catch (Exception e) {
            throw new DaoException("Sem conexão com o banco " + e.getMessage());
        }
        return Collections.unmodifiableList(departamentList);
    }
}
