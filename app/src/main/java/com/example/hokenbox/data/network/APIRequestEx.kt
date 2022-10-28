package com.example.hokenbox.data.network

import com.example.hokenbox.data.response.BaseResponse
import kotlinx.coroutines.flow.Flow

//Request
//fun APIRequest.getShopInfo(id: Int): Flow<BaseResponse> = request(ApiRouter(APIPath.shopInfo(id)))
////    request(ApiRouter(APIPath.shopInfo(id), parameters = toMap(request).filterNotNullValues().toHashMap()))

fun APIRequest.getCommon(): Flow<Response<BaseResponse>> =
    request(ApiRouter(APIPath.common()))