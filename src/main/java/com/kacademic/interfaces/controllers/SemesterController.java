package com.kacademic.interfaces.controllers;

// import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.app.services.SemesterService;
import com.kacademic.shared.utils.Semester;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/semester")
@RestController
public class SemesterController {
    
    private final SemesterService semesterS;

    public SemesterController(SemesterService semesterS) {
        this.semesterS = semesterS;
    }

    @Operation(
        summary = "Finalize all grades of a Semester",
        description = "Students with a final average of 5 or higher will be Approved, while those below 5 will be Failed. " +
                      "<br>Note: This is applied after the Final Exam."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Finalizes all the grades of the semester and updates the enrollees' status (Approved/Failed).")
    })
    @PostMapping("/{semester}")
    public ResponseEntity<String> finalSubmit(
        @Parameter(description = "The semester to be finalized. <br>E.g: 25.1", required = true)
        @PathVariable @Semester String semester) {
        // return semesterS.finalSubmitAsync(semester).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
        return new ResponseEntity<>(semesterS.finalSubmitAsync(semester), HttpStatus.OK);
    }

}