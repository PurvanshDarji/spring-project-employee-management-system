package com.example.demo.repository;

import com.example.demo.entity.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Integer> {
}