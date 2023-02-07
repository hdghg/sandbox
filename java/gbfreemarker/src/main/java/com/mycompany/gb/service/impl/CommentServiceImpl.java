package com.mycompany.gb.service.impl;

import com.mycompany.gb.dao.CommentDao;
import com.mycompany.gb.model.domain.Comment;
import com.mycompany.gb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public List<Comment> findAll() {
        return commentDao.findAll();
    }

    @Override
    public Comment findOne(long id) {
        return commentDao.findOne(id);
    }

    @Override
    public void delete(long id) {
        commentDao.delete(id);
    }

    @Override
    public Comment save(Comment comment) {
        if (StringUtils.hasText(comment.getBody()) && StringUtils.hasText(comment.getHeader())) {
            return commentDao.save(comment);
        } else {
            return null;
        }
    }
}
