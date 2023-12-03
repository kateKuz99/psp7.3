package org.example;

import org.example.ui.Cities;
import org.example.ui.Citizens;
import org.example.ui.FilterPanel;
import org.example.ui.FilterPanel2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public Main() {
        setTitle("Учет городов и жителей");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        Cities cities = new Cities();
        Citizens citizens = new Citizens();
        FilterPanel filterPanel = new FilterPanel();
        FilterPanel2 filterPanel2 = new FilterPanel2();

        cardPanel.add(cities, "screen1");
        cardPanel.add(citizens, "screen2");
        cardPanel.add(filterPanel, "screen3");
        cardPanel.add(filterPanel2, "screen4");

        getContentPane().add(cardPanel, BorderLayout.CENTER);

        JButton button1 = new JButton("Города");
        JButton button2 = new JButton("Жители");
        JButton button3 = new JButton("Поиск");
        JButton button4 = new JButton("Поиск 2");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);

        getContentPane().add(buttonPanel, BorderLayout.EAST);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "screen1");
                Cities cities = (Cities) cardPanel.getComponent(0);
                cities.refreshData(); // Вызываем метод обновления данных при переходе на панель "Города"
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "screen2");
                Citizens citizens1 = (Citizens) cardPanel.getComponent(1);
                citizens1.refreshData(); // Вызываем метод обновления данных при переходе на панель "Города"

            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "screen3");
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "screen4");

            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);

            }
        });
    }
}