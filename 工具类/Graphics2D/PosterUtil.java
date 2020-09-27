package com.jsb.common.util.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author chenhaowen
 * @description 海报生成工具类
 * @date 2020/9/24  下午3:39
 */
public class PosterUtil {


    protected final static Logger logger = LoggerFactory.getLogger(PosterUtil.class);


    /**
     * 设置图片大小
     *
     * @param x
     * @param y
     * @param bfi
     * @return
     */
    public static BufferedImage resizeImage(int x, int y, BufferedImage bfi) {
        BufferedImage bufferedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(bfi.getScaledInstance(x, y, Image.SCALE_SMOOTH), 0, 0, null);
        return bufferedImage;
    }


    /**
     * 图片远程地址获取
     *
     * @param path
     * @return
     */
    public static BufferedImage fromRemoteUrl(String path) throws IOException {
        URL url = new URL(path);
        return ImageIO.read(url);
    }

    /**
     * 从本地路径获取
     *
     * @param path
     * @return
     */
    public static BufferedImage fromLocal(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    /**
     * byte[] -> BufferedImage
     *
     * @param data
     * @return
     */
    public static BufferedImage fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        return ImageIO.read(in);
    }

    /**
     * 增加背景
     * 用于解决二维码不完整的问题
     *
     * @param width
     * @param height
     */
    public static BufferedImage addBackgroundImage(int width, int height, BufferedImage mainImg, int padding) throws Exception {
        // 透明底的图片
        BufferedImage formatAvatarImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = formatAvatarImage.createGraphics();
        // 压缩原图
        BufferedImage image = scaleByPercentage(mainImg, width - padding, height - padding);
        //添加原图
        int x, y;
        if (padding > 0) {
            x = padding / 2;
            y = padding / 2;
        } else {
            x = 0;
            y = 0;
        }
        graphics.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
        graphics.dispose();
        return formatAvatarImage;
    }


    /**
     * 画圆形
     */
    public static BufferedImage setRadius2(BufferedImage srcImage) throws IOException {
        int radius = (srcImage.getWidth() + srcImage.getHeight()) / 2;
        return setRadius(srcImage, radius, 0, 0);
    }


    /**
     * 设置圆角
     *
     * @param srcImage 图片
     * @param radius   圆角
     * @param border   边框
     * @param padding  内边距
     */
    public static BufferedImage setRadius(BufferedImage srcImage, int radius, int border, int padding) throws IOException {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int canvasWidth = width + padding * 2;
        int canvasHeight = height + padding * 2;

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setComposite(AlphaComposite.Src);
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(Color.WHITE);
        gs.fill(new RoundRectangle2D.Float(0, 0, canvasWidth, canvasHeight, radius, radius));
        gs.setComposite(AlphaComposite.SrcAtop);
        gs.drawImage(setClip(srcImage, radius), padding, padding, null);
        if (border != 0) {
            gs.setColor(Color.GRAY);
            gs.setStroke(new BasicStroke(border));
            gs.drawRoundRect(padding, padding, canvasWidth - 2 * padding, canvasHeight - 2 * padding, radius, radius);
        }
        gs.dispose();
        return image;
    }

