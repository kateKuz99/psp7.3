package org.example.ui;

import org.example.DAO.CityDAO;
import org.example.model.City;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FilterPanel2 extends JPanel {
    private CityDAO cityDAO;
    private JTextField citizensTextField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton filterButton;

    public FilterPanel2() {
        this.cityDAO = new CityDAO();
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
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Год</span>" + tableEndStyle);
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Площадь</span>" + tableEndStyle);


        // Создание таблицы
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Панель ввода
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2));

        JLabel citizensLabel = new JLabel("Название жителей:");
        citizensTextField = new JTextField(20);



        inputPanel.add(citizensLabel);
        inputPanel.add(citizensTextField);

        filterButton = new JButton("Поиск");
        // Добавление компонентов на основное окно
        add(inputPanel, BorderLayout.NORTH);
        add(filterButton, BorderLayout.SOUTH);
        // Обработка событий кнопок
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String citizens = citizensTextField.getText();
                if (!citizens.isEmpty()) {
                    java.util.List<City> city = cityDAO.searchCitiesByCitizenName(citizens);
                    initializeTable(city);
                } else {
                    JOptionPane.showMessageDialog(FilterPanel2.this, "Заполните поля", "Ошибка", JOptionPane.ERROR_MESSAGE);

                }
            }
        });


    }

    private void initializeTable(List<City> cities) {
        tableModel.setRowCount(0);
        for (City city : cities) {
            tableModel.addRow(new Object[]{city.getId(), city.getName(), city.getYear(), city.getSquare()});
        }
    }
}
