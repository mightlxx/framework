package com.mightlin.web.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.mightlin.common.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

@Slf4j
public class ResponseUtil {

    public static void printJson(ServletResponse response, BaseResponse res){
        PrintWriter out = null;
        try {
            response.setCharacterEncoding(CharsetUtil.defaultCharsetName());
            response.setContentType(ContentType.JSON.getValue());
            out = response.getWriter();
            out.println(JSONUtil.toJsonStr(res));
        } catch (Exception e) {
            log.error("【JSON输出异常】"+e);
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }
}
