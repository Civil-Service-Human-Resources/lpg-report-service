package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.cshr.report.controller.model.DeleteUserResults;
import uk.gov.cshr.report.controller.model.UpdateUserDetailsParams;
import uk.gov.cshr.report.controller.model.UpdateUserResult;
import uk.gov.cshr.report.service.CourseCompletionService;
import uk.gov.cshr.report.service.RegisteredLearnersService;

@Controller
@RequestMapping("/api/report")
public class ApiController {

    private final CourseCompletionService courseCompletionService;
    private final RegisteredLearnersService registeredLearnersService;

    public ApiController(CourseCompletionService courseCompletionService, RegisteredLearnersService registeredLearnersService) {
        this.courseCompletionService = courseCompletionService;
        this.registeredLearnersService = registeredLearnersService;
    }

    @Transactional
    @PutMapping("/remove-user-details")
    @ResponseBody
    public DeleteUserResults removeUserDetails(@Valid @RequestBody UpdateUserDetailsParams deleteUserDetailsParams) {
        return new DeleteUserResults(courseCompletionService.removeUserDetails(deleteUserDetailsParams.getUids()),
        registeredLearnersService.deleteLearners(deleteUserDetailsParams.getUids()));
    }

    @Transactional
    @PutMapping("/deactivate-users")
    @ResponseBody
    public UpdateUserResult deactivateUsers(@Valid @RequestBody UpdateUserDetailsParams deleteUserDetailsParams) {
        return new UpdateUserResult(registeredLearnersService.deactivateLearners(deleteUserDetailsParams.getUids()));
    }

}
