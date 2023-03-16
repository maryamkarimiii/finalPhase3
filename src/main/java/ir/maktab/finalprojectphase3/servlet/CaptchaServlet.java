package ir.maktab.finalprojectphase3.servlet;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.io.IOException;

@WebServlet("/captcha-servlet")
public class CaptchaServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // create captcha image
        Captcha captcha = new Captcha.Builder(200, 50)
                .addText(new DefaultTextProducer(), new DefaultWordRenderer())
                .addBackground(new GradiatedBackgroundProducer())
                .build();

        // store captcha text in session
        request.getSession().setAttribute("captcha", captcha.getAnswer());

        // write image to response output stream
        response.setContentType("image/png");
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(captcha.getImage(), "png", outputStream);
        outputStream.close();
    }
}
