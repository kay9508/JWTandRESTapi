package com.assignment.sinyoung.controller;

import com.assignment.sinyoung.dto.BoardResponseDto;
import com.assignment.sinyoung.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController_hateoas {

    private final BoardService boardService;

    @GetMapping("/board")
    public CollectionModel<EntityModel<BoardResponseDto.ListBoard>> listBoard() {
        List<EntityModel<BoardResponseDto.ListBoard>> boards = boardService.list().stream()
                .map(board -> EntityModel.of(board,
                        linkTo(methodOn(BoardController_hateoas.class).loadBoard(board.getId())).withSelfRel(),
                        linkTo(methodOn(BoardController_hateoas.class).listBoard()).withRel("board_list")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(boards,
                linkTo(methodOn(BoardController_hateoas.class).listBoard()).withSelfRel()
        );
    }
    @GetMapping( "/board/{boardPid}")
    public EntityModel<BoardResponseDto.LoadBoard> loadBoard(@PathVariable Long boardPid) {
        BoardResponseDto.LoadBoard boardResponseDto = boardService.load(boardPid);
        return EntityModel.of(boardResponseDto,
                linkTo(methodOn(BoardController_hateoas.class).loadBoard(boardPid)).withSelfRel(),
                linkTo(methodOn(BoardController_hateoas.class).listBoard()).withRel("board_list")
        );
    }
}