    /**
     * 修边 为了抗锯齿
     *
     * @param srcImage
     * @param radius
     * @return
     */
    public static BufferedImage setClip(BufferedImage srcImage, int radius) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();

        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }


    /**
     * 缩小Image，此方法返回源图像按给定宽度、高度限制下缩放后的图像
     *
     * @param inputImage
     * @param newWidth   ：压缩后宽度
     * @param newHeight  ：压缩后高度
     * @throws IOException return
     */
    public static BufferedImage scaleByPercentage(BufferedImage inputImage, int newWidth, int newHeight) throws Exception {
        // 获取原始图像透明度类型
        int type = inputImage.getColorModel().getTransparency();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        // 开启抗锯齿
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 使用高质量压缩
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage img = new BufferedImage(newWidth, newHeight, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHints(renderingHints);
        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
        graphics2d.dispose();
        return img;
    }


    /**
     * 获取字符串宽度
     *
     * @param g
     * @param str
     * @return
     */
    private static int getStringLength(Graphics2D g, String str) {
        char[] chars = str.toCharArray();
        int strWidth = g.getFontMetrics().charsWidth(chars, 0, str.length());
        logger.info("length of str : {}", strWidth);
        return strWidth;
    }


    /**
     * 字符行数
     *
     * @param strWidth 字符串总长度
     * @param rowWidth 一行的宽度
     * @return
     */
    private static int getRows(int strWidth, int rowWidth) {
        int rows = 0;
        if (strWidth % rowWidth > 0) {
            rows = strWidth / rowWidth + 1;
        } else {
            rows = strWidth / rowWidth;
        }
        logger.info("width of row : {}", strWidth);
        return rows;
    }


    /**
     * 计算每一行字符的个数
     *
     * @param strNum
     * @param rowWidth
     * @param strWidth
     * @return
     */
    private static int getRowStrNum(int strNum, int rowWidth, int strWidth) {
        if (strWidth < 1) {
            throw new RuntimeException("字符串宽度不能小于1");
        }
        int rowStrNum = (rowWidth * strNum) / strWidth;
        logger.info("char num of one row : {}", rowStrNum);
        return rowStrNum;
    }

    /**
     * 获取字符高度
     *
     * @param g
     * @return
     */
    private static int getStringHeight(Graphics2D g) {
        int height = g.getFontMetrics().getHeight();
        logger.info("height of str : {}", height);
        return height;
    }


    /**
     * 绘制字符串换行
     *
     * @param g
     * @param content  内容
     * @param x        起始位置x
     * @param y        起始位置y
     * @param font
     * @param rowWidth
     * @param lineNum
     */
    public static void drawStringWithFontStyleLineFeed(Graphics2D g, String content, int x, int y, Font font, int rowWidth, int lineNum) {
        //设置字体
        g.setFont(font);
        //获取字符串 字符的总宽度
        int strWidth = getStringLength(g, content);
        logger.info("width of each row {}", rowWidth);
        //获取字符高度
        int strHeight = getStringHeight(g);
        if (strWidth > rowWidth) {
            int rowStrNum = getRowStrNum(content.length(), rowWidth, strWidth);

            int rows = getRows(strWidth, rowWidth);
            //限定行数
            if (rows > lineNum) {
                //结尾使用...替换最后一个字符串
                int maxNum = rowStrNum * lineNum - 1;
                content = content.substring(0, maxNum) + "...";
            }
            rows = lineNum;
            String temp = "";
            for (int i = 0; i < rows; i++) {
                //获取各行的String
                if (i == rows - 1) {
                    //最后一行
                    temp = content.substring(i * rowStrNum);
                } else {
                    temp = content.substring(i * rowStrNum, i * rowStrNum + rowStrNum);
                }
                if (i > 0) {
                    //第一行不需要增加字符高度，以后的每一行在换行的时候都需要增加字符高度
                    y = y + strHeight;
                }
                g.drawString(temp, x, y);
            }
        } else {
            //直接绘制
            g.drawString(content, x, y);
        }

    }

//
//    public static String generatePoster(String backgroundPath, String qrCodePath, String imgUrl, String message, String outPutPath) {
//
//        try {
//            //设置背景图片
//
//            BufferedImage background = resizeImage(750, 1288, fromLocal(backgroundPath));
//            //获取二维码图片
//            BufferedImage qrCode = fromRemoteUrl(qrCodePath);
//            Graphics2D g = background.createGraphics();
//            g.setColor(Color.WHITE);
//            Font font = new Font("PingFangSC-Semibold", Font.BOLD, 44);
//            g.setFont(font);
//            g.setPaint(new Color(7, 7, 7));
//            //计算居中
//            FontMetrics fm = g.getFontMetrics(font);
//            int title1Center = (background.getWidth() - fm.stringWidth(message)) / 2;
//            drawStringWithFontStyleLineFeed(g, message, 68, 340, font, 616, 2);
////            g.drawString(message, title1Center,340);
//            //在背景图片上添加二维码图片
//            BufferedImage convertImage = addBackgroundImage(160, 160, qrCode, 8);
//            qrCode = setRadius2(convertImage);
//            g.drawImage(qrCode, 148 * 2, 482 * 2, qrCode.getWidth(), qrCode.getHeight(), null);
//            //添加缩略图
//            BufferedImage img = resizeImage(616, 424, fromRemoteUrl(imgUrl));
//            img = setRadius(img, 20, 0, 0);
//            g.drawImage(img, 68, 500, img.getWidth(), img.getHeight(), null);
//            g.dispose();
//            ImageIO.write(background, "png", new File(outPutPath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }


//    public static void main(String[] args) {
//        //generatePoster("/home/paat/doc/2.png","https://fileserver.paat.com/eb2/eb23e82b3e07f33e169e13ef7d1a829e.png","https://fileserver.paat.com/606/606c1b184b9d2b13fc851734c1e0b149.png","测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下", "/home/paat/doc/test.png");
//        generatePoster("/Users/chenhaowen/doc/canvas/2.png", "https://fileserver.paat.com/eb2/eb23e82b3e07f33e169e13ef7d1a829e.png", "https://fileserver.paat.com/606/606c1b184b9d2b13fc851734c1e0b149.png", "测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下", "/Users/chenhaowen/doc/canvas/test.png");
////        test1();
//    }
//
//
//    private static void test1() {
//
//        try {
//            //获取二维码图片
//            BufferedImage qrCode = fromRemoteUrl("https://fileserver.paat.com/eb2/eb23e82b3e07f33e169e13ef7d1a829e.png");
//            BufferedImage backgroundImage = addBackgroundImage(160, 160, qrCode, 20);
//            ImageIO.write(backgroundImage, "png", new File("/Users/chenhaowen/doc/canvas/test3.png"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
