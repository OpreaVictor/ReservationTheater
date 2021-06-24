package service;
import service.components.DataLabelFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;
import dao.*;

public class AdminService extends JFrame{
    private JFrame frame;
    private JPanel checkReservationPanel;
    private JTextField dateField;
    private JButton submitButton;
    public AdminService() {
        createFrame();
        createCheckAppoimentsReservation();
        frame.setVisible(true);
        addSubmitButton();
    }
    private void createFrame() {
        frame = new JFrame("Theater  Reservation");
        Image icon = null;

        try {
            icon = ImageIO.read(new File("image/download.jfif"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setIconImage(icon);
        frame.setSize(new Dimension(500, 500));
        frame.setFont(new Font("Benne", Font.PLAIN, 12));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);



    }
    private void createCheckAppoimentsReservation() {
        checkReservationPanel = new JPanel();
        checkReservationPanel.setBackground(Color.white);
        checkReservationPanel.setLayout(new BoxLayout(checkReservationPanel, BoxLayout.Y_AXIS));
        addTitle(checkReservationPanel, "Check Reservation");
        addDatePicker(checkReservationPanel);
        checkReservationPanel.add(Box.createRigidArea(new Dimension(0, 126)));
        submitButton = getSubmitButton(checkReservationPanel, "Check Reservation");
        frame.add(checkReservationPanel);
    }
    private void addSubmitButton() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String popUpTitle = "Check Reservation";
                String textToDisplay;
                Icon icon;
                if (dateField.getText().isEmpty()) {
                    icon = new ImageIcon("image/download.jfif");
                    textToDisplay = "Please select Date";
                    JOptionPane.showMessageDialog(checkReservationPanel, textToDisplay, popUpTitle, JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    icon = new ImageIcon("image/download.jfif");
                    Connection conn = DAOConnection.getConnection();
                    String getEventQuery = "Select name, hour from event where date = '" + dateField.getText() + "' order by hour";
                    ResultSet resultSet = DbQueries.selectQuery(conn, getEventQuery);
                    StringBuilder event = new StringBuilder();
                    try {
                        while (resultSet.next()) {
                            event.append("<tr>" +
                                    "<td>").append(resultSet.getString(1)).append("</td>" +
                                    "<td>").append(resultSet.getTime(2)).append("</td>" +
                                    "</tr>");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    DAOConnection.closeConnection();

                    if (event.toString().isEmpty()) {
                        textToDisplay = "No reservation were found on '" + dateField.getText() + "'";
                        JOptionPane.showMessageDialog(checkReservationPanel, textToDisplay, popUpTitle, JOptionPane.INFORMATION_MESSAGE, icon);
                    } else {
                        textToDisplay = "<html><table border=2><tr><th>name</th><th>hour</th></tr>" + event.toString() + "</table><br /></html>";
                        BorderLayout border = new BorderLayout();
                        JPanel panel = new JPanel();
                        panel.setLayout(border);
                        panel.add(new JLabel(textToDisplay), BorderLayout.NORTH);
                        JOptionPane.showMessageDialog(checkReservationPanel, panel, popUpTitle, JOptionPane.INFORMATION_MESSAGE, icon);
                    }
                }

            }
        });
    }
    private void addTitle(JPanel panel, String titleText) {
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Benne", Font.BOLD, 18));
        titleLabel.setForeground(new Color(55, 94, 212));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.white);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(titlePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
    }
    private void addDatePicker(JPanel panel) {
        JPanel datePanel = getFieldPanel("Select Date:          ");
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanelImpl, new DataLabelFormatter());
        dateField = datePicker.getJFormattedTextField();
        dateField.setFont(new Font("Benne", Font.PLAIN, 12));
        dateField.setMargin(new Insets(0, 5, 0, 5));
        datePanel.add(datePicker);
        datePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(datePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    private JPanel getFieldPanel(String labelText) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Benne", Font.PLAIN, 12));
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.setBackground(checkReservationPanel.getBackground());
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));

        return panel;
    }
    private JButton getSubmitButton(JPanel panel, String buttonText) {
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton(buttonText);
        submitButton.setFont(new Font("Benne", Font.BOLD, 12));
        submitButton.setForeground(new Color(57, 196, 96));
        submitButton.setBackground(new Color(55, 212, 199));
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.add(submitButton);
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        return submitButton;
    }


}
