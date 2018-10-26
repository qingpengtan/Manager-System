package com.example.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.dao.MusicDao;
import com.example.entity.Music;
import com.example.service.MusicService;
import org.springframework.stereotype.Service;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicDao, Music> implements MusicService {
}
