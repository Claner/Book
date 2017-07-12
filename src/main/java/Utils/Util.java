package Utils;

import Entity.Response;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Clanner on 2017/3/23.
 */
public class Util {

    private final String separator = File.separator;

    /**
     * 采用加密算法加密字符串数据
     *
     * @param str       需要加密的数据
     * @param algorithm 采用的加密算法
     * @return 字节数据
     */
    private byte[] EncryptionStrBytes(String str, String algorithm) {
        // 加密之后所得字节数组
        byte[] bytes = null;
        try {
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //添加要进行计算摘要的信息
            md.update(str.getBytes());
            //得到该摘要
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("加密算法: " + algorithm + " 不存在: ");
        }
        return null == bytes ? null : bytes;
    }


    /**
     * 把字节数组转化成字符串返回
     *
     * @param bytes
     * @return
     */
    private String BytesConvertToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String s = Integer.toHexString(0xff & aByte);
            if (s.length() == 1) {
                sb.append("0" + s);
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * 采用加密算法加密字符串数据
     *
     * @param str 需要加密的数据
     * @return 字节数据
     */
    public String EnCode(String str) {
        // 加密之后所得字节数组
        byte[] bytes = EncryptionStrBytes(str, "MD5");
        return BytesConvertToHexString(bytes);
    }

    public boolean saveImage(MultipartFile file, HttpServletRequest request, String path) {
        if (!file.isEmpty()) {
            String[] s = file.getOriginalFilename().split("\\.");
            //文件保存路径
            String filePath = request.getSession().getServletContext().getRealPath(separator + path + separator)
                    + separator + s[0] + ".jpg";
            try {
                File f = new File(filePath);
                f.setWritable(true, false);
                if (!f.isDirectory()) f.mkdirs();
                file.transferTo(f);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean saveAvatar(Integer user_id, MultipartFile file, HttpServletRequest request, String path) {
        if (!file.isEmpty()) {
            String[] s = file.getOriginalFilename().split("\\.");
            //文件保存路径
            String filePath = request.getSession().getServletContext().getRealPath(separator + path + separator)
                    + separator + user_id + separator + s[0] + ".jpg";
            try {
                File f = new File(filePath);
                f.setWritable(true, false);
                if (!f.isDirectory()) f.mkdirs();
                file.transferTo(f);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean saveApk(MultipartFile file, HttpServletRequest request, String path) {
        if (!file.isEmpty()) {
            String[] s = file.getOriginalFilename().split("\\.");
            //文件保存路径
            String filePath = request.getSession().getServletContext().getRealPath(separator + path + separator)
                    + separator + s[0] + ".apk";
            try {
                File f = new File(filePath);
                f.setWritable(true, false);
                if (!f.isDirectory()) f.mkdirs();
                file.transferTo(f);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isFilesEmpty(MultipartFile[] files) {
        if (files.length == 0) return true;
        for (MultipartFile file : files) if (file.isEmpty()) return true;
        return false;
    }

    public boolean isParamsEmpty(String... params) {
        for (String param : params)
            if (param == null || "".equals(param)) return true;
        return false;
    }

    public String timeStamp2String(String timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        timeStamp = timeStamp.substring(0, timeStamp.length() - 3);
        int i = Integer.parseInt(timeStamp);
        String times = sdf.format(new Date(i * 1000L));
        return times;
    }

    public void returnJson(HttpServletResponse httpServletResponse, int code,String message){
        Object jsonObject = JSONObject.toJSON(new Response().notVisit(code,message));
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            out.append(jsonObject.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static Util getInstance() {
        return UtilHolder.instance;
    }

    private static final class UtilHolder {
        private static final Util instance = new Util();
    }
}
