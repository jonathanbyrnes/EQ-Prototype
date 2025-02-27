package byrnes.jonathan.eqprototype.model;

import byrnes.jonathan.eqprototype.model.LinkedQuiz;
import byrnes.jonathan.eqprototype.repository.LinkedQuizRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public class QuizPauseScheduler {

    private final LinkedQuizRepository linkedQuizRepository;

    public QuizPauseScheduler(LinkedQuizRepository linkedQuizRepository) {
        this.linkedQuizRepository = linkedQuizRepository;
    }

    // 10m
    @Scheduled(fixedRate = 600000)
    public void autoPauseInactiveQuizzes() {
        long inactivityThresholdMillis = 15 * 60 * 1000; //15m
        Date now = new Date();

        List<LinkedQuiz> inProgressQuizzes = this.linkedQuizRepository.findByStatus("IN PROGRESS");

        for (LinkedQuiz quiz : inProgressQuizzes) {
            if (now.getTime() - quiz.getLastActivityTime().getTime() > inactivityThresholdMillis) {
                quiz.setStatus("PAUSED");
                this.linkedQuizRepository.save(quiz);
            }
        }
    }

}
