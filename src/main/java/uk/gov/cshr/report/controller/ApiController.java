package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.cshr.report.controller.model.DeleteUserDetailsParams;
import uk.gov.cshr.report.service.CourseCompletionService;

@Controller
@RequestMapping("/api/report")
public class ApiController {

    private final CourseCompletionService courseCompletionService;

    public ApiController(CourseCompletionService courseCompletionService) {
        this.courseCompletionService = courseCompletionService;
    }

    @Transactional
    @PutMapping("/remove-user-details")
    @ResponseBody
    public int removeUserDetails(@RequestBody DeleteUserDetailsParams deleteUserDetailsParams) {
        return courseCompletionService.removeUserDetails(deleteUserDetailsParams.getUids());
    }
}
