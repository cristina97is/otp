package com.promoit.otp.dao;

import com.promoit.otp.model.OtpConfig;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;

@Repository
public class OtpConfigDao {
    private final DataSource ds;
    public OtpConfigDao(DataSource ds) { this.ds = ds; }
    public OtpConfig get() {
        try (Connection c = ds.getConnection(); Statement st = c.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT code_length, ttl_seconds FROM otp_config WHERE id=1");
            if (!rs.next()) return new OtpConfig(6,300);
            return new OtpConfig(rs.getInt(1), rs.getInt(2));
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public void update(int length, int ttl) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE otp_config SET code_length=?, ttl_seconds=? WHERE id=1")) {
            ps.setInt(1,length); ps.setInt(2,ttl); ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
