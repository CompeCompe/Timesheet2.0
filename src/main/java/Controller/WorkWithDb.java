
package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkWithDb {

    //Запрос для изменения таблицы в зависимости от департамента и месяца
    public static String[][] UpdateTimeSheet(String department, String month) throws SQLException {
        //Подключение к базе данных
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        //Получение списка рабочих в определенном департаменте
        List<String> workers = new ArrayList<>();
        int departmentId = 0;
        ResultSet departments = stmt.executeQuery("SELECT idDepartments from Departments where Department = \""+ department +"\"");
        if(departments.next()){
            departmentId = departments.getInt(1);
        }
        ResultSet workersInDepartment = stmt.executeQuery("SELECT Name FROM Workers Where depart = \""+ departmentId +"\"");
        while(workersInDepartment.next()){
            workers.add(workersInDepartment.getString(1));
        }
        //Получение объдиненных таблиц для вывода кодификаторов на каждый день в месяце
        String[][] information = new String[workers.size()][33];
            for(int i = 0; i < workers.size(); i++){
                String[] kontroll = new String[33];
                String query = "SELECT Name,Number,Day,Encoding FROM Workers JOIN Calendar ON Workkers_id = idWorkers " +
                            " Join Encodings ON Status = idEncodings where Month = \"" + month +
                            "\" and Name = \""+
                            workers.get(i) +
                            "\"order by Day+0 ASC;";
                ResultSet rs = stmt.executeQuery(query);
                for(int j = 2;j < 33;j++) {
                    if (rs.next()) {
                        kontroll[0] = rs.getString(1);
                        kontroll[1] = rs.getString(2);
                        kontroll[rs.getInt(3) + 1] = rs.getString(4);

                    }
                    if (kontroll[j] == null) {
                        kontroll[j] = "-";
                    }
                }
                information[i] = kontroll;
            }
        connection.close();
        return information;
    }

    //Запрос на добавление записи в таблицу
    public static void UpdateDb(String name, String month, int day, String mark) throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        int id = 0;
        ResultSet workerId = stmt.executeQuery("select idWorkers from Workers where Name = \""+ name +"\"");
        if(workerId.next()){
            id = workerId.getInt(1);
        }
        ResultSet markId = stmt.executeQuery("SELECT idEncodings FROM Encodings where Encoding = \""+ mark +"\"");
        int status = 0;
        if(markId.next()){
            status = markId.getInt(1);
        }
        stmt.executeUpdate("insert into Calendar (Workkers_id,Month,Day,Status) value (\""+id+"\",\""+month+"\","+day+","+status+")");
        connection.close();
    }


    //Запрос на вывод списка департаментов
    public static List<String> departmentsList() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        List<String> departments = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("Select Department from Departments");
        while (rs.next()){
            departments.add(rs.getString(1));
        }
        connection.close();
        return departments;
    }
    // Запрос на добавление департамента
    public static void addDepartment(String department) throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        stmt.executeUpdate("insert into Departments (Department) value (\""+department+"\")");
        connection.close();
    }
    //Запрос на изменения департамента
    public static void updateDepartment(String oldDepartment, String newDepartment) throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        stmt.executeUpdate("update Departments Set Department = \""+newDepartment+"\" where Department = \""+oldDepartment+"\"");
        connection.close();
    }

    //Запрос на вывод списка работников
    public static String[][] workersList() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        int count = 0;
        ResultSet workersInDepartment = stmt.executeQuery("SELECT * FROM Workers");
        while(workersInDepartment.next()){
            count++;
        }
        String[][] workers = new String[count][3];
        ResultSet workersInfo = stmt.executeQuery("select Name,Number,Department from Workers JOIN Departments ON depart = idDepartments;");
        for(int i = 0;i < count; i++){
            String[] info = new String[3];
            if (workersInfo.next()) {
                info[0] = workersInfo.getString(1);
                info[1] = workersInfo.getString(2);
                info[2] = workersInfo.getString(3);
            }
            workers[i] = info;
        }
        connection.close();
        return workers;
    }
    //Запрос на изменение табельного номера работчника
    public static void updateNumber(String name, String Number) throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        stmt.executeUpdate("update Workers set Number = \""+Number+"\" where Name = \""+name+"\"");
        connection.close();
    }
    //Запрос на изменение департамента работника
    public static void updateWorkersDep(String name, String department) throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        int departmentId = 0;
        ResultSet departments = stmt.executeQuery("SELECT idDepartments from Departments where Department = \""+ department +"\"");
        if(departments.next()){
            departmentId = departments.getInt(1);
        }
        stmt.executeUpdate("update Workers set depart = "+departmentId+" where Name = \""+name+"\"");
        connection.close();
    }
    //Запрос на удаление работника из бд
    public static void deleteWorker(String name) throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        stmt.executeUpdate("delete from Workers where Name = \"+name+\"");
        connection.close();
    }
    //Запрос на вывод имен работников
    public static List<String> workersNames() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/timesheet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("Select Name From Workers");
        List<String> names = new ArrayList<>();
        while (rs.next()){
            names.add(rs.getString(1));
        }
        return names;
    }
}
