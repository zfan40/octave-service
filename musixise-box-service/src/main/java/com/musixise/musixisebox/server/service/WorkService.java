package com.musixise.musixisebox.server.service;


import com.musixise.musixisebox.api.web.vo.req.home.QueryWork;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.Work;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by zhaowei on 2018/4/4.
 */
public interface WorkService {

    WorkVO getListByUid(Long uid);

    void updateFavoriteCount(Long workId);

    List<WorkVO> getWorkList(List<Work> workList);

    Boolean saveMidiFile(byte[] bt, String file);

    Page<Work> getRecommends(QueryWork queryWork,  int page, int limit);

}
