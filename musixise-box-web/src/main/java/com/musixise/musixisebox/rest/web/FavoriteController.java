package com.musixise.musixisebox.rest.web;

import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.FavoriteApi;
import com.musixise.musixisebox.api.web.vo.req.favorite.CreateFavoriteVO;
import com.musixise.musixisebox.api.web.vo.resp.favorite.FavoriteVO;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.aop.MusixiseContext;
import com.musixise.musixisebox.server.domain.Favorite;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.manager.FavoriteManager;
import com.musixise.musixisebox.server.manager.WorkManager;
import com.musixise.musixisebox.server.repository.FavoriteRepository;
import com.musixise.musixisebox.server.repository.WorkRepository;
import com.musixise.musixisebox.server.service.FavoriteService;
import com.musixise.musixisebox.server.service.UserService;
import com.musixise.musixisebox.server.service.WorkService;
import com.musixise.musixisebox.server.transfter.FavoriteTransfter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by zhaowei on 2018/4/5.
 */
@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController implements FavoriteApi {

    @Resource
    FavoriteRepository favoriteRepository;

    @Resource
    WorkRepository workRepository;

    @Resource FavoriteService favoriteService;

    @Resource WorkService workService;

    @Resource UserService userService;

    @Resource WorkManager workManager;

    @Resource FavoriteManager favoriteManager;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse create(@RequestBody @Valid CreateFavoriteVO createFavoriteVO) {

        Long uid = MusixiseContext.getCurrentUid();
        Long workId = createFavoriteVO.getWorkId();
        Integer status = Optional.ofNullable(createFavoriteVO.getStatus()).orElse(1);

        Optional<Work> optional = workRepository.findById(workId);
        return optional.map(work -> {
            if (status == 1) {
                favoriteService.create(uid, workId);
            } else if (status == 0) {
                favoriteService.cancle(uid, workId);
            } else {
                return new MusixiseResponse(ExceptionMsg.PARAM_ERROR);
            }
            //更新收藏数
            workService.updateFavoriteCount(workId);
            return new MusixiseResponse(ExceptionMsg.SUCCESS);
        }).orElse(new MusixiseResponse(ExceptionMsg.NOT_EXIST));
    }

    @RequestMapping(value = "/getWorkList/{uid}", method = RequestMethod.GET)
    @AppMethod
    @Override
    public MusixisePageResponse<List<FavoriteVO>> getList(@PathVariable Long uid,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {

        //get favorite list
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Favorite> favorites = favoriteRepository.findAllByUserIdOrderByIdDesc(uid, pageable);

        //get work list po
        List<Long> workIds = favorites.stream().map(Favorite::getWorkId).collect(Collectors.toList());
        List<Work> workList = workManager.getWorkList(workIds);


        List<WorkVO> workListVO = workService.getWorkList(workList);
        Map<Long, WorkVO> workVOMap = workListVO.stream().collect(Collectors.toMap(WorkVO::getId, w -> w));


        List<FavoriteVO> favoriteVOList = new ArrayList<>();
        favorites.forEach(favorite -> {
            WorkVO workVO = workVOMap.get(favorite.getWorkId());
            if (workVO != null) {
                FavoriteVO favoriteWithUser = FavoriteTransfter.getFavoriteWithUser(workVO);
                favoriteVOList.add(favoriteWithUser);
            }
        });

        return new MusixisePageResponse<>(favoriteVOList, favorites.getTotalElements(), size, page);
    }

}
