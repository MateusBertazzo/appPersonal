package br.com.apppersonal.apppersonal.service;

import br.com.apppersonal.apppersonal.exceptions.ExercisesNotFound;
import br.com.apppersonal.apppersonal.exceptions.ParameterNullException;
import br.com.apppersonal.apppersonal.model.Dto.TrainingExercicesDto;
import br.com.apppersonal.apppersonal.model.entitys.ExerciseEntity;
import br.com.apppersonal.apppersonal.model.entitys.TrainingEntity;
import br.com.apppersonal.apppersonal.model.entitys.UserEntity;
import br.com.apppersonal.apppersonal.model.repositorys.ExerciseRepository;
import br.com.apppersonal.apppersonal.model.repositorys.TrainingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TrainingDayWeekService {
    private final TrainingRepository trainingRepository;

    private final UserService userService;

    private final ExerciseRepository exerciseRepository;

    public TrainingDayWeekService(TrainingRepository trainingRepository, UserService userService,
                                  ExerciseRepository exerciseRepository) {
        this.trainingRepository = trainingRepository;
        this.userService = userService;
        this.exerciseRepository = exerciseRepository;
    }

    public void createTrainingDayWeek(Long userId, String dayOfWeek, List<ExerciseEntity> exerciseEntityList) {
        if (exerciseEntityList.isEmpty()) throw new RuntimeException("Exercício não pode ser vazio");
        if (dayOfWeek.isEmpty()) throw new RuntimeException("Dia da semana não pode ser vazio");
        if (userId == null) throw new RuntimeException("Usuário não pode ser vazio");

        UserEntity user = userService.getUserById(userId);

        if (user == null) throw new RuntimeException("Usuário não encontrado");

        try {
            TrainingEntity trainingEntity = new TrainingEntity();
            trainingEntity.setUser(user);
            trainingEntity.setDayOfWeek(dayOfWeek);

            exerciseEntityList.forEach(exercise -> exercise.setTraining(trainingEntity));

            trainingRepository.save(trainingEntity);

            exerciseRepository.saveAll(exerciseEntityList);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar treino");
        }

    }

    public List<TrainingExercicesDto> getExerciseByTrainingId(Long trainingId) {
        if (trainingId == null) throw new ParameterNullException();

        List<ExerciseEntity> exerciseEntities = exerciseRepository.findAllByTrainingId(trainingId);

        if (exerciseEntities.isEmpty()) throw new ExercisesNotFound();

        List<TrainingExercicesDto> responseDtoList = new ArrayList<>();

        for (ExerciseEntity exerciseEntity : exerciseEntities) {
            responseDtoList.add(new TrainingExercicesDto(
                    exerciseEntity.getId(),
                    exerciseEntity.getName(),
                    exerciseEntity.getTraining().getId(),
                    exerciseEntity.getTraining().getDayOfWeek(),
                    exerciseEntity.getDescription(),
                    exerciseEntity.getRepetition(),
                    exerciseEntity.getWeight(),
                    exerciseEntity.getRepose()
            ));
        }

        return  responseDtoList;
    }
}
