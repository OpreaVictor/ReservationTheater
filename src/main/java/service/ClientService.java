package service;

import dao.DAOConnection;
import dao.DbQueries;
import service.components.DataLabelFormatter;
import helpers.DateTimeHelper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class ClientService extends JFrame {
    private String[] workingHours = {" -- Please choose an hour --", "08:00","09:00", "10:00", " 11:00", " 12:00", " 13:00", " 14:00", " 15:00", " 16:00","17:00", "18:00","19:00","20:00","21:00"};
    private JFrame frame;
    private JPanel createAppointmentPanel;
    private JTextField nameField;
    private JTextField dateField;
    private JDatePickerImpl datePicker;
    private JComboBox timeSelect;
    private JButton submitAppointmentButton;

    public ClientService() {
        createFrame();
        addCreatePanel();
        frame.setVisible(true);
        submitButtonAction();
        addDatePickerAction();

    }
    private void createFrame() {
        frame = new JFrame("Theater Reservation");
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

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    private void addCreatePanel() {
        createAppointmentPanel = new JPanel();
        createAppointmentPanel.setBackground(Color.white);
        createAppointmentPanel.setLayout(new BoxLayout(createAppointmentPanel, BoxLayout.Y_AXIS));
        addTitle(createAppointmentPanel, "Create a New Reservation for Theater");
        addClientNameField();
        addDatePicker(createAppointmentPanel);
        addTimeSelect();
        submitAppointmentButton = getSubmitButton(createAppointmentPanel, "Create Reservation for Theater");
        frame.add(createAppointmentPanel);
    }
    private void submitButtonAction(){
        submitAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textToDispaly, popUpTitle;
                Icon icon;

                if(nameField.getText().isEmpty()|| dateField.getText().isEmpty() || timeSelect.getSelectedItem().equals(timeSelect.getItemAt(0))){
                    textToDispaly = "Complete all the fields and try again!";
                    popUpTitle = "Registration Error";
                }else{
                    Connection conn = DAOConnection.getConnection();
                    String insertEventQuery = "insert into event (name, date, hour) values ( '"+nameField.getText()+"', '" +dateField.getText()+ "" +
                            "', '" +String.valueOf(timeSelect.getSelectedItem()).trim()+"');";
                    DbQueries.insertQuery(conn, insertEventQuery);
                    DAOConnection.closeConnection();
                    textToDispaly = "Success! Your reservation was successfully complete!";
                    popUpTitle = "New Reservation";
                }
                icon = new ImageIcon("image/download.jfif");
                JOptionPane.showMessageDialog(createAppointmentPanel, textToDispaly, popUpTitle, JOptionPane.INFORMATION_MESSAGE, icon);


            }
        });
    }
    private void addDatePickerAction() {
        datePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn = DAOConnection.getConnection();
                String getAvailableHoursQuery = "Select hour from event where date = '" + dateField.getText() + "'";
                ResultSet res = DbQueries.selectQuery(conn, getAvailableHoursQuery);
                String[] hoursToShow = getAvailableHours(res);
                timeSelect.removeAllItems();

                for (String item:
                        hoursToShow) {
                    timeSelect.addItem(item);
                }

                timeSelect.setEnabled(true);
                DAOConnection.closeConnection();
            }
        });
    }
    private String[] getAvailableHours(ResultSet dataFromDb) {
        List<String> hoursToReturn = new ArrayList<>();
        hoursToReturn.addAll(Arrays.asList(workingHours));
        List<String> dbEntries = DateTimeHelper.getTimeAsList(dataFromDb);
        String[] arrayToReturn;

        for (String entry :
                dbEntries) {
            hoursToReturn.remove(entry);
        }

        arrayToReturn = new String[hoursToReturn.size()];

        return hoursToReturn.toArray(arrayToReturn);
    }
    private void addTitle(JPanel panel, String titleText) {
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Benne", Font.BOLD, 17));
        titleLabel.setForeground(new Color(67, 114, 205));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.white);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(titlePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
    }
    private void addClientNameField() {
        JPanel namePanel = getFieldPanel("Enter your name: ");
        nameField = new JTextField();
        nameField.setFont(new Font("Benne", Font.PLAIN, 13));
        nameField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        nameField.setMargin(new Insets(4, 7, 5, 7));
        nameField.setMaximumSize(new Dimension(300, 30));
        namePanel.add(Box.createRigidArea(new Dimension(2, 0)));
        namePanel.add(nameField);
        namePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        createAppointmentPanel.add(namePanel);
        createAppointmentPanel.add(Box.createRigidArea((new Dimension(0, 10))));
    }
    private JPanel getFieldPanel(String labelText) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Benne", Font.PLAIN, 12));
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.setBackground(createAppointmentPanel.getBackground());
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));

        return panel;
    }
    private void addDatePicker(JPanel panel) {
        JPanel datePanel = getFieldPanel("Select Date:          ");
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(model, properties);
        datePicker = new JDatePickerImpl(datePanelImpl, new DataLabelFormatter());
        dateField = datePicker.getJFormattedTextField();
        dateField.setFont(new Font("Benne", Font.PLAIN, 12));
        dateField.setMargin(new Insets(0, 5, 0, 5));
        datePanel.add(datePicker);
        datePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(datePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    private void addTimeSelect() {
        JPanel timePanel = getFieldPanel("Select Hour:          ");
        timeSelect = new JComboBox(workingHours);
        timeSelect.setFont(new Font("Benne", Font.PLAIN, 12));
        timeSelect.setSelectedIndex(0);
        timeSelect.setMaximumSize(new Dimension(300, 50));
        timePanel.add(timeSelect);
        timePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        createAppointmentPanel.add(timePanel);
        createAppointmentPanel.add(Box.createRigidArea(new Dimension(0, 70)));
        timeSelect.setEnabled(false);
    }
    private JButton getSubmitButton(JPanel panel, String buttonText) {
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton(buttonText);
        submitButton.setFont(new Font("Benne", Font.BOLD, 12));
        submitButton.setForeground(new Color(203, 38, 213));
        submitButton.setBackground(new Color(65, 131, 226));
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.add(submitButton);
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        return submitButton;
    }


}