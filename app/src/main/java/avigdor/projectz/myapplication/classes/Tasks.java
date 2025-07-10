package avigdor.projectz.myapplication.classes;

public class Tasks {
    private String taskID, taskClass, taskNumClass,
            taskName, taskDescription, taskStatus,
            taskStartDate, taskEndDate, taskPicUrl,
            taskYear;



    public Tasks() {}

    public Tasks(String taskID, String taskClass, String taskNumClass,
                 String taskName, String taskDescription, String taskStatus,
                 String taskStartDate, String taskEndDate, String taskPicUrl,
                 String taskYear) {
        this.taskID = taskID;
        this.taskClass = taskClass;
        this.taskNumClass = taskNumClass;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskStartDate = taskStartDate;
        this.taskEndDate = taskEndDate;
        this.taskPicUrl = taskPicUrl;
        this.taskYear = taskYear;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getTaskNumClass() {
        return taskNumClass;
    }

    public void setTaskNumClass(String taskNumClass) {
        this.taskNumClass = taskNumClass;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(String taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public String getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(String taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public String getTaskPicUrl() {
        return taskPicUrl;
    }
    public String getTaskYear(){return taskYear;}

    public void setTaskPicUrl(String taskPicUrl) {
        this.taskPicUrl = taskPicUrl;
    }
}
