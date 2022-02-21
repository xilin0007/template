package com.fxl.template.user.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fxl.frame.base.BaseController;
import com.fxl.frame.common.ReturnMsg;
import com.fxl.frame.util.GenerateQRCode;
import com.fxl.frame.util.PinyinAPI;
import com.fxl.frame.util.SessionUtils;
import com.fxl.frame.util.constant.Consts;
import com.fxl.frame.util.file.DownloadFile;
import com.fxl.template.user.entity.PinyinChinese;
import com.fxl.template.user.entity.UserInfo;
import com.fxl.template.user.pdf.GenerateCourseListPDF;
import com.fxl.template.user.service.PinyinChineseService;
import com.fxl.template.user.service.UserInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/userPage")
public class UserPageController extends BaseController {
	
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private PinyinChineseService pinyinChineseService;
	
	@RequestMapping("/demo")
	public String demo(HttpServletRequest request) {
		//测试设置session
		HttpSession session = getRequest().getSession();
		session.setAttribute("baseUrl", Consts.BASE_URL);
		//测试获取session
		String baseUrl = SessionUtils.getHttpBaseUrl();
		log.info(baseUrl);
		return "user/demo";
	}
	
	/**
	 * 搜索自动补全
	 * @version 1.0
	 * @createTime 2017-4-1,下午4:43:28
	 * @createAuthor fangxilin
	 * @param nickName
	 * @param size
	 * @return
	 */
	@RequestMapping("/findByNickName")
	@ResponseBody
	public ReturnMsg findByNickName(@RequestParam(value = "nickName") String nickName,
			@RequestParam("size") int size){
		try {
			List<UserInfo> returnList = userInfoService.findByNickName(nickName, size);
			Object data = (returnList != null) ? returnList : new ArrayList<>();
			return new ReturnMsg(ReturnMsg.SUCCESS, "搜索成功", data);
		} catch (Exception e) {
			e.printStackTrace();
			return new ReturnMsg(ReturnMsg.FAIL, "搜索异常", Collections.emptyList());
		}
	}
	
