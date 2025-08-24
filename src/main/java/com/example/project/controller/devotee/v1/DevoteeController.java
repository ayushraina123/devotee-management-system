package com.example.project.controller.devotee.v1;

import com.example.project.dtos.DevoteeDto;
import com.example.project.dtos.DevoteeRequestDto;
import com.example.project.service.DevoteeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/devotees")
public class DevoteeController {

    private final DevoteeService devoteeService;

    public DevoteeController(DevoteeService devoteeService) {
        this.devoteeService = devoteeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public List<DevoteeDto> getDevotees(@RequestParam int pageNumber, @RequestParam int pageSize,
                                        @ModelAttribute DevoteeRequestDto devoteeRequestDto) {
        return devoteeService.getAllDevotees(pageNumber, pageSize, devoteeRequestDto.getFirstName(), devoteeRequestDto.getLastName(),
                devoteeRequestDto.getCity(), devoteeRequestDto.getState(), devoteeRequestDto.getDonationType());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void saveDevotees(@RequestBody @Valid @NotEmpty(message = "Request body cannot be empty") List<DevoteeDto> devoteeDtos) {
        devoteeService.saveDevotees(devoteeDtos);
    }

    @DeleteMapping("/{devoteeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDevotee(@PathVariable("devoteeId") Long devoteeId) {
        devoteeService.deleteDevotee(devoteeId);
    }


}
