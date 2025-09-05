package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.domain.User;
import com.readytoplanbe.myapp.repository.UserRepository;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link TrainingCourse} and its DTO {@link TrainingCourseDTO}.
 */
@Mapper(componentModel = "spring")
public abstract class TrainingCourseMapper implements EntityMapper<TrainingCourseDTO, TrainingCourse> {

    protected UserRepository userRepository;

    // Injection via setter pour éviter problème compilation MapStruct
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdByLogin", source = "createdByLogin")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "createdByName", ignore = true) // On set après avec @AfterMapping
    public abstract TrainingCourseDTO toDto(TrainingCourse trainingCourse);

    @AfterMapping
    protected void setCreatedByName(TrainingCourse trainingCourse, @MappingTarget TrainingCourseDTO dto) {
        if (trainingCourse.getCreatedByLogin() != null && userRepository != null) {
            User user = userRepository.findOneByLogin(trainingCourse.getCreatedByLogin()).orElse(null);
            if (user != null) {
                dto.setCreatedByName(user.getFirstName() + " " + user.getLastName());
            } else {
                dto.setCreatedByName(trainingCourse.getCreatedByLogin()); // fallback si l'utilisateur n'existe pas
            }
        }
    }

    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdByLogin", source = "createdByLogin")
    @Mapping(target = "createdDate", source = "createdDate")
    public abstract TrainingCourse toEntity(TrainingCourseDTO trainingCourseDTO);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdByLogin", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    public abstract void partialUpdate(@MappingTarget TrainingCourse entity, TrainingCourseDTO dto);
}
