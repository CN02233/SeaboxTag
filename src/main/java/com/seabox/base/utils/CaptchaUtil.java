/**
 * 
 */
package com.seabox.base.utils;

import java.awt.Color;
import java.awt.Font;

import com.seabox.tagsys.sys.Constants;
import org.springframework.context.annotation.Bean;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

/**
 * Description 验证码生成工具类
 * @author shibh
 * @create date 2015年12月22日
 * @version 0.0.1
 */

public class CaptchaUtil {
	/**
	 * 定义验证码图文服务对象
	 * 
	 * @return 返回验证码图文服务对象
	 */
	@Bean
	public static ImageCaptchaService captchaService() {
		CaptchaEngine captchaEngine = new GenericCaptchaEngine(new CaptchaFactory[] {captchaFactory()});
		
		return new GenericManageableCaptchaService(captchaEngine, 300, 20000, 20000);
	}
	
	/**
	 * 定义验证码图文工厂对象
	 * 
	 * @return 返回验证码图文工厂对象
	 */
	private static CaptchaFactory captchaFactory() {
		return new GimpyFactory(wordGenerator(), wordToImage());
	}
	
	/**
	 * 定义字符生成器对象
	 * 
	 * @return 返回字符生成器对象
	 */
	private static WordGenerator wordGenerator() {
		return new RandomWordGenerator(Constants.RANDOM_WORD_GENERATOR_ACCEPTED_CHARS);
	}
	
	/**
	 * 定义将字符输出成图片对象
	 * @return 返回字符输出成图片对象
	 */
	private static WordToImage wordToImage() {
		
		// 定义字体生成器对象
		Font[] fonts = new Font[] {  
        	new Font("Arial", 0, 20),  
            new Font("Tahoma", 0, 20),  
            new Font("Verdana", 0, 20),
            new Font("ITALIC", 0, 20),
     	};  
	       
		FontGenerator fontGenerator = new RandomFontGenerator(16, 16, fonts);
		RandomRangeColorGenerator colorRandom = new RandomRangeColorGenerator(new int[]{165,165},new int[]{206,206},new int[]{222,222},new int[]{0,0});
		// 定义背景生成器对象
		FunkyBackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(80, 30,colorRandom);
		
		// 定义字符转换对象
		TextPaster textPaster = new RandomTextPaster(4, 4, Color.WHITE);
		return new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
	}

}
