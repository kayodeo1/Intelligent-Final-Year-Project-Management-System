package com.kayode.ifypm.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.kayode.ifypm.model.Chapter;
import com.kayode.ifypm.service.ChapterService;

import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/chapter-file")
public class ChapterFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private ChapterService chapterService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long id;
        try { id = Long.parseLong(idStr.trim()); }
        catch (NumberFormatException e) { resp.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }

        Chapter chapter = chapterService.findChapter(id);
        if (chapter == null || chapter.getFilePath() == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path path = Paths.get(chapter.getFilePath());
        if (!Files.exists(path)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on disk");
            return;
        }

        String contentType = detectContentType(chapter.getFileName());
        resp.setContentType(contentType);
        resp.setContentLengthLong(Files.size(path));
        resp.setHeader("Content-Disposition", "inline; filename=\"" + chapter.getFileName() + "\"");
        resp.setHeader("Cache-Control", "private, max-age=300");

        try (OutputStream out = resp.getOutputStream()) {
            Files.copy(path, out);
        }
    }

    private String detectContentType(String fileName) {
        if (fileName == null) return "application/octet-stream";
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf"))  return "application/pdf";
        if (lower.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (lower.endsWith(".doc"))  return "application/msword";
        return "application/octet-stream";
    }
}
