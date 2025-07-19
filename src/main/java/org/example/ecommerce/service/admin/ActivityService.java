package org.example.ecommerce.service.admin;

import org.example.ecommerce.common.dto.ActivityDto;
import org.example.ecommerce.common.mapper.ActivityMapper;
import org.example.ecommerce.entity.admin.Activity;
import org.example.ecommerce.repository.admin.ActivityRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityService {

    private final ActivityRepo activityRepo;
    private final ActivityMapper activityMapper;
    public ActivityService(ActivityRepo activityRepo, ActivityMapper activityMapper) {
        this.activityRepo = activityRepo;
        this.activityMapper = activityMapper;
    }

    public List<Activity> findAll() {
           return activityRepo.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
    }
    public void save(Activity activity) {
        activityRepo.save(activity);

        List<Activity> activities = activityRepo.findAll();
        if(activities.size() > 5){
            List<Activity> newActivities = activities.subList(5, activities.size());
            activityRepo.deleteAll(newActivities);
        }
    }

    public List<ActivityDto> getActivitiesDTO() {
        return activityMapper.activityToActivityDtoList(activityRepo.findAll());
    }
}
