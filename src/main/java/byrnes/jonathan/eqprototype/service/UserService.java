package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.*;
import byrnes.jonathan.eqprototype.model.*;
import byrnes.jonathan.eqprototype.repository.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        LinkedRole linkedRole = new LinkedRole(role);

        User user = new User(
                linkedRole,
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPassword(),
                new Date(),
                false);

        linkedRole.setUser(user);
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

        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token, expiryDate);
        this.passwordResetTokenRepository.save(passwordResetToken);

        return token;
    }

    public User resetPassword(String token, ResetPasswordDto resetPasswordDto) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token is invalid, or no longer valid."));

        if (passwordResetToken.getExpiryDate().before(new Date())) {
            throw new IllegalArgumentException("Token is invalid, or no longer valid.");
        }

        User user = getUserById(passwordResetToken.getUser().getId());

        user.setPassword(resetPasswordDto.getPassword());
        this.passwordResetTokenRepository.delete(passwordResetToken);
        return this.userRepository.save(user);
    }

    public LinkedQuiz joinQuiz(String userId, String quizId) {
        User user = getUserById(userId);
        Quiz quiz = getQuizById(quizId);

        LinkedQuiz linkedQuiz = new LinkedQuiz(
                user, quiz, new Date(), "IN PROGRESS", 0, new Date(), new Date()
        );

        return this.linkedQuizRepository.save(linkedQuiz);
    }

    public Response submitResponse(String linkedQuizId, String questionId, ResponseDto responseDto) {
        LinkedQuiz linkedQuiz = getLinkedQuizById(linkedQuizId);
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
                linkedQuiz,
                question,
                responseDto.getResponse(),
                isCorrect,
                submissionTime
        );

        return this.responseRepository.save(response);
    }

    public QuizSummaryDto completeQuiz(String linkedQuizId) {
        LinkedQuiz linkedQuiz = getLinkedQuizById(linkedQuizId);

        if(!linkedQuiz.getStatus().equals("COMPLETE")) {
            linkedQuiz.setStatus("COMPLETE");
            this.linkedQuizRepository.save(linkedQuiz);
        }

        List<Response> responses = responseRepository.findByLinkedQuizId(linkedQuizId);
        int totalQuestions = responses.size();

        int correctAnswers = 0;
        for (Response response : responses) {
            if (response.isCorrect()) {
                correctAnswers++;
            }
        }

        return new QuizSummaryDto(
                linkedQuizId, totalQuestions, correctAnswers, linkedQuiz.getScore());
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

    private Quiz getQuizById(String quizId) {
        return this.quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("This quiz cannot be found."));
    }

    private LinkedQuiz getLinkedQuizById(String linkedQuizId) {
        return this.linkedQuizRepository.findById(linkedQuizId)
                .orElseThrow(() -> new IllegalArgumentException("This linked quiz cannot be found."));
    }

    private Question getQuestionById(String questionId) {
        return this.questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("This question cannot be found."));
    }

}
