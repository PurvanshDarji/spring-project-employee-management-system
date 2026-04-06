package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice_comment")
public class NoticeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String empName;
    private String comment;
    private LocalDateTime commentedAt;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCommentedAt() { return commentedAt; }
    public void setCommentedAt(LocalDateTime commentedAt) { this.commentedAt = commentedAt; }

    public Notice getNotice() { return notice; }
    public void setNotice(Notice notice) { this.notice = notice; }
}