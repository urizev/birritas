package com.urizev.birritas.data.mappers;

import com.urizev.birritas.data.api.ApiService;

class CommonMapper {
    private static final int NOT_VERIFIED = -1;
    private static final int VERIFIED = 0;

    static Float mapFloat(String str) {
        if (str == null) {
            return null;
        }
        return Float.valueOf(str);
    }

    static Integer mapInteger(String str) {
        if (str == null) {
            return null;
        }
        return Integer.valueOf(str);
    }

    static int mapStatus(String status) {
        if (status == null) {
            return NOT_VERIFIED;
        }
        switch (status) {
            case ApiService.STATUS_VERIFIED:
                return VERIFIED;
        }

        return NOT_VERIFIED;
    }
}
