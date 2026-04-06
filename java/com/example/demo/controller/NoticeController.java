package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Notice;
import com.example.demo.entity.NoticeComment;
import com.example.demo.entity.Employee;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.NoticeCommentRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class NoticeController {

    @Autowired private NoticeRepository noticeRepo;
    @Autowired private NoticeCommentRepository commentRepo;

    // ✅ Admin — Notice Board page
    @GetMapping("/admin/notices")
    public String adminNotices(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";
        model.addAttribute("notices", noticeRepo.findAllByOrderByPostedAtDesc());
        return "Admin Panel/Notices";
    }

    // ✅ Admin — Notice post karo
    @PostMapping("/admin/notices/post")
    @ResponseBody
    public String postNotice(@RequestParam String title,
                             @RequestParam String message,
                             @RequestParam String category,
                             HttpSession session) {
        if (session.getAttribute("admin") == null) return "UNAUTHORIZED";

        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setMessage(message);
        notice.setCategory(category);
        notice.setPostedBy("Admin");
        notice.setPostedAt(LocalDateTime.now());
        noticeRepo.save(notice);
        return "POSTED";
    }

    // ✅ Admin — Notice delete karo
    @DeleteMapping("/admin/notices/delete/{id}")
    @ResponseBody
    public String deleteNotice(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("admin") == null) return "UNAUTHORIZED";
        noticeRepo.deleteById(id);
        return "DELETED";
    }

    // ✅ Employee — Notice Board page
    @GetMapping("/employee/notices")
    public String employeeNotices(Model model, HttpSession session) {
        Employee emp = (Employee) session.getAttribute("user");
        if (emp == null) return "redirect:/login";
        model.addAttribute("notices", noticeRepo.findAllByOrderByPostedAtDesc());
        model.addAttribute("empName", emp.getName());
        return "User Panel/Notices";
    } 

    // ✅ Employee — Comment karo
    @PostMapping("/employee/notices/comment")
    @ResponseBody
    public String addComment(@RequestParam int noticeId,
                             @RequestParam String comment,
                             HttpSession session) {
        Employee emp = (Employee) session.getAttribute("user");
        if (emp == null) return "UNAUTHORIZED";

        Notice notice = noticeRepo.findById(noticeId).orElse(null);
        if (notice == null) return "NOT_FOUND";

        NoticeComment nc = new NoticeComment();
        nc.setEmpName(emp.getName());
        nc.setComment(comment);
        nc.setCommentedAt(LocalDateTime.now());
        nc.setNotice(notice);
        commentRepo.save(nc);
        return "COMMENTED";
    }

    // ✅ Get comments for a notice
    @GetMapping("/notices/comments/{noticeId}")
    @ResponseBody
    public List<NoticeComment> getComments(@PathVariable int noticeId) {
        Notice notice = noticeRepo.findById(noticeId).orElse(null);
        if (notice == null) return List.of();
        return notice.getComments();
    }
}