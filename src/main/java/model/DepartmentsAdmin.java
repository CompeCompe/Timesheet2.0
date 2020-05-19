package model;

import Controller.WorkWithDb;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class DepartmentsAdmin extends JFrame {
    private static Object[] columnsHeader = new String[]{"Департаменты"};
    private static JTable table;
    public DepartmentsAdmin() throws SQLException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Создание таблицы с департаментами
        table = new JTable(createTableModel(WorkWithDb.departmentsList()));
        resizableFalse(table);

        JPanel funct = new JPanel();
        JButton update = new JButton("Изменить название департамента");
        JButton add = new JButton("Добавить департамент");
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new updateDialog().setVisible(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new addDialog().setVisible(true);
            }
        });
        funct.add(update);
        funct.add(add);

        //Меню для переключения функционала
        JMenuBar jMenuBar = new JMenuBar();
        JMenu role = new JMenu("Роль");
        jMenuBar.add(role);
        JMenuItem timekeeper = new JMenuItem("Табельщик");
        timekeeper.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new Visualization();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                setVisible(false);
            }
        });
        JMenuItem departAdm = new JMenuItem("Админ департаментов");
        departAdm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new DepartmentsAdmin();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                setVisible(false);
            }
        });
        JMenuItem workersAdm = new JMenuItem("Админ работников");
        workersAdm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new WorkersAdmin();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                setVisible(false);
            }
        });
        role.add(timekeeper);
        role.add(departAdm);
        role.add(workersAdm);


        //Добавление элементов
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(jMenuBar);
        contents.add(funct);
        contents.add(new JScrollPane(table));


        setContentPane(contents);
        setSize(1500, 500);
        setVisible(true);
    }

    private static DefaultTableModel createTableModel(List<String> departments){

        DefaultTableModel tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        // Определение столбцов
        tableModel.setColumnIdentifiers(columnsHeader);

        // Наполнение модели данными
        for (int i = 0; i < departments.size(); i++) {
            tableModel.addRow(new String[]{departments.get(i)});
        }
        return tableModel;
    }
    //Настройки разверов таблицы и запрет на их изменение
    private void resizableFalse(JTable table) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        table.setCellSelectionEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(0).setResizable(false);
    }

    private class updateDialog extends JDialog {
        public updateDialog() throws SQLException {
            JPanel jPanel = new JPanel();
            JLabel oldDep = new JLabel("Какой департамент изменить?");
            JLabel newDep = new JLabel("Введите новое название");
            String[] departments = new String[WorkWithDb.departmentsList().size()];
            for(int i = 0; i < WorkWithDb.departmentsList().size(); i++){
                departments[i] = WorkWithDb.departmentsList().get(i);
            }
            JComboBox<String> departmentsCombo = new JComboBox<>(departments);
            JTextField department = new JTextField(20);
            JButton update = new JButton("Изменить");
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        WorkWithDb.updateDepartment((String)departmentsCombo.getSelectedItem(),department.getText());
                        DepartmentsAdmin.table.setModel(createTableModel(WorkWithDb.departmentsList()));
                        resizableFalse(table);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
            });
            jPanel.add(oldDep);
            jPanel.add(departmentsCombo);
            jPanel.add(newDep);
            jPanel.add(department);
            jPanel.add(update);
            add(jPanel);
            setSize(250, 200);
            setResizable(false);
        }
    }

    private class addDialog extends JDialog {
        public addDialog(){
            JPanel jPanel = new JPanel();
            JLabel oldDep = new JLabel("Какой департамент добавить?");
            JTextField newDepartment = new JTextField(20);
            JButton add = new JButton("Добавить");
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        WorkWithDb.addDepartment(newDepartment.getText());
                        DepartmentsAdmin.table.setModel(createTableModel(WorkWithDb.departmentsList()));
                        resizableFalse(table);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
            });
            jPanel.add(oldDep);
            jPanel.add(newDepartment);
            jPanel.add(add);
            add(jPanel);
            setSize(250, 200);
            setResizable(false);
        }
    }

}
