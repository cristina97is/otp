package com.promoit.otp.dao;

import com.promoit.otp.model.OtpStatus;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class OtpDao {
    private final DataSource ds;
    public OtpDao(DataSource ds) { this.ds = ds; }
    public void create(long userId, String operationId, String code, LocalDateTime expiresAt) {
        String sql = "INSERT INTO otp_codes(user_id,operation_id,code,status,expires_at) VALUES(?,?,?,?,?)";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1,userId); ps.setString(2,operationId); ps.setString(3,code); ps.setString(4,OtpStatus.ACTIVE.name()); ps.setTimestamp(5,Timestamp.valueOf(expiresAt)); ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public Optional<Long> findActiveId(long userId, String operationId, String code) {
        String sql = "SELECT id FROM otp_codes WHERE user_id=? AND operation_id=? AND code=? AND status='ACTIVE' AND expires_at > NOW() ORDER BY id DESC LIMIT 1";
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1,userId); ps.setString(2,operationId); ps.setString(3,code); ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(rs.getLong(1)) : Optional.empty();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public void markUsed(long id) { updateStatus(id, OtpStatus.USED); }
    public int expireOldCodes() {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE otp_codes SET status='EXPIRED' WHERE status='ACTIVE' AND expires_at <= NOW()")) {
            return ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    private void updateStatus(long id, OtpStatus status) {
        try (Connection c = ds.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE otp_codes SET status=? WHERE id=?")) {
            ps.setString(1,status.name()); ps.setLong(2,id); ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
