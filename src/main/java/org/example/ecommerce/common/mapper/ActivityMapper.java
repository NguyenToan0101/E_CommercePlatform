package org.example.ecommerce.common.mapper;

import org.example.ecommerce.common.dto.ActivityDto;
import org.example.ecommerce.entity.admin.Activity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {


    ActivityDto activityToActivityDto(Activity activity);

    List<ActivityDto> activityToActivityDtoList(List<Activity> activities);
}
