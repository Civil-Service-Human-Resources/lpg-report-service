package uk.gov.cshr.report.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.gov.cshr.report.controller.model.UpdateUserDetailsParams;
import uk.gov.cshr.report.controller.model.UpdateUserResult;
import uk.gov.cshr.report.controller.model.reportRequest.AddReportRequestResponse;
import uk.gov.cshr.report.controller.model.reportRequest.GetReportRequestParams;
import uk.gov.cshr.report.controller.model.reportRequest.GetReportRequestsResponse;
import uk.gov.cshr.report.controller.model.reportRequest.OrganisationalReportRequestParams;
import uk.gov.cshr.report.domain.report.RegisteredLearnerReportRequest;
import uk.gov.cshr.report.service.RegisteredLearnersService;
import uk.gov.cshr.report.service.reportRequests.RegisteredLearnerReportRequestService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/registered-learners")
public class RegisteredLearnersController {

    private final RegisteredLearnersService registeredLearnersService;
    private final RegisteredLearnerReportRequestService registeredLearnerReportRequestService;

    public RegisteredLearnersController(RegisteredLearnersService registeredLearnersService,
                                        RegisteredLearnerReportRequestService registeredLearnerReportRequestService) {
        this.registeredLearnersService = registeredLearnersService;
        this.registeredLearnerReportRequestService = registeredLearnerReportRequestService;
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

    @PostMapping("/report-requests")
    @ResponseBody
    public AddReportRequestResponse addReportRequest(@RequestBody @Valid OrganisationalReportRequestParams params){
        return registeredLearnerReportRequestService.addReportRequest(params);
    }

    @GetMapping("/report-requests")
    @ResponseBody
    public GetReportRequestsResponse<RegisteredLearnerReportRequest> getAllReportRequests(@Valid GetReportRequestParams params){
        List<RegisteredLearnerReportRequest> reportRequests = registeredLearnerReportRequestService.findReportRequestsByUserIdAndStatus(params);
        return new GetReportRequestsResponse<>(reportRequests);
    }


}
