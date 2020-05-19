package model;

import Controller.WorkWithDb;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Visualization extends JFrame{

    public void setInformation(String[][] information) {
        this.information = information;
    }

    private String[][] information = new String[][]{{"Антон Антоныч","1231","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я","Я"}};

    // Заголовки столбцов
    private Object[] columnsHeader = new String[]{"ФИО", "Табель",
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

    public Visualization() throws SQLException {
        super("Табель");
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }



        // Создание таблицы на основании модели данных
        JTable table = new JTable(createTableModel(information));
        resizableFalse(table);

        JLabel department = new JLabel("Выбор департамента");

        JComboBox<String> departments = new JComboBox<String>(new String[] { "Автоматизация", "Разработка", "Точильня"});

        JLabel month = new JLabel("Выбор месяца");

        JComboBox<String> months = new JComboBox<String>(new String[] { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"});

        JButton update = new JButton("Обновить табель");
        //Изменение таблицы по нажатию
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setInformation(WorkWithDb.UpdateTimeSheet((String)departments.getSelectedItem(),(String)months.getSelectedItem()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
               table.setModel(createTableModel(information));
                resizableFalse(table);
            }
        });

        JComboBox<String> months2 = new JComboBox<String>(new String[] { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"});
        JComboBox<Integer> days = new JComboBox<>(new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31});
        JComboBox<String> mark = new JComboBox<>(new String[]{"Я","Н","В","Рв","Б","К","ОТ","До","Хд","У","Ож"});
        JTextField name = new JTextField(20);
        JButton updateDb = new JButton("Отметиться");
        updateDb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    WorkWithDb.UpdateDb(name.getText(),(String)months2.getSelectedItem(),(int)days.getSelectedItem(),(String)mark.getSelectedItem());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    setInformation(WorkWithDb.UpdateTimeSheet((String)departments.getSelectedItem(),(String)months2.getSelectedItem()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                table.setModel(createTableModel(information));
                resizableFalse(table);
            }
        });
        //Поля для функционала Табельщика
        JLabel disk = new JLabel("Проставление отметки");
        JLabel diskName = new JLabel("Напишите свое ФИО");
        JLabel diskDate = new JLabel("Выберите дату");
        JLabel diskMark = new JLabel("Выберите статус");
        JPanel note = new JPanel();
        JPanel jPanel = new JPanel();
        jPanel.add(disk);
        note.add(diskName);
        note.add(name);
        note.add(diskDate);
        note.add(months2);
        note.add(days);
        note.add(diskMark);
        note.add(mark);
        note.add(updateDb);
        note.revalidate();

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


        //Добавление элементов на экран
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(jMenuBar);
        contents.add(department);
        contents.add(departments);
        contents.add(month);
        contents.add(months);
        contents.add(update);
        contents.add(jPanel);
        contents.add(note);
        contents.add(new JScrollPane(table));



        setContentPane(contents);
        setSize(1500, 500);
        setVisible(true);
        setResizable(false);
        setVisible(true);
    }

    private DefaultTableModel createTableModel(String[][] info){

        DefaultTableModel tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        // Определение столбцов
        tableModel.setColumnIdentifiers(columnsHeader);
        // Наполнение модели данными
        for (int i = 0; i < info.length; i++) {
            tableModel.addRow(info[i]);
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
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        int columnNumber = table.getColumnModel().getColumnCount();
        for (int i = 0;i < columnNumber;i++){
            table.getColumnModel().getColumn(i).setResizable(false);
            table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
            if(i > 1){
                table.getColumnModel().getColumn(i).setPreferredWidth(50);
            }
        }
    }

}

