package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.bean.ApiResponseData;
import com.beiyelin.shop.common.utils.UploadExUtils;
import com.beiyelin.shop.modules.app.web.AppBaseController;
import com.beiyelin.shop.common.config.ResultCode;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by newmann on 2017/3/20.
 */
@RestController
@RequestMapping("${adminPath}/api/files")
public class ApiFileController extends ApiBaseController {



    @RequestMapping("/uploads")
    public ApiResponseData savePerson(HttpServletRequest request, HttpServletResponse response) {
        UploadExUtils uploadExUtils;

        try {
            uploadExUtils = new UploadExUtils();
            List<String> getFile = uploadExUtils.uploadFile(request);

            responseData.pushData("save", getFile);
            responseData.setSuccessMessage("");
        } catch (Exception ex){
            responseData.setErrorMessage(ex.getMessage());
        }

        return responseData;
    }
}
