package org.example.ui;

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

public class Cities extends JPanel {
    private CityDAO cityDAO;
    private JTextField nameTextField;
    private JFormattedTextField yearTextField;
    private JFormattedTextField squareTextField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton createButton;
    private JButton editButton;
    private JButton deleteButton;

    public Cities() {

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
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Год основания</span>" + tableEndStyle);
        tableModel.addColumn(tableStyle + "<span style='color: red; font-size: 16px;'>Площадь</span>" + tableEndStyle);
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

        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Название:");
        nameTextField = new JTextField(20);

        JLabel yearLabel = new JLabel("Год основания:");
        NumberFormatter formatterForYear = new NumberFormatter();
        formatterForYear.setMinimum(0); // Минимальное значение
        formatterForYear.setMaximum(2023); // Максимальное значение
        yearTextField = new JFormattedTextField(formatterForYear);
        yearTextField.setColumns(10);

        JLabel squareLabel = new JLabel("Площадь км2:");
        NumberFormatter formatterForSquare = new NumberFormatter();
        formatterForSquare.setMinimum(0); // Минимальное значение
        formatterForSquare.setMaximum(10000); // Максимальное значение
        squareTextField = new JFormattedTextField(formatterForSquare);
        squareTextField.setColumns(10);

        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(yearLabel);
        inputPanel.add(yearTextField);
        inputPanel.add(squareLabel);
        inputPanel.add(squareTextField);

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
                if (!name.isEmpty() && !yearTextField.getText().isEmpty() && !squareTextField.getText().isEmpty()) {
                    int year = ((Number) yearTextField.getValue()).intValue();
                    int square = ((Number) squareTextField.getValue()).intValue();

                    if (year >= 0 && year <= 2023 && square > 0 && square < 100000) {
                        City city = cityDAO.create(new City(name, year, square));
                        tableModel.addRow(new Object[]{city.getId(), city.getName(), city.getYear(), city.getSquare()});
                        nameTextField.setText("");
                        yearTextField.setText("");
                        squareTextField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(Cities.this, "Данные некорректны", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Cities.this, "Заполните поля", "Ошибка", JOptionPane.ERROR_MESSAGE);

                }

            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                String name = nameTextField.getText();
                if (selectedRow >= 0) {
                    if (!name.isEmpty() && !yearTextField.getText().isEmpty() && !squareTextField.getText().isEmpty()) {
                        int year = ((Number) yearTextField.getValue()).intValue();
                        int square = ((Number) squareTextField.getValue()).intValue();
                        if (year >= 0 && year <= 2023 && square > 0 && square < 100000) {
                            City city = cityDAO.update(new City((Integer) tableModel.getValueAt(selectedRow, 0), name, year, square));

                            tableModel.setValueAt(name, selectedRow, 1);
                            tableModel.setValueAt(year, selectedRow, 2);
                            tableModel.setValueAt(square, selectedRow, 3);

                            nameTextField.setText("");
                            yearTextField.setText("");
                            squareTextField.setText("");
                        } else {
                            JOptionPane.showMessageDialog(Cities.this, "Данные некорректны", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Cities.this, "Заполните поля", "Ошибка", JOptionPane.ERROR_MESSAGE);

                    }
                }
            }

        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    cityDAO.delete(new City((Integer) tableModel.getValueAt(selectedRow, 0), (String) tableModel.getValueAt(selectedRow, 1), (Integer) tableModel.getValueAt(selectedRow, 2), (Integer) tableModel.getValueAt(selectedRow, 3)));
                    tableModel.removeRow(selectedRow);

                    nameTextField.setText("");
                    yearTextField.setText("");
                    squareTextField.setText("");
                } else {
                    JOptionPane.showMessageDialog(Cities.this, "Выберите запись для удаления", "Ошибка", JOptionPane.ERROR_MESSAGE);
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
                    int yaer = (int) tableModel.getValueAt(selectedRow, 2);
                    int square = (int) tableModel.getValueAt(selectedRow, 3);
                    // Установка данных в текстовые поля
                    nameTextField.setText(name);
                    yearTextField.setValue(yaer);
                    squareTextField.setValue(square);
                }
            }
        });
    }

    private void initializeTable() {
        java.util.List<City> cityList = cityDAO.getAll();
        for (City city : cityList) {
            tableModel.addRow(new Object[]{city.getId(), city.getName(), city.getYear(), city.getSquare()});
        }
    }

    public void refreshData() {
        // Обновление данных в таблице
        tableModel.setRowCount(0);
        List<City> cities = cityDAO.getAll();
        tableModel.setRowCount(0);
        for (City city : cities) {
            tableModel.addRow(new Object[]{city.getId(), city.getName(), city.getYear(), city.getSquare()});
        }
    }


}
