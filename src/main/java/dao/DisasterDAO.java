package dao;

import com.disasterresponse.model.Disaster;
import com.disasterresponse.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisasterDAO {

    private static final String INSERT_DISASTER_SQL = "INSERT INTO disaster_reports (location, type, severity, status, comment, reportedTime) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_DISASTERS_SQL = "SELECT * FROM disaster_reports";
    
        public List<Disaster> getLatestDisasters(int limit) throws SQLException {
        List<Disaster> disasters = new ArrayList<>();
        String sql = "SELECT * FROM disaster_reports ORDER BY reportedTime DESC LIMIT ?"; // Get the latest reports based on limit

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, limit); // Set the limit dynamically
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Disaster disaster = new Disaster(
                    resultSet.getString("location"),
                    resultSet.getString("type"),
                    resultSet.getString("severity"),
                    resultSet.getString("status"),
                    resultSet.getString("comment"),
                    resultSet.getString("reportedTime")
                );
                disasters.add(disaster);
            }
        }
        return disasters;
    }

    // Method to save a disaster report to the database
    public void saveDisaster(Disaster disaster) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DISASTER_SQL)) {
            preparedStatement.setString(1, disaster.getLocation());
            preparedStatement.setString(2, disaster.getType());
            preparedStatement.setString(3, disaster.getSeverity());
            preparedStatement.setString(4, disaster.getStatus());
            preparedStatement.setString(5, disaster.getComment());
            preparedStatement.setString(6, disaster.getReportedTime());
            preparedStatement.executeUpdate();
        }
    }

    // Method to fetch all disasters from the database
    public List<Disaster> getAllDisasters() throws SQLException {
        List<Disaster> disasterList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_DISASTERS_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Loop through the result set and create Disaster objects
            while (resultSet.next()) {
                String location = resultSet.getString("location");
                String type = resultSet.getString("type");
                String severity = resultSet.getString("severity");
                String status = resultSet.getString("status");
                String comment = resultSet.getString("comment");
                String reportedTime = resultSet.getString("reportedTime");

                // Create a new Disaster object and add it to the list
                Disaster disaster = new Disaster(location, type, severity, status, comment, reportedTime);
                disasterList.add(disaster);
            }
        }
        return disasterList;
    }
}