	/**
	 * jsp分页
	 */
	@RequestMapping("/listPageUser")
	public String listPageUser(ModelMap model, Page<?> page, String nickName) {
		try {
			page.setPageSize(10);
			PageInfo<UserInfo> pageInfo = userInfoService.listPageUser(page, nickName);
			model.put("nickName", nickName);
			model.put("pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "user/listPageUser";
	}
	
	/**
	 * 基于layui的分页
	 */
	@RequestMapping("/listPageUserLayui")
	public String listPageUserLayui() {
		return "user/listPageUserLayui";
	}
	
	/**
	 * 基于mui的H5分页
	 */
	@RequestMapping("/pullrefreshSub")
	public String pullrefreshMain() {
		return "user/pullrefreshSub";
	}
	
	/**
	 * 分页ajax接口
	 */
	@RequestMapping("/pageUserAjax")
	@ResponseBody
	public ReturnMsg pageUserAjax(@RequestParam(required = false) String query,
			@RequestParam int pageNum,
			@RequestParam int pageSize) {
		try {
			query = (StringUtils.isNotEmpty(query)) ? query : null;
			//最好传基本参数类型来代替Page参数
			Page<UserInfo> page = new Page<UserInfo>();
			page.setPageNum(pageNum);
			page.setPageSize(pageSize);
			PageInfo<UserInfo> pageInfo = userInfoService.listPageUser(page, query);
			return new ReturnMsg(ReturnMsg.SUCCESS, "搜索成功", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return new ReturnMsg(ReturnMsg.FAIL, "搜索异常", new ArrayList<>());
		}
	}
	
	/**
	 * 分页ajax接口-只返回列表
	 */
	@RequestMapping("/pageUserAjaxList")
	@ResponseBody
	public ReturnMsg pageUserAjaxList(@RequestParam(required = false) String query,
			@RequestParam int pageNum,
			@RequestParam int pageSize) {
		try {
			query = (StringUtils.isNotEmpty(query)) ? query : null;
			List<UserInfo> data = userInfoService.listPageUser(pageNum, pageSize, query);
			return new ReturnMsg(ReturnMsg.SUCCESS, "搜索成功", data);
		} catch (Exception e) {
			e.printStackTrace();
			return new ReturnMsg(ReturnMsg.FAIL, "搜索异常", new ArrayList<>());
		}
	}
	
	
	/**
	 * 测试传List参数
	 * @createTime 2017-4-28,下午4:35:03
	 * @createAuthor fangxilin
	 * @param idList
	 * @return
	 */
	@RequestMapping("/testIdList")
	@ResponseBody
	public ReturnMsg testIdList(@RequestParam(value = "idList[]") List<Integer> idList){
		try {
			log.info(idList.toString());
			return new ReturnMsg(ReturnMsg.SUCCESS, "搜索成功");
		} catch (Exception e) {
			return new ReturnMsg(ReturnMsg.FAIL, "搜索异常", new ArrayList<>());
		}
	}
	
	/**
	 * 下载网络文件
	 * @createTime 2017-5-23,上午10:20:40
	 * @createAuthor fangxilin
	 * @param response
	 * @param urlString
	 */
	@RequestMapping("/downloadNet")
	public void downloadNet(HttpServletResponse response, 
			@RequestParam(value = "urlString") String urlString) {  
        // 下载网络文件  
		String fileName = "二维码.jpg";
		//fileName = new String(fileName.getBytes(),"iso-8859-1");
        String filePath = DownloadFile.download(urlString, fileName);
        OutputStream outStream = null;
        InputStream inStream = null;
        try {
	    	inStream = new BufferedInputStream(new FileInputStream(filePath));
		    byte[] buffer = new byte[inStream.available()];
		    inStream.read(buffer);
		    response.reset();
		    //Content-Disposition激活文件下载对话框, 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
			//response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1"));
		    response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
		    outStream = new BufferedOutputStream(response.getOutputStream());
		    //application/octet-stream二进制流，不知道下载文件类型时设置
		    response.setContentType("application/octet-stream");
		    outStream.write(buffer);// 输出文件
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inStream, outStream);
			 /** 最后将临时文件删除 **/
        	File file = new File(filePath);
        	FileUtils.deleteQuietly(file);
		}
    }
	
	/**
	 * 生成二维码
	 * @createTime 2017-5-23,上午10:47:38
	 * @createAuthor fangxilin
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/generateQRCode")
	public void generateQRCode(HttpServletResponse response) throws Exception {
		//Pragma，Cache-Control no-cache设置页面缓存 ，每次访问时，都不去访问缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        //缓存过去时间
        response.setDateHeader("Expires", 0);
        //设置输出的内容的类型为JPEG图像  
        response.setContentType("image/png");
        
        /*String filePath = GenerateQRCode.generate("https://www.baidu.com");
        File file = new File(filePath);
        BufferedImage bufferedImage = ImageIO.read(file);
        //写给浏览器  
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
        //最后删除临时文件
        file.delete();*/
        
        GenerateQRCode.generate("https://www.baidu.com", response.getOutputStream());
	}
	
	/**
	 * 下载二维码
	 * @createTime 2017-5-23,下午12:02:07
	 * @createAuthor fangxilin
	 * @param response
	 * @param hospitalId
	 * @throws Exception 
	 */
	@RequestMapping("/downloadQRCode")
	public void downloadQRCode(HttpServletResponse response) throws Exception {
		String fileName = "二维码.png";
		String content = "https://www.baidu.com";
        OutputStream outStream = null;
        try {
        	//response.reset();
        	/**
        	 * Content-Disposition：把请求所得的内容存为一个文件的时候提供一个默认的文件名
        	 * attachment为以附件方式下载 
        	 * 去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
        	 */
		    //response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso-8859-1"));
			response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
		    /**
		     * application/octet-stream：二进制流数据（如常见的文件下载）
		     */
		    response.setContentType("application/octet-stream");
		    outStream = new BufferedOutputStream(response.getOutputStream());
		    GenerateQRCode.generate(content, outStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}
	
	/**
	 * 拼音模糊查询
	 * @createTime 2017-7-3,下午2:07:40
	 * @createAuthor fangxilin
	 * @param query
	 * @param size
	 * @return
	 */
	@RequestMapping("/searchByPinyin")
	@ResponseBody
	public ReturnMsg searchByPinyin(@RequestParam String query, 
			@RequestParam int size) {
		try {
			query = PinyinAPI.split(query);
			List<PinyinChinese> data = pinyinChineseService.listByPinyin(query, size);
			return new ReturnMsg(ReturnMsg.SUCCESS, "拼音模糊查询成功", data);
		} catch (Exception e) {
			log.error("拼音模糊查询异常");
			e.printStackTrace();
			return new ReturnMsg(ReturnMsg.FAIL, "拼音模糊查询异常", new ArrayList<>());
		}
	}
	
	/**
	 * @Description 导出pdf
	 * @createTime 2017年11月28日,下午3:11:05
	 * @createAuthor fangxilin
	 * @param request
	 * @param response
	 * @param tabName
	 * @throws Exception 
	 */
	@RequestMapping(value = "/downPDF")
	public void downPDF(HttpServletRequest request, HttpServletResponse response, String tabName) throws Exception {
		String filePath = GenerateCourseListPDF.generatePDF(tabName);
		if (filePath != null && filePath.length() != 0) {
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + new String(tabName.getBytes("gb2312"), "iso-8859-1") + ".pdf");
			ServletOutputStream out = null;
			InputStream inputStream = null;
			try {
				File file = new File(filePath);
				inputStream = new FileInputStream(file);
				out = response.getOutputStream();
				int byteread = 0;
				byte[] buffer = new byte[1024];
				while ((byteread = inputStream.read(buffer)) != -1) {
					out.write(buffer, 0, byteread);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(inputStream, out);
				/** 最后将临时文件删除 **/
				File file = new File(filePath);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
}
