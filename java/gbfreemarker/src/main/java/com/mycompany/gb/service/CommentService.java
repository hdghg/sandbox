package com.mycompany.gb.service;

import com.mycompany.gb.model.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAll();

    Comment findOne(long id);

    void delete(long id);

    Comment save(Comment comment);

}
