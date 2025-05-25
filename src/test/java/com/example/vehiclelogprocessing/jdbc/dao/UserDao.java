package com.example.vehiclelogprocessing.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import com.example.vehiclelogprocessing.jdbc.models.User;

public class UserDao {
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<User> findAll(List<Long> userIds) throws SQLException {
        String placeholders = String.join(",", Collections.nCopies(userIds.size(), "?"));

        String sql = "Select id, first_name, last_name from users where id in (" + placeholders + ")";
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < userIds.size(); i++)
                preparedStatement.setLong(i + 1, userIds.get(i));
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRowToUser(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"));
        return user;
    }
}
