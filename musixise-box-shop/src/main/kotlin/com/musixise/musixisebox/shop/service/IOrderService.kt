package com.musixise.musixisebox.shop.service

import com.musixise.musixisebox.shop.rest.web.vo.req.OrderVO
import com.musixise.musixisebox.shop.rest.web.vo.req.PayVO

interface IOrderService {

    fun create(orderVO: OrderVO) : Long?

    fun pay(payVO: PayVO) : Boolean
}