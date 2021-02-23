package com.qf.learn.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/API/func")
public class RestFunc {

    @Value("${vrv.root.path}")
    private String rootpath;

    @RequestMapping(value = "**")
    public String api() {
        return "NO DATA";
    }

    @RequestMapping(value = "/upload")
    public String update(HttpServletRequest request, @RequestParam MultipartFile file) {
        String fileName = file.getOriginalFilename();
        java.io.File dest = new java.io.File(rootpath + fileName);
        java.io.File pfile = new java.io.File(rootpath);
        if (!pfile.exists()) {
            pfile.mkdirs();
        }
        try {
            file.transferTo(dest);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping("/down/{filename:.+}")
    @ResponseBody
    public void down(@PathVariable String filename, HttpServletResponse response) {
        java.io.File dest = new java.io.File(rootpath + filename);
        try {
            ServletOutputStream out;
            FileInputStream fis = new FileInputStream(dest);
            try {
                out = response.getOutputStream();

                response.setContentType("application/octet-stream");
                response.setContentLength((int) dest.length());
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));

                int bytesRead = 0;
                byte[] buffer = new byte[819200];
                while ((bytesRead = fis.read(buffer, 0, 819200)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}