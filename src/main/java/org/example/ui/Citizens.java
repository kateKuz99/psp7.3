package org.example.ui;

import org.example.DAO.CitizensDAO;
import org.example.DAO.CityDAO;
import org.example.model.City;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Citizens extends JPanel {
    private CityDAO cityDAO;
    private CitizensDAO citizensDAO;
    private JTextField nameTextField;
    private JTextField languageTextField;
    private JFormattedTextField countTextField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton createButton;
    private JButton editButton;
    private JButton deleteButton;
    private JComboBox<String> citiesComboBox;

    public Citizens() {
        this.cityDAO = new CityDAO();
        this.citizensDAO = new CitizensDAO();
        setSize(800, 600);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        String css = "table {"
                + "    border-collapse: collapse;"
                + "    width: 80%;"
                + "    table-layout: fixed; "
                + "}"
                + ""
                + "th, td {"
                + "    padding: 8px;"
                + "    text-align: left;"
                + "    border-bottom: 1px solid #ddd;"
                + "}"
                + ""
                + "th {"
                + "    background-color: #f2f2f2;"
                + "    color: white;"
                + "}";

        String tableStyle = "<html><head><style>" + css + "</style></head><body><table>";
        String tableEndStyle = "</table></body></html>";

        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>ID</span>" + tableEndStyle);
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Название</span>" + tableEndStyle);
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Язык</span>" + tableEndStyle);
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Город</span>" + tableEndStyle);
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Количество</span>" + tableEndStyle);
        initializeTable();

        // Создание таблицы
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setForeground(Color.BLACK); // Цвет текста
                component.setBackground(Color.YELLOW); // Фоновый цвет
                return component;
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.NORTH);

        // Панель ввода
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Название:");
        nameTextField = new JTextField(20);

        JLabel languageLabel = new JLabel("Язык:");
        languageTextField = new JTextField(20);

        JLabel countLabel = new JLabel("Количество:");
        NumberFormatter formatterForSquare = new NumberFormatter();
        formatterForSquare.setMinimum(0); // Минимальное значение
        formatterForSquare.setMaximum(1000000); // Максимальное значение
        countTextField = new JFormattedTextField(formatterForSquare);
        countTextField.setColumns(10);

        JLabel citiesLabel = new JLabel("Город:");
        citiesComboBox = new JComboBox<>();
        java.util.List<City> cityList = cityDAO.getAll();
        for (City city: cityList) {
            citiesComboBox.addItem(String.valueOf(city.getName()));
        }

        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(languageLabel);
        inputPanel.add(languageTextField);
        inputPanel.add(countLabel);
        inputPanel.add(countTextField);
        inputPanel.add(citiesLabel);
        inputPanel.add(citiesComboBox);

        // Панель кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        createButton = new JButton("<html><span style='color: red; font-size: 16px; '>Создать</span></html>");
        editButton = new JButton("<html><span style='color: red; font-size: 16px;'>Редактировать</span></html>");
        deleteButton = new JButton("<html><span style='color: red; font-size: 16px;'>Удалить</span></html>");

        buttonPanel.add(createButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Добавление компонентов на основное окно
        add(inputPanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.EAST);

        // Обработка событий кнопок
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String language = languageTextField.getText();

                if (!name.isEmpty() &&citiesComboBox.getItemCount()!=0 && !language.isEmpty() && !countTextField.getText().isEmpty()) {
                    int count = ((Number) countTextField.getValue()).intValue();
                    String city = (String) citiesComboBox.getSelectedItem();
                    if (count > 0 && count < 1000000) {
                        City city1 = cityDAO.getByName(city);
                        org.example.model.Citizens citizens = citizensDAO.create(new org.example.model.Citizens(name, language, count, city1));
                        tableModel.addRow(new Object[]{citizens.getId(), citizens.getName(),citizens.getLanguage(), citizens.getCity().getName(), citizens.getCount()});
                        nameTextField.setText("");
                        languageTextField.setText("");
                        countTextField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(Citizens.this, "Данные некорректны", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Citizens.this, "Заполните поля", "Ошибка", JOptionPane.ERROR_MESSAGE);

                }

            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                String name = nameTextField.getText();
                String language = languageTextField.getText();
                if (selectedRow >= 0) {
                    if (!name.isEmpty() && !language.isEmpty() && !countTextField.getText().isEmpty()) {
                        int count = ((Number) countTextField.getValue()).intValue();
                        String city = (String) citiesComboBox.getSelectedItem();
                        if (count > 0 && count < 1000000) {
                            City city1 = cityDAO.getByName(city);
                            org.example.model.Citizens citizens = citizensDAO.update(new org.example.model.Citizens((Integer) tableModel.getValueAt(selectedRow, 0), name, language, city1, count));

                            tableModel.setValueAt(name, selectedRow, 1);
                            tableModel.setValueAt(language, selectedRow, 2);
                            tableModel.setValueAt(city1.getName(), selectedRow, 3);
                            tableModel.setValueAt(count, selectedRow, 4);
                            nameTextField.setText("");
                            languageTextField.setText("");
                            countTextField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(Citizens.this, "Данные некорректны", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Citizens.this, "Заполните поля", "Ошибка", JOptionPane.ERROR_MESSAGE);

                    }
                }
            }

        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    City city =cityDAO.getByName((String) tableModel.getValueAt(selectedRow, 3));
                    citizensDAO.delete(new org.example.model.Citizens((Integer) tableModel.getValueAt(selectedRow, 0), (String) tableModel.getValueAt(selectedRow, 1), (String) tableModel.getValueAt(selectedRow, 2), city, (Integer) tableModel.getValueAt(selectedRow, 4)));
                    tableModel.removeRow(selectedRow);
                    nameTextField.setText("");
                    languageTextField.setText("");
                    countTextField.setText("");
                } else {
                    JOptionPane.showMessageDialog(Citizens.this, "Выберите запись для удаления", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    // Получение данных выбранной строки
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String name = (String) tableModel.getValueAt(selectedRow, 1);
                    String language = (String) tableModel.getValueAt(selectedRow, 2);
                    int count = (int) tableModel.getValueAt(selectedRow, 4);
                    // Установка данных в текстовые поля
                    nameTextField.setText(name);
                    languageTextField.setText(language);
                    countTextField.setValue(count);
                }
            }
        });
    }

    private void initializeTable() {
        java.util.List<org.example.model.Citizens> citizens= citizensDAO.getAll();
        for (org.example.model.Citizens citizens1: citizens)  {
            tableModel.addRow(new Object[]{citizens1.getId(), citizens1.getName(), citizens1.getLanguage(), citizens1.getCity().getName(), citizens1.getCount() });
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        citiesComboBox.removeAllItems();
        java.util.List<org.example.model.Citizens> citizens= citizensDAO.getAll();
        for (org.example.model.Citizens citizens1: citizens)  {
            tableModel.addRow(new Object[]{citizens1.getId(), citizens1.getName(), citizens1.getLanguage(), citizens1.getCity().getName(), citizens1.getCount() });
        }
        List<City> cityList = cityDAO.getAll();
        for (City city: cityList) {
            citiesComboBox.addItem(String.valueOf(city.getName()));
        }

    }
}
