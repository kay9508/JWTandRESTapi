package com.assignment.sinyoung.service;

import com.assignment.sinyoung.dto.BoardResponseDto;
import com.assignment.sinyoung.dto.TodoResponseDto;
import com.assignment.sinyoung.entity.Board;
import com.assignment.sinyoung.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    public BoardResponseDto.LoadBoard load(Long pid) {
        Optional<Board> load = boardRepository.findById(pid);
        if (load.isPresent()) {
            BoardResponseDto.LoadBoard rtnDto = modelMapper.map(load, BoardResponseDto.LoadBoard.class);
            return rtnDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글 입니다.");
        }
    }

    public List<BoardResponseDto.ListBoard> list() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponseDto.ListBoard> rtnList = modelMapper.map(boards, new TypeToken<List<BoardResponseDto.ListBoard>>(){}.getType());
        return rtnList;
    }
}
