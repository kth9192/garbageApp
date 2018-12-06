package com.taehoon.garbagealarm.model.cleanhouse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kth919 on 2017-11-04.
 */

public class NGeoDomain {

    @SerializedName("result")
    private NgeoCodeModel houseList;

    public NgeoCodeModel getHouseList() {
        return houseList;
    }
}

//
////제작 당시 JSON 구조
//{
//        "result": {
//            "total": 1,
//            "userquery": "제주특별자치도 제주시 일도2동 995-6",
//            "items": [
//                {
//                    "address": "제주특별자치도 제주시 일도2동 995-6",
//                    "addrdetail": {
//                    "country": "대한민국",
//                    "sido": "제주특별자치도",
//                    "sigugun": "제주시",
//                    "dongmyun": "일도2동",
//                    "ri": "",
//                    "rest": "995-6"
//                    },
//                    "isRoadAddress": false,
//                    "point": {
//                    "x": 126.5329714,
//                    "y": 33.5120978
//                    }
//                    }
//                ]
//            }
//}