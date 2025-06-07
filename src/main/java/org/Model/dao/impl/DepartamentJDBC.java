package org.Model.dao.impl;

import org.Model.dao.DepartamentDao;
import org.Model.entities.Departament;
import org.Model.exception.DaoException;
import org.database.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DepartamentJDBC implements DepartamentDao {

    @Override
    public void insert(Departament obj) {
        try(Connection conn = Db.getConnectionDb()) {
            String sql = "SELECT  \"?\"\n" +
                    "FROM public.department;";
            conn.setAutoCommit(false);
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, obj.getName());
                int rowns = ps.executeUpdate();

            } catch (SQLException e) {
                conn.rollback();
                throw new DaoException(e.getMessage());
            }
            conn.commit();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void update(Departament obj) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Departament findById(Integer id) {
        return null;
    }

    @Override
    public List<Departament> findAll() {
        return List.of();
    }
}
