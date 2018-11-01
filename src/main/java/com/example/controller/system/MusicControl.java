package com.example.controller.system;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.config.util.Result;
import com.example.entity.Article;
import com.example.entity.Music;
import com.example.entity.UserAccount;
import com.example.service.MusicService;
import com.example.service.impl.MusicServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/sys/music")
public class MusicControl {

    @Autowired
    public MusicServiceImpl musicService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result List(HttpServletRequest request, HttpServletResponse response, Article article, UserAccount userAccount) {
        String pageN= request.getParameter("page");
        if(StringUtils.isEmpty(pageN)){
            pageN = "1";
        }
        Page page = new Page();
        page.setCurrent(Integer.parseInt(pageN));
        page.setSize(10);
        Page result = musicService.selectPage(page,new EntityWrapper<Music>().orderBy("create_time",false));
        HashMap map = new HashMap();
        map.put("totalPage",result.getPages());
        map.put("current",result.getCurrent());
        map.put("totalSize",result.getTotal());
        map.put("musicList",result.getRecords());
        return  Result.success(map);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        musicService.deleteById(Integer.parseInt(id));
        return  Result.success(null);
    }
}
