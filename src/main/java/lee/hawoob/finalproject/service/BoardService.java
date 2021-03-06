package lee.hawoob.finalproject.service;

import lee.hawoob.finalproject.auth.PrincipalDetails;
import lee.hawoob.finalproject.dto.BoardDto;
import lee.hawoob.finalproject.entity.Board;
import lee.hawoob.finalproject.dto.SearchBoardDto;
import lee.hawoob.finalproject.entity.User;
import lee.hawoob.finalproject.form.CreatePostForm;
import lee.hawoob.finalproject.form.UpdateBoardForm;
import lee.hawoob.finalproject.repository.BoardRepository;
import lee.hawoob.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository repository;
    private final UserRepository userRepository;

    public Page<SearchBoardDto> getBoardList(Pageable pageable){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardIndex"));
        Page<SearchBoardDto> dto = repository.findAll(pageable).map(b -> new SearchBoardDto(b));

        return dto;
    }

    @Transactional
    public int updateView(Long boardIndex) {
        return repository.updateView(boardIndex);
    }

    public Page<SearchBoardDto> searchBoard(String keyword, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "boardIndex")); // <- Sort 추가
        Page<SearchBoardDto> dto = repository.findByBoardTitleAndPostContentContaining(keyword, pageable).map(b -> new SearchBoardDto(b));

        return dto;
    }

    public BoardDto getBoardDto(Board board) {
        BoardDto dto = new BoardDto();

        dto.setBoardIndex(board.getBoardIndex());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setDate(board.getCreateDate());
        dto.setUser(board.getUser());

        return dto;
    }

    public Optional<Board> findByIndex(Long boardIndex){
        return repository.findById(boardIndex);
    }

    public void createBoard(CreatePostForm form, @AuthenticationPrincipal PrincipalDetails custom) {
        Board board = new Board();
        User user = userRepository.findById(custom.getUser().getUser_id()).get();

        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setUser(user);

        repository.save(board);
    }

    public void deleteBoard(Long boardIndex) {
        repository.deleteById(boardIndex);

    }

    public void updateBoard(UpdateBoardForm form){
        Optional<Board> opBoard = repository.findById(form.getBoardIndex());
        Board board = opBoard.get();

        board.setBoardIndex(form.getBoardIndex());
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

        repository.save(board);
    }
}



