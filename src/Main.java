import classes.Employee;
import exceptions.InvalidDateException;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\Ico\\IdeaProjects\\Task\\src\\data");
        writeData(file);
        longestPairEmployees(readData(file));
    }

    public static void writeData(File file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            Employee firstEmp = new Employee(143, 12, "2013-11-01", "2014-01-05");
            Employee secondEmp = new Employee(218, 10, "2012-05-16", null);
            Employee thirdEmp = new Employee(143, 10, "2009-01-01", "2011-04-27");

            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(firstEmp);
            oos.writeObject(secondEmp);
            oos.writeObject(thirdEmp);
        } catch (InvalidDateException e) {
            System.out.println("Invalid date! ");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Error initializing stream!");
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Employee> readData(File file) {
        List<Employee> employees = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            while (fis.available() > 0) {
                Object obj = ois.readObject();
                if (obj instanceof Employee) {
                    Employee employee = (Employee) obj;
                    employees.add(employee);
                    System.out.println(employee.toString());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Error initializing stream!");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found! ");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return employees;
    }

    private static void longestPairEmployees(List<Employee> employees) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayDeque<Employee> stack = new ArrayDeque<>();
        long longestPair = 0;

        for (int i = 0; i < employees.size() - 1; i++) {
            for (int j = i + 1; j < employees.size(); j++) {

                if ((employees.get(i).getEmpId() != employees.get(j).getEmpId()) &&
                        (employees.get(i).getProjectId() == employees.get(j).getProjectId())) {
                    long dateFrom = 0;
                    long dateTo = 0;
                    String empFirstDateFrom = employees.get(i).getDateFrom();
                    String empSecondDateFrom = employees.get(j).getDateFrom();
                    String empFirstDateTo = employees.get(i).getDateTo();
                    String empSecondDateTo = employees.get(j).getDateTo();

                    try {
                        Date t1 = dateFormat.parse(empFirstDateFrom);
                        Date t2 = dateFormat.parse(empSecondDateFrom);
                        Date t3 = null;
                        Date t4 = null;
                        long temp = 0;

                        if (empFirstDateTo != null) {
                            t3 = dateFormat.parse(empFirstDateTo);
                        } else {
                            t3 = dateFormat.getCalendar().getTime();
                        }
                        if (empSecondDateTo != null) {
                            t4 = dateFormat.parse(empSecondDateTo);
                        } else {
                            t4 = dateFormat.getCalendar().getTime();
                        }
                        if (t1.compareTo(t2) > 0) {
                            dateFrom = t1.getTime();
                        } else {
                            dateFrom = t2.getTime();
                        }
                        if (t3.compareTo(t4) > 0) {
                            dateTo = t4.getTime();
                        } else {
                            dateTo = t3.getTime();
                        }
                        if (dateTo > dateFrom) {
                            temp = Math.abs(dateTo - dateFrom);
                        } else {
                            continue;
                        }
                        if (temp > longestPair) {
                            longestPair = temp;
                            stack.push(employees.get(i));
                            stack.push(employees.get(j));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (longestPair != 0) {
            System.out.println("Longest period of working together is between: colegue with empId " +
                    stack.pop().getEmpId() + " and colegue with empId " +
                    stack.pop().getEmpId() + " - " +
                    (longestPair / 60 / 60 / 24 / 1000) + " days! ");
        } else {
            System.out.println("There are no colleagues that working together on same project! ");
        }
    }
}
