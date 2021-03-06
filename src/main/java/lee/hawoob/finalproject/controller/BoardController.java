package lee.hawoob.finalproject.controller;

import lee.hawoob.finalproject.auth.PrincipalDetails;
import lee.hawoob.finalproject.dto.BoardDto;
import lee.hawoob.finalproject.dto.SearchBoardDto;
import lee.hawoob.finalproject.entity.Board;
import lee.hawoob.finalproject.form.CreatePostForm;
import lee.hawoob.finalproject.form.UpdateBoardForm;
import lee.hawoob.finalproject.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
@Transactional
public class BoardController {

    private final BoardService service;

    @GetMapping("/list")
    public String boardView(@PageableDefault Pageable pageable, Model model){
        Page<SearchBoardDto> boardList1 = service.getBoardList(pageable);
        model.addAttribute("boardList1", boardList1);

        return "board/list";
    }

    @GetMapping("search")
    public String searchBoard(@RequestParam("keyword") String keyword,Pageable pageable , Model model){
        Page<SearchBoardDto> boardList = service.searchBoard(keyword, pageable);
        model.getAttribute("keyword");
        model.addAttribute("boardList1", boardList);

        return "board/list";
    }

    @GetMapping("/detail/{boardIndex}")
    public ModelAndView detailBoard(@PathVariable Long boardIndex, ModelAndView mav, @AuthenticationPrincipal PrincipalDetails custom){
        Optional<Board> board = service.findByIndex(boardIndex);
        BoardDto dto =service.getBoardDto(board.get());
        service.updateView(boardIndex); // views ++

        mav.setViewName("board/detailsPost");
        mav.addObject("dto", dto);
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create(@ModelAttribute CreatePostForm form, ModelAndView mav){
        mav.addObject("form", form);
        mav.setViewName("board/createPost");
        return mav;
    }

    @PostMapping("/create")
    public String createBoard(@ModelAttribute CreatePostForm form,@AuthenticationPrincipal PrincipalDetails custom, Model model){
        model.addAttribute("form", form);
        service.createBoard(form, custom);
        return "redirect:/board/list";
    }

    @RequestMapping("/delete/{boardIndex}")
    public String deleteBoard(@PathVariable Long boardIndex){
        service.deleteBoard(boardIndex);

        return "redirect:/board/list";
    }

    @GetMapping("update/{boardIndex}")
    public ModelAndView updateBoard(@PathVariable Long boardIndex, ModelAndView mav){
        Optional<Board> board = service.findByIndex(boardIndex);
        UpdateBoardForm form = new UpdateBoardForm();

        form.setBoardIndex(board.get().getBoardIndex());
        form.setTitle(board.get().getTitle());
        form.setContent(board.get().getContent());
        form.setDate(board.get().getCreateDate());

        mav.setViewName("/board/updatePost");
        mav.addObject("form", form);

        return mav;
    }

    @PostMapping("/update")
    public ModelAndView updateBoard(@ModelAttribute UpdateBoardForm form, ModelAndView mav){

        mav.setViewName("board/updatePost");
        mav.addObject("form", form);
        service.updateBoard(form);


        mav = new ModelAndView("redirect:/board/list");
        return mav;
    }
}
