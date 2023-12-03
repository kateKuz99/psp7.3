package org.example.ui;

import org.example.DAO.CitizensDAO;
import org.example.DAO.CityDAO;
import org.example.model.Citizens;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FilterPanel extends JPanel {
    private CityDAO cityDAO;
    private CitizensDAO citizensDAO;
    private JTextField languageTextField;
    private JTextField cityTextField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton filterButton;

    public FilterPanel() {
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
        inputPanel.setLayout(new GridLayout(2, 2));

        JLabel languageLabel = new JLabel("Язык:");
        languageTextField = new JTextField(20);

        JLabel cityLabel = new JLabel("Город:");
        cityTextField = new JTextField(20);


        inputPanel.add(languageLabel);
        inputPanel.add(languageTextField);
        inputPanel.add(cityLabel);
        inputPanel.add(cityTextField);

        filterButton = new JButton("Поиск");
        // Добавление компонентов на основное окно
        add(inputPanel, BorderLayout.NORTH);
        add(filterButton, BorderLayout.SOUTH);
        // Обработка событий кнопок
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String language = languageTextField.getText();
                String city = cityTextField.getText();
                if (!language.isEmpty() && !city.isEmpty()) {
                    java.util.List<Citizens> citizens = citizensDAO.searchCitizensByLanguageAndCity(language, city);
                    initializeTable(citizens);
                } else {
                    JOptionPane.showMessageDialog(FilterPanel.this, "Заполните поля", "Ошибка", JOptionPane.ERROR_MESSAGE);

                }
            }
        });


    }

    private void initializeTable(List<Citizens> citizens) {
        tableModel.setRowCount(0);
        for (Citizens citizens1 : citizens) {
            tableModel.addRow(new Object[]{citizens1.getId(), citizens1.getName(), citizens1.getLanguage(), citizens1.getCity().getName(), citizens1.getCount()});
        }
    }
}
