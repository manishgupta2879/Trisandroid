package com.pcs.tim.myapplication;

public class PartialViewEmployer {
    private String empName ;
    private String empRegNo ;
    private String empContactName ;
    private String empContactNo;
    private String jobType ;
    private String workSector;
    private String workLocation ;
    private String workDescription ;
    private Integer lengthOfWork ;
    private Double currentPay ;

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpRegNo() {
        return empRegNo;
    }

    public void setEmpRegNo(String empRegNo) {
        this.empRegNo = empRegNo;
    }

    public String getEmpContactName() {
        return empContactName;
    }

    public void setEmpContactName(String empContactName) {
        this.empContactName = empContactName;
    }

    public String getEmpContactNo() {
        return empContactNo;
    }

    public void setEmpContactNo(String empContactNo) {
        this.empContactNo = empContactNo;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getWorkSector() {
        return workSector;
    }

    public void setWorkSector(String workSector) {
        this.workSector = workSector;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public Integer getLengthOfWork() {
        return lengthOfWork;
    }

    public void setLengthOfWork(Integer lengthOfWork) {
        this.lengthOfWork = lengthOfWork;
    }

    public Double getCurrentPay() {
        return currentPay;
    }

    public void setCurrentPay(Double currentPay) {
        this.currentPay = currentPay;
    }
}
