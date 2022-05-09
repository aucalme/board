package com.spring.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.board.HomeController;
import com.spring.board.service.boardService;
import com.spring.board.vo.BoardVo;
import com.spring.board.vo.PageVo;
import com.spring.common.CommonUtil;

@Controller
public class BoardController {
	
	@Autowired 
	boardService boardService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/board/boardList.do", method = RequestMethod.GET)
	public String boardList(Locale locale, Model model,PageVo pageVo) throws Exception{
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		
		int page = 1;
		int totalCnt = 0;
		
		if(pageVo.getPageNo() == 0){
			pageVo.setPageNo(page);;
		}
		
		boardList = boardService.SelectBoardList(pageVo);
		totalCnt = boardService.selectBoardCnt();
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("pageNo", page);
		
		return "board/boardList";
	}
	
	@RequestMapping(value = "/board/{boardType}/{boardNum}/boardView.do", method = RequestMethod.GET)
	public String boardView(Locale locale, Model model
			,@PathVariable("boardType")String boardType
			,@PathVariable("boardNum")int boardNum) throws Exception{
		
		BoardVo boardVo = new BoardVo();
		
		
		boardVo = boardService.selectBoard(boardType,boardNum);
		
		model.addAttribute("boardType", boardType);
		model.addAttribute("boardNum", boardNum);
		model.addAttribute("board", boardVo);
		
		return "board/boardView";
	}
	
	@RequestMapping(value = "/board/boardWrite.do", method = RequestMethod.GET)
	public String boardWrite(Locale locale, Model model) throws Exception{
		
		return "board/boardWrite";
	}
	
	@RequestMapping(value = "/board/boardWriteAction.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardWriteAction(Locale locale,BoardVo boardVo) throws Exception{
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		
		String boardTitle = boardVo.getBoardTitle();
		String boardComment = boardVo.getBoardComment();
		
		String[] titleArry = boardTitle.split(",");
		String[] commentArry = boardComment.split(",");
		
		HashMap<String, String> result = new HashMap<String, String>();
		CommonUtil commonUtil = new CommonUtil();
		
		for(int i = 0; i < titleArry.length; i++) {
			boardVo.setBoardTitle(titleArry[i]);
					
			boardVo.setBoardComment(commentArry[i]);				
			int resultCnt = boardService.boardInsert(boardVo);
						
			result.put("success", (resultCnt > 0)?"Y":"N");	
		}
		
		String callbackMsg = commonUtil.getJsonCallBackString(" ",result);
		System.out.println("callbackMsg::"+callbackMsg);
		return callbackMsg;
	}
	
	@RequestMapping(value = "/board/boardUpdate.do", method = RequestMethod.POST)
	@ResponseBody
	public String boardUpdate(BoardVo boardVo) throws Exception {
		HashMap<String, String> result = new HashMap<String, String>();
		CommonUtil commonUtil = new CommonUtil();
		
		int resultCnt = boardService.boardUpdate(boardVo);
		
		result.put("success", (resultCnt > 0)?"Y":"N");
		String callbackMsg = commonUtil.getJsonCallBackString(" ", result);
		
		return callbackMsg;
	}
	
	// 수정 페이지로 이동
	@RequestMapping(value = "/board/update.do", method = RequestMethod.GET)
	public String boardUpdate(int boardNum, Model model) throws Exception {
		BoardVo boardVo = boardService.selectBoard("1", boardNum);
		model.addAttribute("boardVo", boardVo);
		return "/board/boardUpdate";
	}
	
	// 수정
	@RequestMapping(value = "/board/update.do", method = RequestMethod.POST)
	public String postUpdate(BoardVo boardVo) throws Exception {
		boardService.boardUpdate(boardVo);
		return "redirect:/board/boardList.do";
	}
	
	// 삭제
	@RequestMapping(value = "/board/delete.do", method = RequestMethod.GET)
	public String boardDelete(@RequestParam("boardNum")int boardNum, 
			Model model) throws Exception {
		
		int result = boardService.boardDelete(boardNum);
		if(result > 0) {
			model.addAttribute("msg", "deleteSuccess");
			return "redirect:/board/boardList.do";
		}else {
			model.addAttribute("msg", "deleteFail");
			return "redirect:/board/boardList.do";
		}
	}
}
