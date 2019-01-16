package com.example.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.dao.CommentsDao;
import com.example.entity.Comments;
import com.example.service.CommentsService;
import org.springframework.stereotype.Service;

@Service
public class CommentsServiceImpl  extends ServiceImpl<CommentsDao, Comments> implements CommentsService {
}
