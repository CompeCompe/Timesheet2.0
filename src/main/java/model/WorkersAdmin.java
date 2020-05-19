package model;

import Controller.WorkWithDb;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkersAdmin extends JFrame {
    private static Object[] columnsHeader = new String[]{"ФИО","Табель","Департамент"};
    private static JTable table;
    public WorkersAdmin() throws SQLException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        table = new JTable(createTableModel(WorkWithDb.workersList()));
        resizableFalse(table);

        JPanel funct = new JPanel();
        JButton updateNumber = new JButton("Изменить табель");
        JButton updateDepartment = new JButton("Изменить департамент");
        JButton delete = new JButton("Уволить работника");

        updateNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new UpdateNumber().setVisible(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        updateDepartment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new UpdateDepartment().setVisible(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new DeleteWorker().setVisible(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        funct.add(updateDepartment);
        funct.add(updateNumber);
        funct.add(delete);

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
    private static DefaultTableModel createTableModel(String[][] workers){
        DefaultTableModel tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        // Определение столбцов
        tableModel.setColumnIdentifiers(columnsHeader);

        // Наполнение модели данными
        for (int i = 0; i < workers.length; i++) {
            tableModel.addRow(workers[i]);
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
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        for (int i = 0; i < 3;i++){
            table.getColumnModel().getColumn(i).setResizable(false);
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
        }

    }
    private class UpdateNumber extends JDialog{
        private UpdateNumber() throws SQLException {
            JPanel jPanel = new JPanel();
            JLabel ndisk = new JLabel("Табель какого работника поменять?");
            String[] name = new String[WorkWithDb.workersNames().size()];
            for (int i = 0;i < name.length; i++){
                name[i] = WorkWithDb.workersNames().get(i);
            }
            JComboBox names = new JComboBox(name);
            JLabel newDisk = new JLabel("Введите новый табельный номер");
            JTextField newNumber = new JTextField(10);
            JButton update = new JButton("Обновить");
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        WorkWithDb.updateNumber((String)names.getSelectedItem(),newNumber.getText());
                        WorkersAdmin.table.setModel(createTableModel(WorkWithDb.workersList()));
                        resizableFalse(table);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
            jPanel.add(ndisk);
            jPanel.add(names);
            jPanel.add(newDisk);
            jPanel.add(newNumber);
            jPanel.add(update);
            add(jPanel);
            setSize(250,250);
            setResizable(false);
        }
    }
    private class UpdateDepartment extends JDialog{
        public UpdateDepartment() throws SQLException {
            JPanel jPanel = new JPanel();
            JLabel ndisk = new JLabel("Департамент какого работника поменять?");
            String[] name = new String[WorkWithDb.workersNames().size()];
            for (int i = 0;i < name.length; i++){
                name[i] = WorkWithDb.workersNames().get(i);
            }
            JComboBox names = new JComboBox(name);
            JLabel ddisk = new JLabel("На какой департамент поменять?");
            String[] departments = new String[WorkWithDb.departmentsList().size()];
            for(int i = 0; i < WorkWithDb.departmentsList().size(); i++){
                departments[i] = WorkWithDb.departmentsList().get(i);
            }
            JComboBox<String> departmentsCombo = new JComboBox<>(departments);
            JButton update = new JButton("Обновить");
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        WorkWithDb.updateWorkersDep((String)names.getSelectedItem(),(String)departmentsCombo.getSelectedItem());
                        WorkersAdmin.table.setModel(createTableModel(WorkWithDb.workersList()));
                        resizableFalse(table);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
            jPanel.add(ndisk);
            jPanel.add(names);
            jPanel.add(ddisk);
            jPanel.add(departmentsCombo);
            jPanel.add(update);
            add(jPanel);
            setSize(250,250);
            setResizable(false);
        }
    }
    private class DeleteWorker extends JDialog{
        public DeleteWorker() throws SQLException {
            JPanel jPanel = new JPanel();
            JLabel ndisk = new JLabel("Кого уволить?");
            String[] name = new String[WorkWithDb.workersNames().size()];
            for (int i = 0;i < name.length; i++){
                name[i] = WorkWithDb.workersNames().get(i);
            }
            JComboBox names = new JComboBox(name);
            JButton delete = new JButton("Уволить");
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        WorkWithDb.deleteWorker((String)names.getSelectedItem());
                        WorkersAdmin.table.setModel(createTableModel(WorkWithDb.workersList()));
                        resizableFalse(table);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
            jPanel.add(ndisk);
            jPanel.add(names);
            jPanel.add(delete);
            add(jPanel);
            setSize(250,250);
            setResizable(false);
        }
    }
}
