package com.mycompany.gb.dao.impl;

import com.mycompany.gb.config.Constants;
import com.mycompany.gb.dao.CommentDao;
import com.mycompany.gb.model.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
public class ExternalServiceCommentDao implements CommentDao {

    private final String endpoint;
    private final RestTemplate restTemplate;

    @Autowired
    public ExternalServiceCommentDao(Environment environment, RestTemplate restTemplate) {
        this.endpoint = environment.getRequiredProperty(Constants.EXTERNAL_CRUD);
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Comment> findAll() {
        Comment[] comments = restTemplate.getForObject(endpoint, Comment[].class);
        return Arrays.asList(comments);
    }

    @Override
    public Comment findOne(long id) {
        return restTemplate.getForObject(endpoint + "/" + id, Comment.class);
    }

    @Override
    public void delete(long id) {
        restTemplate.delete(endpoint + "/" + id);
    }

    @Override
    public Comment save(Comment comment) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("header", comment.getHeader());
        params.add("body", comment.getBody());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        HttpEntity<MultiValueMap<String, String>> entity =
                new HttpEntity<MultiValueMap<String, String>>(params, httpHeaders);
        return restTemplate.postForObject(endpoint, entity, Comment.class);
    }
}
