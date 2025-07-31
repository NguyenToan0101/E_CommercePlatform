package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.ActivityDto;
import org.example.ecommerce.service.admin.ActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;



    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/data")
    public List<ActivityDto> getActivities() {
        System.out.println("Activity " +   activityService.getActivitiesDTO());
        return activityService.getActivitiesDTO();
    }
}
