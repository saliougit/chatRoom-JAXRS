package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import models.Message;
import models.Room;
import models.User;

public class ChatClientGUI extends JFrame {
    private final RestClient client;
    private User currentUser;
    private Room currentRoom;
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JList<Room> roomList;
    private final DefaultListModel<Room> roomListModel;
    private Timer messagePollingTimer;
    private final Color PRIMARY_COLOR = new Color(70, 130, 180); // Steel Blue
    private final Color ACCENT_COLOR = new Color(135, 206, 235); // Sky Blue
    private final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14);

    public ChatClientGUI() {
        client = new RestClient();
        
        // Configuration de la fenêtre
        setTitle("ChatRoom JAX-RS Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Création des composants
        roomListModel = new DefaultListModel<>();
        roomList = new JList<>(roomListModel);
        roomList.setFont(MAIN_FONT);
        roomList.setFixedCellHeight(30);
        roomList.setBackground(new Color(240, 240, 240));
        roomList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(MAIN_FONT);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        messageField = new JTextField();
        messageField.setFont(MAIN_FONT);
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Layout
        setLayout(new BorderLayout(10, 10));

        // Panel de gauche pour la liste des salons
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel roomsLabel = new JLabel("Salons disponibles");
        roomsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roomsLabel.setForeground(PRIMARY_COLOR);
        leftPanel.add(roomsLabel, BorderLayout.NORTH);
        
        JScrollPane roomScrollPane = new JScrollPane(roomList);
        roomScrollPane.setPreferredSize(new Dimension(200, 0));
        leftPanel.add(roomScrollPane, BorderLayout.CENTER);

        JButton createRoomButton = new JButton("Nouveau Salon");
        styleButton(createRoomButton);
        leftPanel.add(createRoomButton, BorderLayout.SOUTH);
        
        add(leftPanel, BorderLayout.WEST);

        // Panel central pour le chat
        JPanel chatPanel = new JPanel(new BorderLayout(5, 5));
        chatPanel.setBackground(Color.WHITE);
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Zone de messages
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        
        // Zone de saisie
        JPanel messagePanel = new JPanel(new BorderLayout(5, 0));
        messagePanel.setBackground(Color.WHITE);
        messageField.setPreferredSize(new Dimension(0, 40));
        messagePanel.add(messageField, BorderLayout.CENTER);
        
        JButton sendButton = new JButton("Envoyer");
        styleButton(sendButton);
        messagePanel.add(sendButton, BorderLayout.EAST);
        
        chatPanel.add(messagePanel, BorderLayout.SOUTH);
        add(chatPanel, BorderLayout.CENTER);

        // Event Listeners
        createRoomButton.addActionListener(e -> createRoom());
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Room selected = roomList.getSelectedValue();
                if (selected != null) {
                    joinRoom(selected);
                }
            }
        });

        // Demander le nom d'utilisateur et se connecter
        UIManager.put("OptionPane.messageFont", MAIN_FONT);
        UIManager.put("OptionPane.buttonFont", MAIN_FONT);
        String username = JOptionPane.showInputDialog(this, "Entrez votre nom :");
        if (username != null && !username.trim().isEmpty()) {
            currentUser = client.createUser(username);
            setTitle("ChatRoom - " + username);
            startPolling();
        } else {
            System.exit(0);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(MAIN_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    private void startPolling() {
        // Polling des salons toutes les 5 secondes
        Timer roomPollingTimer = new Timer(5000, e -> refreshRooms());
        roomPollingTimer.start();

        // Polling des messages toutes les secondes
        messagePollingTimer = new Timer(1000, e -> {
            if (currentRoom != null) {
                refreshMessages();
            }
        });
        messagePollingTimer.start();

        // Rafraîchir immédiatement
        refreshRooms();
    }

    private void refreshRooms() {
        List<Room> rooms = client.getAllRooms();
        roomListModel.clear();
        for (Room room : rooms) {
            roomListModel.addElement(room);
        }
    }

    private void refreshMessages() {
        if (currentRoom != null) {
            List<Message> messages = client.getRoomMessages(currentRoom.getId());
            StringBuilder chat = new StringBuilder();
            for (Message message : messages) {
                User user = client.getUser(message.getUserId());
                String username = user != null ? user.getUsername() : "Inconnu";
                chat.append(String.format(">> %s : %s%n", username, message.getContent()));
            }
            chatArea.setText(chat.toString());
            // Scroll automatique vers le bas
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }

    private void createRoom() {
        String roomName = JOptionPane.showInputDialog(this, "Nom du salon :");
        if (roomName != null && !roomName.trim().isEmpty()) {
            // Formatage du nom du salon
            roomName = formatRoomName(roomName);
            client.createRoom(roomName);
            refreshRooms();
        }
    }

    private String formatRoomName(String name) {
        // Capitalise la première lettre et met le reste en minuscules
        if (name == null || name.isEmpty()) return name;
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    private void joinRoom(Room room) {
        currentRoom = room;
        setTitle("ChatRoom - " + currentUser.getUsername() + " - Salon: " + room.getName());
        chatArea.setText("");
        refreshMessages();
    }

    private void sendMessage() {
        String content = messageField.getText().trim();
        if (!content.isEmpty() && currentRoom != null) {
            client.sendMessage(content, currentUser.getId(), currentRoom.getId());
            messageField.setText("");
            refreshMessages();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
