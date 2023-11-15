package com.example.chapter6.service;

import com.example.chapter6.model.BoardVO;
import com.example.chapter6.model.SearchHelper;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    List<BoardVO> selectBoardVO(SearchHelper searchHelper);

    void insertBoardVO(BoardVO boardVO);

    void updateBoardVO(BoardVO boardVO);

    Optional<BoardVO> selectBoardVOById(int id);
}
