package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.controller.model.UpdateUserDetailsParams;
import uk.gov.cshr.report.controller.model.UpdateUserResult;
import uk.gov.cshr.report.service.RegisteredLearnersService;

@Slf4j
@Controller
@RequestMapping("/registered-learners")
public class RegisteredLearnersController {

    private final RegisteredLearnersService registeredLearnersService;

    public RegisteredLearnersController(RegisteredLearnersService registeredLearnersService) {
        this.registeredLearnersService = registeredLearnersService;
    }

    @Transactional
    @PutMapping("/deactivate")
    @ResponseBody
    public UpdateUserResult removeUserDetails(@Valid @RequestBody UpdateUserDetailsParams deleteUserDetailsParams) {
        return new UpdateUserResult(registeredLearnersService.deactivateLearners(deleteUserDetailsParams.getUids()));
    }

    @Transactional
    @ResponseBody
    @DeleteMapping("/bulk")
    public UpdateUserResult deleteUsers(@Valid @RequestBody UpdateUserDetailsParams deleteUserDetailsParams) {
        return new UpdateUserResult(registeredLearnersService.deleteLearners(deleteUserDetailsParams.getUids()));
    }


}
