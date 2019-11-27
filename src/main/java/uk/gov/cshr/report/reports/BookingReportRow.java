package uk.gov.cshr.report.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"learnerId", "name", "email", "department", "profession", "otherAreasOfWork", "grade", "courseId",
        "courseTitle", "moduleId", "moduleTitle", "learningProvider", "required", "associatedLearning", "status", "bookingTime", "confirmationTime",
        "cancellationTime", "accessibilityOptions", "bookingCancellationReason", "topicId", "poNumber"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingReportRow {
    private String learnerId;
    private String name;
    private String email;
    private String department;
    private String profession;
    private String otherAreasOfWork;
    private String grade;
    private String courseId;
    private String courseTitle;
    private String moduleId;
    private String moduleTitle;
    private String learningProvider;
    private boolean required;
    private boolean associatedLearning;
    private String status;
    private String bookingTime;
    private String confirmationTime;
    private String cancellationTime;
    private String accessibilityOptions;
    private String bookingCancellationReason;
    private String topicId;
    private String poNumber;

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

    public String getLearningProvider() {
        return learningProvider;
    }

    public void setLearningProvider(String learningProvider) {
        this.learningProvider = learningProvider;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getConfirmationTime() {
        return confirmationTime;
    }

    public void setConfirmationTime(String confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public String getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(String cancellationTime) {
        this.cancellationTime = cancellationTime;
    }

    public String getAccessibilityOptions() {
        return accessibilityOptions;
    }

    public void setAccessibilityOptions(String accessibilityOptions) {
        this.accessibilityOptions = accessibilityOptions;
    }

    public String getBookingCancellationReason() {
        return bookingCancellationReason;
    }

    public void setBookingCancellationReason(String bookingCancellationReason) {
        this.bookingCancellationReason = bookingCancellationReason;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public boolean isAssociatedLearning() { return associatedLearning; }

    public void setAssociatedLearning(boolean associatedLearning) { this.associatedLearning = associatedLearning; }
}
