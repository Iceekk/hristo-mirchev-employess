package classes;

import exceptions.InvalidDateException;

import java.io.Serializable;

public class Employee implements Serializable {
    private int empId;
    private int projectId;
    private String dateFrom;
    private String dateTo;

    public Employee(int empId, int projectId, String dateFrom, String dateTo) throws InvalidDateException {
        this.setEmpId(empId);
        this.setProjectId(projectId);
        this.setDateFrom(dateFrom);
        this.setDateTo(dateTo);
    }

    public int getEmpId() {
        return this.empId;
    }

    private void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getProjectId() {
        return this.projectId;
    }

    private void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getDateFrom() {
        return this.dateFrom;
    }

    private void setDateFrom(String dateFrom) throws InvalidDateException {
        if (dateFrom.equals(null)) {
            throw new InvalidDateException("The date is invalid!");
        }
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return this.dateTo;
    }

    private void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", projectId=" + projectId +
                ", dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                '}';
    }
}
