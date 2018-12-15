package com.musixise.musixisebox.shop.rest.web

import com.musixise.musixisebox.api.enums.ExceptionMsg
import com.musixise.musixisebox.api.result.MusixiseResponse
import com.musixise.musixisebox.server.aop.AppMethod
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderVO
import com.musixise.musixisebox.shop.rest.web.vo.req.PayVO
import com.musixise.musixisebox.shop.service.IOrderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/shop/orders")
class MyOrderController {

    @Resource
    lateinit var iOrderService: IOrderService;

//    @RequestMapping("/create", consumes=arrayOf(MediaType.APPLICATION_JSON_VALUE)
//        , produces =  arrayOf(MediaType.APPLICATION_JSON_VALUE), method = arrayOf(RequestMethod.POST))
    @PostMapping("/create")
    @AppMethod(isLogin = false)
    fun create( @Valid @RequestBody orderVO: OrderVO) : MusixiseResponse<Long> {

        val orderId = iOrderService.create(orderVO)
        return MusixiseResponse<Long>(ExceptionMsg.SUCCESS, orderId)
    }

    @PostMapping("/pay")
    @AppMethod(isLogin = true)
    fun pay(userId: Long, @Valid @RequestBody payVO: PayVO) : MusixiseResponse<Boolean> {

        val pay = iOrderService.pay(payVO)
        return MusixiseResponse<Boolean>(ExceptionMsg.SUCCESS, pay);
    }
}