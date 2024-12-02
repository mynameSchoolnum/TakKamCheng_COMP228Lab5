package Lab5Exercise1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertPlayerAndGame {
    public static JPanel createInsertPlayerAndGamePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> playerComboBox = new JComboBox<>();
        JComboBox<String> gameComboBox = new JComboBox<>();
        JTextField dateField = new JTextField();
        JTextField scoreField = new JTextField();
        JButton insertButton = new JButton("Insert Data");

        try (Connection connection = DatabaseConnection.getConnection()) {
            // input players
            String playerQuery = "SELECT player_id, first_name, last_name FROM TAK_KAM_CHENG_PLAYER";
            try (PreparedStatement ps = connection.prepareStatement(playerQuery);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playerComboBox.addItem(rs.getInt("player_id") + " - " + rs.getString("first_name") + " " + rs.getString("last_name"));
                }
            }

            // input games
            String gameQuery = "SELECT game_id, game_title FROM TAK_KAM_CHENG_GAME";
            try (PreparedStatement ps = connection.prepareStatement(gameQuery);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    gameComboBox.addItem(rs.getInt("game_id") + " - " + rs.getString("game_title"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Failed to load players or games.");
        }

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Player:"), gbc);
        gbc.gridx = 1;
        panel.add(playerComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Select Game:"), gbc);
        gbc.gridx = 1;
        panel.add(gameComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Playing Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Score:"), gbc);
        gbc.gridx = 1;
        panel.add(scoreField, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(insertButton, gbc);

        insertButton.addActionListener((ActionEvent e) -> {
            String player = (String) playerComboBox.getSelectedItem();
            String game = (String) gameComboBox.getSelectedItem();
            String playingDate = dateField.getText();
            String scoreText = scoreField.getText();

            if (player == null || game == null || playingDate.isEmpty() || scoreText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "All fields must be filled.");
                return;
            }

            try {
                int playerId = Integer.parseInt(player.split(" - ")[0]);
                int gameId = Integer.parseInt(game.split(" - ")[0]);
                int score = Integer.parseInt(scoreText);

                try (Connection connection = DatabaseConnection.getConnection()) {
                    String insertSQL = """
                            INSERT INTO TAK_KAM_CHENG_PLAYER_AND_GAME (player_game_id, game_id, player_id, playing_date, score)
                            VALUES (player_game_seq.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)
                            """;

                    try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
                        ps.setInt(1, gameId);
                        ps.setInt(2, playerId);
                        ps.setString(3, playingDate);
                        ps.setInt(4, score);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(panel, "Data inserted successfully!");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Score must be a valid number.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Failed to insert data.");
            }
        });

        return panel;
    }
}
