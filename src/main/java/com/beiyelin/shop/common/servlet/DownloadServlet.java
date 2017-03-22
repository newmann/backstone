package com.beiyelin.shop.common.servlet;

import com.beiyelin.shop.common.bean.ApiResponseData;
import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.config.ResultCode;
import com.beiyelin.shop.common.utils.FileUtils;
import com.beiyelin.shop.common.utils.ResponseUtils;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 文件下載
 * @author Newmann

 */
@WebServlet("/download/*")
public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void fileOutputStream(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException,Exception {
		String fileName=null;
		String fileDir = null;
		String requestUri = req.getRequestURI();
		int lastIndex = requestUri.lastIndexOf("/");
		if (lastIndex > 0) {
			fileName = requestUri.substring(lastIndex + 1);
			fileDir = requestUri.substring("/download/".length()-1,lastIndex);
		}
		if (fileName == null || fileName.trim().length() == 0) {
//			resp.reset();
//			resp.setContentType("text/plain;charset=utf-8");
//			PrintWriter writer = resp.getWriter();
//			writer.write("error:can't get the file name! 不能获取文件名");
//			writer.flush();
			SendErrorResponse("error:can't get the file name! 不能获取文件名",resp);
			return;
		}
		String fileRealDir = getServletContext().getRealPath(FileUtils.path("/WEB-INF/userfiles/" + fileDir));
		String pathName = FileUtils.path(fileRealDir)+ fileName;
		File file = new File(pathName);
		if (!file.exists()) {
//			resp.reset();
//			resp.setContentType("text/plain;charset=utf-8");
//			PrintWriter writer = resp.getWriter();
//			writer.write("error: file not exist! 文件不存在");
//			writer.flush();
			SendErrorResponse("error: file not exist! 文件不存在",resp);
			return;
		}

		long length = file.length();
		long start = 0;
		resp.reset();
		resp.setHeader("Accept-Ranges", "byte");
		//断点续传的信息就存储在这个Header属性里面： range:bytes=3-100;200 （从3开始，读取长度为100，总长度为200）
		String range = req.getHeader("Range");
		if (range != null) {
			//SC_PARTIAL_CONTENT 206 表示服务器已经成功处理了部分 GET 请求。类似于 FlashGet 或者迅雷这类的 HTTP下载工具都是使用此类响应实现断点续传或者将一个大文档分解为多个下载段同时下载。
			resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			range = range.substring("bytes=".length());
			String[] rangeInfo = range.split("-");
			start = new Long(rangeInfo[0]);
			if (start > file.length()) {
				resp.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return;
			}
			if (rangeInfo.length > 1) {
				length = Long.parseLong(rangeInfo[1]) - start + 1;
			} else {
				length = length - start;
			}
			if (length + start > file.length()) {
				length = file.length() - start;
			}
		}


		MagicMatch match = Magic.getMagicMatch(file,true,false);
		resp.setHeader("Content-Type", match.getMimeType());
		resp.setHeader("Content-Length", new Long(length).toString());
		if (range != null) {
			resp.setHeader("Content-Range", "bytes " + new Long(start).toString() + "-" + new Long(start + length - 1).toString() + "/" + new Long(file.length()).toString());
		}
		resp.setContentType(match.getMimeType());
//		resp.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes(), "utf-8"));
		resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		long k = 0;
		int ibuffer = 65536;
		byte[] bytes = new byte[ibuffer];
		FileInputStream fileinputstream = new FileInputStream(file);
		try {
			if (start != 0) {
				fileinputstream.skip(start);
			}
			OutputStream os = resp.getOutputStream();
			while (k < length) {
				int j = fileinputstream.read(bytes, 0, (int) (length - k < ibuffer ? length - k : ibuffer));
				if (j < 1) {
					break;
				}
				os.write(bytes, 0, j);
				k += j;
			}
			os.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fileinputstream.close();
		}
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			fileOutputStream(req, resp);
		}catch (Exception ex) {
			SendErrorResponse(ex.getMessage(),resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			fileOutputStream(req, resp);
		}catch (Exception ex){
			SendErrorResponse(ex.getMessage(),resp);
		}
	}

	private void SendErrorResponse(String msg, HttpServletResponse resp) throws IOException{
		ResponseUtils.setHtmlResponse(resp);
		PrintWriter writer = resp.getWriter();
		writer.write(msg);
		writer.flush();
	}
}
