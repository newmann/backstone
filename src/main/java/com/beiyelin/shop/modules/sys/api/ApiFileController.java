package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.utils.UploadExUtils;
import com.beiyelin.shop.modules.app.web.AppBaseController;
import com.beiyelin.shop.common.config.ResultCode;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by newmann on 2017/3/20.
 */
@Controller
@RequestMapping("${adminPath}/api/files")
public class ApiFileController extends AppBaseController {



    @RequestMapping("/uploads")
    public String savePerson(HttpServletRequest request, HttpServletResponse response) {
        boolean result;
        int resultCode;
        String message;
        Map<String, Object> data = Maps.newHashMap();
        UploadExUtils uploadExUtils;

        try {
            uploadExUtils = new UploadExUtils();
            List<String> getFile = uploadExUtils.uploadFile(request);
            data.put("save", getFile);
            result = true;
            resultCode = ResultCode.Success;
            message = "";
        } catch (Exception ex){
            result = false;
            resultCode = ResultCode.Success;
            message = ex.getMessage();
        }

        return renderString(response, result, resultCode, message, data);
    }
}
