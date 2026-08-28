package com.troy.ats.populator;

import com.troy.ats.dto.NoteDto;
import com.troy.ats.entity.Note;
import com.troy.ats.service.SessionService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.troy.ats.util.CommonUtil.convertInstantToLocalDate;

@Component
public class NotePopulator {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);

    public void populate(Note source, NoteDto target, SessionService sessionService) {

        target.setEntityType(source.getEntityType());
        target.setEntityId(source.getEntityId());
        target.setContent(source.getContent());
        target.setChatWith(source.getCreatedBy().getFullName());
        LocalDateTime createdAt = convertInstantToLocalDate(source.getCreatedAt(), sessionService);
        target.setChatAt(createdAt.format(formatter));

    }
}
