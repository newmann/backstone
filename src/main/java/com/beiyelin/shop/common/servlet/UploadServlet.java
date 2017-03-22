package com.beiyelin.shop.common.servlet;

import com.beiyelin.shop.common.bean.ApiResponseData;
import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.config.ResultCode;
import com.beiyelin.shop.common.utils.FileUtils;
import com.beiyelin.shop.common.utils.IdGen;
import com.beiyelin.shop.common.utils.JsonUtils;
import com.beiyelin.shop.common.utils.ResponseUtils;
import com.google.common.base.Strings;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.util.UriUtils;
import sun.nio.ch.IOUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

/**
 * 查看CK上传的图片
 * @author Tony Wong
 * @version 2014-06-25
 */
@WebServlet("/upload")
@MultipartConfig(maxRequestSize = 1024L * 1024* 1024,maxFileSize = 1024*1024*100)
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());



	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ResponseUtils.setJsonResponse(resp);
		ApiResponseData responseData = new ApiResponseData();

		PrintWriter writer = resp.getWriter();
		responseData.setResult(false);
		responseData.setResultCode(ResultCode.Failure);
		responseData.setMessage("不支持Get操作！");
		writer.write(responseData.toString());
		writer.flush();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		Collection<Part> parts = null;
		ApiResponseData responseData = new ApiResponseData();
		String msg;
		ResponseUtils.setJsonResponse(resp);

		PrintWriter writer = resp.getWriter();
		// 获取上传的文件列表，Part对象就是Servlet3对文件上传支持中对文件数据的抽象结构
		try {
			parts = req.getParts();
		} catch (Exception e) {
			msg = "======>获取上传文件错误。";
			logger.error(msg);
			e.printStackTrace();
			responseData.setErrorMessage(msg + e.getMessage());
			writer.write(responseData.toString());
			writer.flush();
			return;
		}
		if(parts == null || parts.isEmpty()){
			msg = "上传文件为空！";
			logger.debug(msg);
			responseData.setErrorMessage(msg);
			writer.write(responseData.toString());
			writer.flush();
			return;
		}

		for (Part part : parts) {
			if(part == null){
				continue;
			}
			if (isFileValid(part)){
				try {
					String fileName = part.getSubmittedFileName();
					String ext = FileUtils.getFileSuffix(fileName);
					InputStream is = part.getInputStream();
					//创建全局唯一的文件名
					String newName = IdGen.uuid()+ "." + ext;
					String saveDir = getServletContext().getRealPath("/WEB-INF/"+Global.USERFILES_BASE_URL);
					String urlDir = Global.USERFILES_BASE_URL;
					FileUtils.makeDirectory(saveDir);
					String newFileName = FileUtils.path(saveDir)+newName;
					String newUrlName = FileUtils.path(urlDir) + newName;


					// 将文件保存指硬盘
					part.write(newFileName);
					//将新旧文件对照关系记录下了，传回客户端
					responseData.pushData(fileName,newUrlName);

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

			}
		}
		responseData.setSuccessMessage("");//补充正确返回的头信息

		writer.write(responseData.toString());
		writer.flush();
	}

	private boolean isFileValid(Part file) {
		// 上传的并非文件
		if (file.getContentType() == null) {
			return false;
		}
		// 没有选择任何文件
		else if (Strings.isNullOrEmpty(file.getSubmittedFileName())) {
			return false;
		}
		return true;
	}
}
