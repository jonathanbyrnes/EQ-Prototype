package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.*;
import byrnes.jonathan.eqprototype.model.*;
import byrnes.jonathan.eqprototype.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LinkedRoleRepository linkedRoleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final QuizRepository quizRepository;
    private final LinkedQuizRepository linkedQuizRepository;
    private final QuestionRepository questionRepository;
    private final ResponseRepository responseRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, LinkedRoleRepository linkedRoleRepository, PasswordResetTokenRepository passwordResetTokenRepository, QuizRepository quizRepository, LinkedQuizRepository linkedQuizRepository, QuestionRepository questionRepository, ResponseRepository responseRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.linkedRoleRepository = linkedRoleRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.quizRepository = quizRepository;
        this.linkedQuizRepository = linkedQuizRepository;
        this.questionRepository = questionRepository;
        this.responseRepository = responseRepository;
    }

    public User register(UserRegistrationDto userRegistrationDto) {
        Optional<User> userOptional = this.userRepository.findByEmail(userRegistrationDto.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException("This email already exists.");
        }

        Role role = getRoleByName("USER");

        LinkedRole linkedRole = new LinkedRole("null", role.getId());

        User user = new User(
                linkedRole.getId(),
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPassword(),
                new Date(),
                false);

        linkedRole.setUserId(user.getId());
        this.linkedRoleRepository.save(linkedRole);
        return this.userRepository.save(user);
    }

    public User login(UserLoginDto userLoginDto) {
        User user = getUserByEmail(userLoginDto.getEmail());

        if (userLoginDto.getEmail().equals(user.getEmail())) {
            if (userLoginDto.getPassword().equals(user.getPassword())) {
                System.out.println("Successfully logged in User: " + user.getEmail());
                return user;
            }
        }
        throw new IllegalArgumentException("Email/Password Incorrect.");
    }

    public User update(String userId, UserUpdateDto userUpdateDto) {
        User user = getUserById(userId);

        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isBlank()) {
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank()) {
            user.setPassword(userUpdateDto.getPassword());
        }

        return this.userRepository.save(user);
    }

    public String generatePasswordResetToken(GeneratePasswordResetTokenDto generatePasswordResetTokenDto) {
        User user = getUserByEmail(generatePasswordResetTokenDto.getEmail());

        String token = UUID.randomUUID().toString();
        Date expiryDate = new Date(System.currentTimeMillis() + 3600 * 1000); //1h from now

        PasswordResetToken passwordResetToken = new PasswordResetToken(user.getId(), token, expiryDate);
        this.passwordResetTokenRepository.save(passwordResetToken);

        return token;
    }

    public User resetPassword(String token, ResetPasswordDto resetPasswordDto) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token is invalid, or no longer valid."));

        if (passwordResetToken.getExpiryDate().before(new Date())) {
            throw new IllegalArgumentException("Token is invalid, or no longer valid.");
        }

        User user = getUserById(passwordResetToken.getUserId());

        user.setPassword(resetPasswordDto.getPassword());
        this.passwordResetTokenRepository.delete(passwordResetToken);
        return this.userRepository.save(user);
    }

    public LinkedQuiz joinQuiz(String userId, String quizId) {
        if(userId.isEmpty()) {
            User user = createGuestUser();
            userId = user.getId();
        }

        LinkedQuiz linkedQuiz = new LinkedQuiz(
                userId, quizId, new Date(), "IN PROGRESS", 0, new Date(), new Date()
        );

        return this.linkedQuizRepository.save(linkedQuiz);
    }

    private User createGuestUser() {
        Role role = getRoleByName("USER");
        LinkedRole linkedRole = new LinkedRole("null", role.getId());

        User user = new User(
                linkedRole.getId(),
                "guestuser@example.com",
                "",
                new Date(),
                true);

        linkedRole.setUserId(user.getId());
        this.linkedRoleRepository.save(linkedRole);
        return this.userRepository.save(user);
    }

    public Response submitResponse(String quizId, String userId, String questionId, ResponseDto responseDto) {
        LinkedQuiz linkedQuiz = getLinkedQuizByQuizIdAndUserId(quizId, userId);
        Question question = getQuestionById(questionId);
        linkedQuiz.setLastActivityTime(new Date());

        Date questionStartTime = linkedQuiz.getCurrentQStartTime();
        Date submissionTime = new Date();
        long elapsedMillis = submissionTime.getTime() - questionStartTime.getTime();
        long timeLimitMillis = question.getTimeLimit() * 1000L;

        boolean isWithinTime = elapsedMillis <= timeLimitMillis;

        boolean isCorrect = question.getAnswers().contains(responseDto.getResponse())
                && isWithinTime;

        if (isCorrect) {
            linkedQuiz.setScore(linkedQuiz.getScore() + question.getWorth());
            this.linkedQuizRepository.save(linkedQuiz);
        }

        Response response = new Response(
                linkedQuiz.getId(),
                questionId,
                responseDto.getResponse(),
                isCorrect,
                submissionTime
        );

        return this.responseRepository.save(response);
    }

    public QuizSummaryDto completeQuiz(String quizId, String userId) {
        LinkedQuiz linkedQuiz = getLinkedQuizByQuizIdAndUserId(quizId, userId);

        if (!linkedQuiz.getStatus().equals("COMPLETE")) {
            linkedQuiz.setStatus("COMPLETE");
            this.linkedQuizRepository.save(linkedQuiz);
        }

        List<Response> responses = responseRepository.findByLinkedQuizId(linkedQuiz.getId());
        int totalQuestions = responses.size();
        List<Question> questions = questionRepository.findByQuizId(linkedQuiz.getQuizId());

        int correctAnswers = 0;
        for (Response response : responses) {
            if (response.isCorrect()) {
                correctAnswers++;
            }
        }

        return new QuizSummaryDto(
                linkedQuiz.getId(), totalQuestions, correctAnswers, linkedQuiz.getScore(),
                questions, responses);
    }

    public List<QuizSummaryDto> getAllResults(String userId) {
        List<LinkedQuiz> linkedQuizzes = this.linkedQuizRepository.findByUserId(userId);

        return linkedQuizzes.stream().map(linkedQuiz -> {
            List<Response> responses = responseRepository.findByLinkedQuizId(linkedQuiz.getId());
            long correctAnswers = responses.stream().filter(Response::isCorrect).count();

            return new QuizSummaryDto(
                    linkedQuiz.getId(),
                    (int) this.linkedQuizRepository.countByQuizId(linkedQuiz.getQuizId()),
                    (int) correctAnswers,
                    linkedQuiz.getScore(),
                    new ArrayList<>(),
                    responses);
        }).collect(Collectors.toList());
    }

    public List<Quiz> getAllQuizzes(String userId) {
        return this.quizRepository.findByUserId(userId);
    }

    public QuizAggregateDto getQuizAggregate(String quizId) {
        List<LinkedQuiz> allAttempts = linkedQuizRepository.findByQuizId(quizId);
        int totalStarted = allAttempts.size();

        List<LinkedQuiz> completedAttempts = allAttempts.stream()
                .filter(quiz -> "COMPLETE".equals(quiz.getStatus()))
                .toList();
        int totalCompleted = completedAttempts.size();

        int sumScore = 0;
        int highestScore = 0;
        int lowestScore = Integer.MAX_VALUE;

        for (LinkedQuiz attempt : completedAttempts) {
            int score = attempt.getScore();
            sumScore += score;
            if (score > highestScore) {
                highestScore = score;
            }
            if (score < lowestScore) {
                lowestScore = score;
            }
        }

        double averageScore = totalCompleted > 0 ? (double) sumScore / totalCompleted : 0;
        if (totalCompleted == 0) {
            lowestScore = 0;
        }

        return new QuizAggregateDto(quizId, totalStarted, totalCompleted, averageScore, highestScore, lowestScore);
    }

    private User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("This email cannot be found."));
    }

    private User getUserById(String userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("This user cannot be found."));
    }

    private Role getRoleByName(String roleName) {
        return this.roleRepository.findByName(roleName)
                .orElseGet(() -> this.roleRepository.save(new Role(roleName)));
    }

    private LinkedQuiz getLinkedQuizById(String linkedQuizId) {
        return this.linkedQuizRepository.findById(linkedQuizId)
                .orElseThrow(() -> new IllegalArgumentException("This linked quiz cannot be found."));
    }

    private LinkedQuiz getLinkedQuizByQuizIdAndUserId(String quizId, String userId) {
        return this.linkedQuizRepository.findByQuizIdAndUserId(quizId, userId);
    }

    private Question getQuestionById(String questionId) {
        return this.questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("This question cannot be found."));
    }

}
