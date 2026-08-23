package com.troy.ats.populator;

import com.troy.ats.dto.ActivityLogDto;
import com.troy.ats.entity.ActivityLog;
import com.troy.ats.service.SessionService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.troy.ats.util.CommonUtil.convertInstantToLocalDate;

@Component
public class ActivityLogPopulator {

    @Autowired
    @Resource(name="sessionService")
    private SessionService sessionService;

    public void populate(ActivityLog source, ActivityLogDto target) {

        target.setId(source.getId());
        target.setEntityType(source.getEntityType());
        target.setEntityId(source.getEntityId());
        target.setAction(source.getAction());
        target.setOldValue(source.getOldValue());
        target.setNewValue(source.getNewValue());
        target.setDescription(source.getDescription());
        target.setPerformedBy(source.getPerformedBy().getFullName());

        target.setPerformedAt(convertInstantToLocalDate(source.getPerformedAt(), sessionService));

    }
}
