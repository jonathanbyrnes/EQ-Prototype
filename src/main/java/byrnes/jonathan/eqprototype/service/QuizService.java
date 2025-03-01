package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateQuizDto;
import byrnes.jonathan.eqprototype.dto.EditQuizDto;
import byrnes.jonathan.eqprototype.dto.ShareQuizDto;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.repository.CategoryRepository;
import byrnes.jonathan.eqprototype.repository.QuizRepository;
import byrnes.jonathan.eqprototype.repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz create(String userId, String categoryId, CreateQuizDto createQuizDto) {

        Quiz quiz = new Quiz(userId, categoryId, createQuizDto.getTitle(), createQuizDto.getDescription(),
                createQuizDto.isActive(), createQuizDto.isQuestionsRandomised(), new Date(), createQuizDto.isInstantFeedback());

        return this.quizRepository.save(quiz);
    }

    public ShareQuizDto share(String quizId) {
        Quiz quiz = getQuizById(quizId);

        if (!quiz.isActive()) {
            throw new IllegalArgumentException("This quiz is not active.");
        }

        String link = "https://localhost:8080/api/join/" + quizId;

        //QR Code API
        byte[] qrCodeBytes = generateQRCode(link, 250, 250);
        String qrBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

        return new ShareQuizDto(link, qrBase64);
    }

    public Quiz edit(String quizId, EditQuizDto editQuizDto) {
        Quiz quiz = getQuizById(quizId);

        quiz.setTitle(editQuizDto.getTitle());
        quiz.setDescription(editQuizDto.getDescription());
        quiz.setActive(editQuizDto.isActive());

        return quizRepository.save(quiz);
    }

    public ResponseEntity<Void> delete(String quizId) {
        Quiz quiz = getQuizById(quizId);
        this.quizRepository.delete(quiz);

        return ResponseEntity.ok().build();
    }

    public Quiz reuse(String quizId) {
        Quiz quiz = getQuizById(quizId);

        Quiz newQuiz = new Quiz(
                quiz.getUserId(), quiz.getCategoryId(), quiz.getTitle(),
                quiz.getDescription(), quiz.isActive(), quiz.isQuestionsRandomised(), new Date(), quiz.isQuestionsRandomised()
        );

        return this.quizRepository.save(newQuiz);
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

    private Quiz getQuizById(String quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz cannot be found."));
    }

}
