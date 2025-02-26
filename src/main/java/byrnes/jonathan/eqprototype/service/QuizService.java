package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateQuizDto;
import byrnes.jonathan.eqprototype.dto.EditQuizDto;
import byrnes.jonathan.eqprototype.dto.ShareQuizDto;
import byrnes.jonathan.eqprototype.model.Category;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.model.User;
import byrnes.jonathan.eqprototype.repository.CategoryRepository;
import byrnes.jonathan.eqprototype.repository.QuizRepository;
import byrnes.jonathan.eqprototype.repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Quiz create(String userId, String categoryId, CreateQuizDto createQuizDto) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("User cannot be found.");
        }
        User user = userOptional.get();

        Optional<Category> categoryOptional = this.categoryRepository.findById(categoryId);
        if(categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be found.");
        }
        Category category = categoryOptional.get();

        Quiz quiz = new Quiz(user, category, createQuizDto.getTitle(), createQuizDto.getDescription(),
                createQuizDto.isActive(), new Date());

        return this.quizRepository.save(quiz);
    }

    public ShareQuizDto share(String quizId) {
        Optional<Quiz> quizOptional = this.quizRepository.findById(quizId);
        if(quizOptional.isEmpty()) {
            throw new IllegalArgumentException("Quiz cannot be found.");
        }
        Quiz quiz = quizOptional.get();

        if(!quiz.isActive()) {
            throw new IllegalArgumentException("This quiz is not active.");
        }

        String link = "https://localhost:8080/api/join/" + quizId;

        //QR Code API
        byte[] qrCodeBytes = generateQRCode(link, 250, 250);
        String qrBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

        return new ShareQuizDto(link, qrBase64);
    }

    public Quiz edit(String quizId, EditQuizDto editQuizDto) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isEmpty()) {
            throw new IllegalArgumentException("Cannot find quiz.");
        }

        Quiz quiz = quizOptional.get();

        quiz.setTitle(editQuizDto.getTitle());
        quiz.setDescription(editQuizDto.getDescription());
        quiz.setActive(editQuizDto.isActive());

        return quizRepository.save(quiz);
    }

    private byte[] generateQRCode(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Cannot generate QR code.", e);
        }
    }

}
