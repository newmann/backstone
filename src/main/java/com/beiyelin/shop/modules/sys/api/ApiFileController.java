package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.utils.UploadExUtils;
import com.beiyelin.shop.modules.person.api.ApiPersonBaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by newmann on 2017/3/20.
 */
@RestController
@RequestMapping("/api/files")
public class ApiFileController extends ApiPersonBaseController {


    //TODO

    @RequestMapping("/uploads")
    public List<String> savePerson(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        UploadExUtils uploadExUtils;

            uploadExUtils = new UploadExUtils();
            List<String> getFile = uploadExUtils.uploadFile(request);

            return getFile;
    }
}
