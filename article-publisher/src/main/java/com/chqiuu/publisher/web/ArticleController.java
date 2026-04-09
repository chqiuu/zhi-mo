package com.chqiuu.publisher.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Controller
public class ArticleController {

    private static final String ARTICLE_DIR = "/Volumes/OtherData/Docker/nginx/html/article-data";

    /**
     * 文章列表页
     */
    @GetMapping("/article/")
    public String articleList(Model model) {
        List<Map<String, String>> articles = new ArrayList<>();
        File dir = new File(ARTICLE_DIR);
        
        if (dir.exists() && dir.isDirectory()) {
            File[] subdirs = dir.listFiles(File::isDirectory);
            if (subdirs != null) {
                Arrays.sort(subdirs, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                for (File subdir : subdirs) {
                    if (subdir.getName().equals("index.html")) continue;
                    
                    File articleFile = new File(subdir, "index.html");
                    if (articleFile.exists()) {
                        Map<String, String> article = new HashMap<>();
                        article.put("id", subdir.getName());
                        article.put("path", "/article-data/" + subdir.getName() + "/");
                        article.put("editPath", "/article/edit/" + subdir.getName());
                        
                        // 尝试从HTML中提取标题
                        String title = extractTitle(articleFile);
                        article.put("title", title);
                        
                        articles.add(article);
                    }
                }
            }
        }
        
        model.addAttribute("articles", articles);
        return "article-list";
    }

    /**
     * 编辑文章页
     */
    @GetMapping("/article/edit/{id}")
    public String editArticle(@PathVariable String id, Model model) {
        File articleDir = new File(ARTICLE_DIR + "/" + id);
        File articleFile = new File(articleDir, "index.html");
        
        if (!articleFile.exists()) {
            return "redirect:/article/";
        }
        
        try {
            String content = new String(Files.readAllBytes(articleFile.toPath()), StandardCharsets.UTF_8);
            
            // 提取标题
            String title = extractTitle(articleFile);
            
            // 提取正文内容（去掉HTML标签，只保留body内的文本）
            String bodyContent = extractBodyContent(content);
            
            model.addAttribute("id", id);
            model.addAttribute("title", title);
            model.addAttribute("content", bodyContent);
            model.addAttribute("originalContent", content);
            
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/article/";
        }
        
        return "article-edit";
    }

    /**
     * 保存文章
     */
    @PostMapping("/article/save/{id}")
    @ResponseBody
    public Map<String, Object> saveArticle(
            @PathVariable String id,
            @RequestParam("title") String title,
            @RequestParam("content") String content) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            File articleDir = new File(ARTICLE_DIR + "/" + id);
            File articleFile = new File(articleDir, "index.html");
            
            if (!articleFile.exists()) {
                result.put("success", false);
                result.put("message", "文章不存在");
                return result;
            }
            
            // 读取原文件，替换标题和body内容
            String originalContent = new String(Files.readAllBytes(articleFile.toPath()), StandardCharsets.UTF_8);
            String newContent = originalContent;
            
            // 替换标题
            newContent = newContent.replaceAll(
                "<title>[^<]*</title>",
                "<title>" + escapeHtml(title) + "</title>"
            );
            newContent = newContent.replaceAll(
                "<h1[^>]*>[^<]*</h1>",
                "<h1>" + escapeHtml(title) + "</h1>"
            );
            
            // 替换body内容
            newContent = newContent.replaceAll(
                "(<body[^>]*>)(.*?)(</body>)",
                "$1" + System.lineSeparator() + content + System.lineSeparator() + "$3"
            );
            
            // 写回文件
            Files.write(articleFile.toPath(), newContent.getBytes(StandardCharsets.UTF_8));
            
            result.put("success", true);
            result.put("message", "保存成功");
            
        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "保存失败: " + e.getMessage());
        }
        
        return result;
    }

    private String extractTitle(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("<title>([^<]*)</title>");
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "无标题";
    }

    private String extractBodyContent(String html) {
        // 简单提取body内容
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(
            "<body[^>]*>(.*?)</body>",
            java.util.regex.Pattern.DOTALL
        );
        java.util.regex.Matcher m = p.matcher(html);
        if (m.find()) {
            return m.group(1).trim();
        }
        return "";
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
