package com.promoit.otp.dao;

import com.promoit.otp.model.Role;
import com.promoit.otp.model.User;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class UserDao {
    private final DataSource ds;
    public UserDao(DataSource ds) { this.ds = ds; }

    public User create(String login, String passwordHash, Role role) {
        String sql = "INSERT INTO users(login,password_hash,role) VALUES(?,?,?) RETURNING id";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login); ps.setString(2, passwordHash); ps.setString(3, role.name());
            ResultSet rs = ps.executeQuery(); rs.next(); return new User(rs.getLong(1), login, passwordHash, role);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public Optional<User> findByLogin(String login) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE login=?")) {
            ps.setString(1, login); ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(map(rs)) : Optional.empty();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public boolean adminExists() {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users WHERE role='ADMIN'")) {
            ResultSet rs = ps.executeQuery(); rs.next(); return rs.getLong(1) > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public List<User> findNonAdmins() {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE role <> 'ADMIN' ORDER BY id")) {
            ResultSet rs = ps.executeQuery(); List<User> out = new ArrayList<>(); while (rs.next()) out.add(map(rs)); return out;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public void deleteUser(long id) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=? AND role <> 'ADMIN'")) {
            ps.setLong(1, id); ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    private User map(ResultSet rs) throws SQLException { return new User(rs.getLong("id"), rs.getString("login"), rs.getString("password_hash"), Role.valueOf(rs.getString("role"))); }
}
