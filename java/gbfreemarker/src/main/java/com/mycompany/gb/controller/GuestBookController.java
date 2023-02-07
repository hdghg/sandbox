package com.mycompany.gb.controller;

import com.mycompany.gb.model.domain.Comment;
import com.mycompany.gb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class GuestBookController {

    private final CommentService commentService;

    @Autowired
    public GuestBookController(CommentService commentService) {
        this.commentService = commentService;
    }


    @RequestMapping("/guestbook.html")
    public ModelAndView guestbookController() {
        ModelAndView mav = new ModelAndView("guestbook");
        List<Comment> all = commentService.findAll();
        mav.addObject("comments", all);
        return mav;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String post(@RequestParam String header, @RequestParam String body) {
        Comment comment = new Comment();
        comment.setBody(body);
        comment.setHeader(header);
        commentService.save(comment);
        return "redirect:/guestbook.html";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id) {
        commentService.delete(id);
        return "redirect:/guestbook.html";
    }
}
