package com.metrobuzz.dependencies.wrappers;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrobuzz.dependencies.utilities.Response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.PrintWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream byteArrayOutputStream;
    private PrintWriter writer;
    private ServletOutputStream servletOutputStream;
    private HttpServletResponse httpServletResponse;
    private boolean writerUsed;
    private boolean outputStreamUsed;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.httpServletResponse = response;
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    public void serveContent() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        if (isTextResponse() || isApplicationJson()) {
            PrintWriter writer = this.httpServletResponse.getWriter();

            String str = new String(byteArray, StandardCharsets.UTF_8);

            Object objectMapped = objectMapper.readValue(str, Object.class);

            Response<Object> customResponse = new Response<>("Success",
                    httpServletResponse.getStatus(), objectMapped);

            String serializedResponse = objectMapper.writeValueAsString(customResponse);

            writer.write(serializedResponse);
            writer.close();
        } else {
            ServletOutputStream finalOutputStream = this.httpServletResponse.getOutputStream();
            finalOutputStream.write(byteArray);
            finalOutputStream.flush();
        }
    }

    public boolean isApplicationJson() {
        String contentType = getContentType();
        return contentType != null && contentType.equals("application/json");
    }

    public boolean isTextResponse() {
        String contentType = getContentType();
        return contentType != null && contentType.startsWith("text/");
    }

    @Override
    public String getContentType() {
        return httpServletResponse.getContentType();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        System.out.println("Called");

        if (this.writer == null) {
            this.writer = new PrintWriter(byteArrayOutputStream, true);
        }

        return this.writer;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        System.out.println("Hello");

        if (this.servletOutputStream == null) {
            this.servletOutputStream = new ServletOutputStream() {
                public void write(int value) throws IOException {
                    byteArrayOutputStream.write(value);
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                }
            };
        }

        return this.servletOutputStream;
    }

}