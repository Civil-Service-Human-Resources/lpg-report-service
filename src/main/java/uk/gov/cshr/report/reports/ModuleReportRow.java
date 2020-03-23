package uk.gov.cshr.report.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"learnerId", "name", "email", "department", "profession", "otherAreasOfWork", "grade", "courseId",
        "courseTitle", "courseTopicId", "moduleId", "moduleTitle", "learningProvider", "required", "associatedLearning", "status", "dateUpdated", "dateCompleted"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleReportRow {
    private String learnerId;
    private String name;
    private String email;
    private String department;
    private String profession;
    private String otherAreasOfWork;
    private String grade;

    private String courseId;
    private String courseTitle;
    private String courseTopicId;
    private String moduleId;
    private String moduleTitle;
    private String moduleType;
    private boolean required;
    private boolean paidFor;

    private String status;
    private String updatedAt;
    private String completedAt;

    public String getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(String learnerId) {
        this.learnerId = learnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOtherAreasOfWork() {
        return otherAreasOfWork;
    }

    public void setOtherAreasOfWork(String otherAreasOfWork) {
        this.otherAreasOfWork = otherAreasOfWork;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseTopicId() {
        return courseTopicId;
    }

    public void setCourseTopicId(String courseTopicId) {
        this.courseTopicId = courseTopicId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isPaidFor() { return paidFor; }

    public void setPaidFor(boolean associateLearning) { this.paidFor = associateLearning; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

}
