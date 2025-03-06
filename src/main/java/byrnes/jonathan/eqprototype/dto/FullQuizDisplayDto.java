package byrnes.jonathan.eqprototype.dto;


import byrnes.jonathan.eqprototype.model.Question;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.model.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class FullQuizDisplayDto {

    @NonNull
    private Quiz quiz;

    @NonNull
    private List<Question> questionList;

    @NonNull
    private List<Type> typeList;

    @NonNull
    private String categoryName;
}
