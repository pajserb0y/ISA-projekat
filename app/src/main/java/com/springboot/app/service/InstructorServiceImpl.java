package com.springboot.app.service;

import com.springboot.app.model.FishingLesson;
import com.springboot.app.model.FishingLessonReservation;
import com.springboot.app.model.Instructor;
import com.springboot.app.model.Role;
import com.springboot.app.model.dto.DateTimeRangeDTO;
import com.springboot.app.model.dto.FishingLessonDTO;
import com.springboot.app.model.dto.InstructorDTO;
import com.springboot.app.repository.FishingLessonRepository;
import com.springboot.app.repository.FishingLessonReservationRepository;
import com.springboot.app.repository.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final FishingLessonReservationRepository fishingLessonReservationRepository;
    private final FishingLessonRepository fishingLessonRepository;
    private final RoleService roleService;

    public InstructorServiceImpl(InstructorRepository instructorRepository, RoleService roleService, FishingLessonReservationRepository fishingLessonReservationRepository, FishingLessonRepository fishingLessonRepository) {
        this.instructorRepository = instructorRepository;
        this.roleService = roleService;
        this.fishingLessonReservationRepository = fishingLessonReservationRepository;
        this.fishingLessonRepository = fishingLessonRepository;
    }

    @Override
    public Instructor saveInstructor(Instructor instructor) {
        List<Role> roles = roleService.findByName("ROLE_INSTRUCTOR");
        instructor.setRole(roles.get(0));
        instructor.setActivated(true);       //admin treba da odobri aktivaciju naloga koji nisu customer
        instructorRepository.save(instructor);
        return null;
    }

    @Override
    public Instructor findByUsername(String username) {
        return instructorRepository.findByUsername(username);
    }

    @Override
    public Instructor changeInstructor(InstructorDTO instructorDTO) {
        Instructor instructor = findByUsername(instructorDTO.getUsername());

        instructor.setPhone(instructorDTO.getPhone());
        instructor.setCountry(instructorDTO.getCountry());
        instructor.setCity(instructorDTO.getCity());
        instructor.setAddress(instructorDTO.getAddress());
        instructor.setLastName(instructorDTO.getLastName());
        instructor.setFirstName(instructorDTO.getFirstName());
        instructor.setMotive(instructorDTO.getMotive());

        saveInstructor(instructor);
        return null;
    }

    @Override
    public void addFishingLessonForInstructor(FishingLessonDTO fishingLessonDTO, Integer id) {

    }

    @Override
    public FishingLesson changeFishingLesson(FishingLessonDTO fishingLessonDTO) {
        return null;
    }

    @Override
    public boolean removeFishingLesson(Integer id) {
        return false;
    }

    @Override
    public List<FishingLesson> findAllFishingLessons() {
        return null;
    }

    @Override
    public List<FishingLesson> findAllAvailableFishingLessons(DateTimeRangeDTO dateRange) {
        return null;
    }

    @Override
    public List<FishingLesson> findAllFishingLessonsForInstructor(Integer instructorId) {
        return null;
    }

    @Override
    public List<FishingLessonReservation> findAllReservationsForInstructor(Integer instructorId, boolean futureOnly) {
        return null;
    }
}
