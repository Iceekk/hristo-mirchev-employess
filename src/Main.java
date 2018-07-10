import classes.Employee;
import exceptions.InvalidDateException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File file = new File(getStorageFilePath(scanner).toString());
        //writeData(file);
        longestPairEmployees(readData(file));
    }

    public static void writeData(File file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            Employee firstEmp = new Employee(143, 12, "2013-11-01", "2014-01-05");
            Employee secondEmp = new Employee(218, 10, "2011-05-16", null);
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

    private static List<Employee> readData(File file) {
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
                    //System.out.println(employee.toString());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("File not contain object to deserialize!");
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
                    long dateFrom;
                    long dateTo;
                    String empFirstDateFrom = employees.get(i).getDateFrom();
                    String empSecondDateFrom = employees.get(j).getDateFrom();
                    String empFirstDateTo = employees.get(i).getDateTo();
                    String empSecondDateTo = employees.get(j).getDateTo();

                    try {
                        long time1 = dateFormat.parse(empFirstDateFrom).getTime();
                        long time2 = dateFormat.parse(empSecondDateFrom).getTime();
                        long time3;
                        long time4;
                        long tempTime = 0;

                        if (empFirstDateTo != null) {
                            time3 = dateFormat.parse(empFirstDateTo).getTime();
                        } else {
                            time3 = System.currentTimeMillis();
                        }
                        if (empSecondDateTo != null) {
                            time4 = dateFormat.parse(empSecondDateTo).getTime();
                        } else {
                            time4 = System.currentTimeMillis();
                        }
                        if (time1 > time2) {
                            dateFrom = time1;
                        } else {
                            dateFrom = time2;
                        }
                        if (time3 > time4) {
                            dateTo = time4;
                        } else {
                            dateTo = time3;
                        }
                        if (dateTo > dateFrom) {
                            tempTime = Math.abs(dateTo - dateFrom);
                        } else {
                            continue;
                        }
                        if (tempTime > longestPair) {
                            longestPair = tempTime;
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

    private static java.nio.file.Path getStorageFilePath(Scanner scanner) {
        while (true) {
            System.out.println("Please enter path to file: ");
            String pathFromConsole = scanner.nextLine();

            if (isValidPath(pathFromConsole)) {
                return Paths.get(pathFromConsole);
            }
        }
    }

    private static boolean isValidPath(String pathFromConsole) {
        if (pathFromConsole.isEmpty()) {
            System.out.println("You didn't entered a path! ");
            return false;
        }

        java.nio.file.Path path = Paths.get(pathFromConsole);

        if (Files.isDirectory(path)) {
            System.out.println("This is a directory! \n");
            return false;
        }
        if (Files.notExists(path)) {
            System.out.println("This file didn't exists! \n");
            return false;
        }
        if (!Files.isExecutable(path)) {
            System.out.println("The file is not executable! \n");
            return false;
        }
        return true;
    }
}
