package lee.hawoob.finalproject.dto;

import lee.hawoob.finalproject.entity.Board;
//import MBTI.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBoardDto {

    private Long boardIndex;

    private String title;

    private String user;

    private String content;

    private LocalDateTime date;

}