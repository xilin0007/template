package com.fxl.frame.util.verify;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author rent
 * @date 2015-08-13
 * @desc 正则验证工具类
 */
public class RegexUtils {
	
	public static String MOBILE_FORMAT = null;
	/**
     * 验证Email
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkEmail(String email) {
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"; 
        return Pattern.matches(regex, email); 
    }
    
    /**
     * 验证身份证号码
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */ 
	public static boolean checkIdCard(String idCard) {
		String regex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
		return Pattern.matches(regex,idCard);
    }
	
	/**
	 * 判断密码是否合法
	 * @param password
	 * @return
	 */
	public static boolean isPasswordValid(String password){
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{6,16}$");
    	Matcher matcher = pattern.matcher(password);
    	if(matcher.matches()) {
    		return true;
    	}
    	return false;
	}
	
    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
	public static boolean checkMobile(String mobile) {
		String regex = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";
        return Pattern.matches(regex,mobile);
    }
	
	/** 根据正则规则校验手机号是否符合  **/
	public static boolean checkMobile(String mobile,String regex) {
        return Pattern.matches(regex,mobile);
    }

	/**
     * 验证固定电话号码
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *  数字之后是空格分隔的国家（地区）代码。</p>
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkPhone(String phone) { 
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$"; 
        return Pattern.matches(regex, phone); 
    } 
     
    /**
     * 验证整数（正整数和负整数）
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkDigit(String digit) { 
        String regex = "\\-?[1-9]\\d+"; 
        return Pattern.matches(regex,digit); 
    } 
     
    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkDecimals(String decimals) { 
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?"; 
        return Pattern.matches(regex,decimals); 
    }  
     
    /**
     * 验证空白字符
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkBlankSpace(String blankSpace) { 
        String regex = "\\s+"; 
        return Pattern.matches(regex,blankSpace); 
    } 
     
    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkChinese(String chinese) { 
        String regex = "^[\u4E00-\u9FA5]+$"; 
        return Pattern.matches(regex,chinese); 
    } 
     
    /**
     * 验证日期（年月日）
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkBirthday(String birthday) { 
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}"; 
        return Pattern.matches(regex,birthday); 
    } 
     
    /**
     * 验证URL地址
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkURL(String url) { 
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"; 
        return Pattern.matches(regex, url); 
    } 
     
    /**
     * 匹配中国邮政编码
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkPostcode(String postcode) { 
        String regex = "[1-9]\\d{5}"; 
        return Pattern.matches(regex, postcode); 
    } 
     
    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkIpAddress(String ipAddress) { 
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"; 
        return Pattern.matches(regex, ipAddress); 
    }
    
    /**
     * 判断用户真实姓名，必须全部为中文且长度为2-6位
     * @param userName 用户真实姓名
     * @return true | false
     */
    public static boolean checkUserRealName(String userName){
    	String regex = "^[A-Z\\u4e00-\\u9fa5]{2,6}$";
    	return Pattern.matches(regex, userName);
    }
    
    /**
     * 验证图片格式，只能为png | jpg
     * @param fileName 文件名
     * @return
     */
    public static boolean checkImageFile(String fileName){
    	String regex = "^(/{0,1}\\w){1,}\\.(png|jpg)$|^\\w{1,}\\.(png|jpg)$";
    	return Pattern.matches(regex, fileName);
    }
    
    /**
     * 自定义正则校验
     * @param regex 正则表达式
     * @param value 待验证值
     * @return true | false
     */
    public static boolean customCheck(String regex, String value){
    	return Pattern.matches(regex, value);
    }
    
    /**
     * 校验是否正整数
     * @param value 待验证值
     */
    public static boolean isIntValue(String value){
    	try{
			Integer.parseInt(value);
		}catch(Exception e){
			return false;
		}
		return true;
    }
    /**
     * 校验是否byte
     * @param value 待验证值
     */
	public static boolean isByteValue(String value) {
		try{
			Byte.parseByte(value);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * 检验字符串可以用几个中文，最多几个字符
	 * ex：最多不超过5个汉字（10个字符）
	 * @param str			字符串
	 * @param cnNum 		中文数
	 * @param characters	字符数
	 * @return
	 */
	public static boolean checkCharacters(String str, int maxCnNum, int characters) {
		// 判断是否为空
		if (StringUtils.isEmpty(str)) {
			return false;			
		}
		// 判断是否超过指定字符数
		if (str.length() > characters) {
			return false;
		}		
		int cnNum = 0;
    	char c[] = str.toCharArray();
    	for (int i=0; i<str.length(); i++) {
    		if (checkChinese(String.valueOf(c[i]))) {
    			cnNum++;
    		}    		
    	}
    	if (cnNum > maxCnNum) {
    		return false;    		
    	}
    	return true;
	}

	/**
	 * 获取URL中的参数名和参数值的Map集合
	 * @param url
	 * @return
	 */
	private Map<String, String> getUrlPramNameAndValue(String url) {
		String regEx = "(\\?|&+)(.+?)=([^&]*)";//匹配参数名和参数值的正则表达式
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(url);
		// LinkedHashMap是有序的Map集合，遍历时会按照加入的顺序遍历输出
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		while (m.find()) {
			String paramName = m.group(2);//获取参数名
			String paramVal = m.group(3);//获取参数值
			paramMap.put(paramName, paramVal);
		}
		return paramMap;
	}

	public static void main(String[] args) {
    	
//    	System.out.println(checkEmail("zierrat@balleisen.email"));
		String mails="michellehulshof@hotmail.com,ceciliehansen2607@live.dk,thathi.cardoso81@gmail.com,Lauren-henley@live.co.uk,noeliaceroni@gmail.com,Natalia.sbrissa@gmail.com,becker_tino@yahoo.de,samparsons3@hotmail.com,tifferalc@yahoo.com,gabi_vazata@hotmail.com,tiaraeaton1994@yahoo.com,joycecsales@hotmail.com,charlieannbrown@hotmail.com,bicostap@hotmail.com,lilietnico@outlook.fr,borthwickterri@yahoo.co.uk,natalie.jaeger@hotmail.de,denizaduarte@hotmail.com,Claire.kirkbride@hotmail.com,lilakrem@hotmail.com,ev@bundunet.com,ca_ferreira20@hotmail.com,ophelie412@hotmail.com,stephanie.vetters@gmx.net,jadedownes67@googlemail.com,mollycarreon@gmail.com,t.dessault@gmail.com,chantellegono@hotmail.com,eva.fialova20@email.cz,brunayvis@yahoo.com.br,alexander.tolk@hotmail.de,xleannemorganx@hotmail.co.uk,leish233@hotmail.com,abbeylink556@yahoo.co.uk,sarahsnowdon19@hotmail.com,elodie.court93@gmail.com,bocioagalarisa@yahoo.com,Wendy@Jumper-Medical.com,nadia_paulas@hotmail.com,j.klein@gmx.de,sarahdsuter@gmail.com,a.tolk@hotmail.de,sheila.sidro@hotmail.com,hannah-mariemabe@hotmail.co.uk,kara.esma@live.de,marte.oskarsen@hotmail.com,nfrueh@gmx.at,natypereiralima@gmail.com,sarah.shrimpton@hotmail.co.uk,bcso.bianca@gmail.com,maleon.mickael@hotmail.fr,silviapagnoni@yahoo.it,nadisch21@gmx.de,nrsohn@gmail.com,k.heroutova@seznam.cz,emrah26@arcor.de,dchart14@hotmail.co.uk,vomfell91@gmail.com,ch_az_666@live.co.uk,isa.vio@web.de,elviraamata@gmail.com,c_weidauer@gmx.de,pmpompermayer@gmail.com,matjaz.bolta@gmail.com,tmynssen@yahoo.com.br,kerryhorsfall@hotmail.com,coshea48@hotmail.co.uk,julianafzeni@gmail.com,angela_66_2000@hotmail.com,olga_lhz@mail.ru,sandrameliss@gmx.de,zoe4k@hotmail.com,anahpaz@gmail.com,sandrinee921@gmail.com,nikki.crystal11@gmail.com,priscilla.thais@hotmail.com,anavbueno@hotmail.com,sheleen.92@live.co.uk,rhyslwhyte@gmail.com,kerry.klw@hotmail.com,dero.fr@hotmail.com,shari1989@hotmail.com,lgarnham@live.ca,anahpaz,brookyfly@yahoo.com,lydiaberry95@gmail.com,luvinlife1988@hotmail.co.uk,zz_vicky@hotmail.com,John.r.greenhalgh@gmail.com,ksyusha_06.01@mail.ru,jazz2nik@yahoo.com,melliebutler@gmail.com,melaniejb83@gmail.com,nika.spegu@gmail.com,lauramonaca@gmail.com,kahy92@hotmail.com,kimberleychisnall@live.co.uk,sass.willis@yahoo.com,pgzz_07@hotmail.com,rachela.targioni@gmail.com,paradibarbee@citromail.hu,manuevin@wanadoo.fr,an.caelen@hotmail.com,fengyihan0607@gmail.com,thayanarj@terra.com.br,dwm_1985@hotmail.co.uk,annci_93@hotmail.com,shireen.williams@hotmail.co.uk,katiemoult@hotmail.co.uk,amarillys.jhennie@gmail.com,nicoleloriread@outlook.com,yvettewilbers@hotmail.com,sandyzzz@163.com,stephaniedenise91@Gmail.com,shannamonceaux72@yahoo.com,karenmccomb@hotmail.co.uk,friuseif@hotmail.de,samanthahowd@gmail.com,markusanzengruber88@gmail.com,solveigkristine@outlook.com,jonathandupont996@hotmail.com,arne44@hotmail.de,shazzerjay@gmail.com,stoutm07@gmail.com,heather.havermans@yahoo.co.uk,salsadicta@aol.com,sannew46@hotmail.com,alvizo624@gmail.com,janajos28@gmail.com,lucyannslend@hotmail.co.uk,praluciane@gmail.com,cheekyfunkymonkey37@yahoo.com,mail.an.nessa@web.de,Cristina1221m@yahoo.com,janja.likar83@gmail.com,olganv86@list.ru,sokkiewokkie@hotmail.com,mj21robertson@aol.de,alvizo624@gmail.com,Chloeanddoug@gmail.com,am.koehler@yahoo.de,sararoggensinger@hotmail.com,kimberley.connor@hotmail.com,taralarkins@y7mail.com,clemy.gio@gmail.com,alvizo624@gmail.com,boelenkelly@hotmail.com,ksmith251183@gmail.com,leidybv@hotmail.com,alvizo624@gmail.com,alvizo624@gmail.com,haru.fael@hotmail.com,saba_hussain06@yahoo.com,kenixy@gmail.com,Janine.gavaghan@hotmail.co.uk,mara_no1@hotmail.co.uk,gran6914@hotmail.com,susi.15@hotmail.de,sgingw_1909@hotmail.com,vick.o2sul@hotmail.com,silvia.ilpirata@alice.it,nano.angel.fatboy.plus1@gmail.com,theresa.hennings@gmail.com,buttonkimberly@yahoo.com,Dalila.srodrigues@gmail.com,simespin@hotmail.com,studgie87@gmail.com,adohert4@myune.edu.au,bigmikesag@gmail.com,janicaojala@hotmail.com,rebeccamcrobbie@outlook.com,annekeravenhorst@hotmail.com,Kirsten.Wiederhold@web.de,xxdanielleloganxx@hotmail.com,chromcakova.livia@gmail.com,mariana.paivaa@gmail.com,steelergemz1@hotmail.co.uk,nicola.Rumbold90@Gmail.com,elenagian@hotmail.it,godellt@mail.nmc.edu,rox.conde@hotmail.com,jeannine298@googlemail.com,bellota.bellota@gmail.com,acissej.mausi@web.de,fabian.castrogiovanni@gmail.com,Hoberg-toni@gmx.de,Tahlia.liddelow@gmail.com,marian_22_arq@hotmail.com,Natashafolkes93@yahoo.co.uk,yummmy_mummy_09@live.co.uk,royterje84@gmail.com,kerrilian@hotmail.com,lauracalistri@googlemail.com,rachaelfullard@hotmail.co.uk,mireillebosma@gmail.com,agata.romanin@libero.it,marston1995@icloud.com,ofmtpod@yahoo.com,dionnec259@hotmail.com,ana.sifrer@gmail.com,angelamarieanaya@gmail.com,patricioamaral@gmail.com,monicapires0305@gmail.com,misleneviegas@yahoo.com.br,moni.davidova@gmail.com,chouchou1111@seznam.cz,agatanot94@gmail.cm,fundevil2006@googlemail.com,nadinehoefler190588@gmail.com,sarah_bennett1@live.com,madi.de-schulte@web.de,lisapearce88@hotnail.co.uk,liz0uill3@hotmail.com,di.massimomarco83@gmail.com,linorkv@gmail.com,laal267@gmail.com,rover19923@gmail.com,kiss_patricia@hormail.com,jeslora@hotmail.com,jojo_murphy11@hotmail.com,bella.ilaria@hotmail.it,larebeojitos@gmail.com,lisacordy88@hotmail.com,cristinaborlotti@gmail.com,ryanwall1988@live.co.uk,manzanita.m28@gmail.com,fayeee_x@live.co.uk,bazza.dearstore@hotmail.com,criscrespo@icloud.com,lisapearce88@hotmail.co.uk,jadegregory@rocketmail.com,sian.riley1986@googlemail.com,chelseasanders34@gmail.com,sinuprince@gmail.com,angel_amie@msn.com,chewie1966@tpg.com.au,chane.vandermerwe23@gmail.com,woollard34@hotmail.co.uk,elaineday214@hotmail.com,vinniedixon2011@hotmail.co.uk,418422@gmail.com,anjola89@gmail.com,niamhandchris@hotmail.co.uk,fransesa@outlook.com,hskinner752@gmail.com,schmidtanita85@gmail.com,chantal.w.93@hotmail.com,sahiti.vigan@gmail.com,xxjade-jxx@hotmail.co.uk,dragana.gagi.17@gmail.com,krustengurt-2014@yahoo.com,marusia.chaika@ya.ru,Myrondrummond@gmail.com,fambuehler@bluewin.ch,jenniecackett@yahoo.co.uk,walmsley289@gmail.com,paili_xlxs@hotmail.com,daniela.trombetti84@gmail.com,edipo64@hotmail.com,pink33@centrum.sk,no-ni66@hotmail.com,helenblinkhorn@Gmail.com,emailgarybowes@yahoo.co.uk,Katharina.saesayellow@gmx.de,licazeus@hotmail.com,aniinhavidaal@icloud.com,majkagtx@gmail.com,saralou1984@hotmail.com,n_Warmbold@web.de,349275085@qq.com,418422@gmail.com,anjola89@gmail.com,niamhandchris@hotmail.co.uk,fransesa@outlook.com,hskinner752@gmail.com,schmidtanita85@gmail.com,chantal.w.93@hotmail.com,sahiti.vigan@gmail.com,xxjade-jxx@hotmail.co.uk,dragana.gagi.17@gmail.com,krustengurt-2014@yahoo.com,marusia.chaika@ya.ru,Myrondrummond@gmail.com,fambuehler@bluewin.ch,jenniecackett@yahoo.co.uk,walmsley289@gmail.com,paili_xlxs@hotmail.com,daniela.trombetti84@gmail.com,edipo64@hotmail.com,pink33@centrum.sk,no-ni66@hotmail.com,helenblinkhorn@Gmail.com,emailgarybowes@yahoo.co.uk,Katharina.saesayellow@gmx.de,licazeus@hotmail.com,aniinhavidaal@icloud.com,majkagtx@gmail.com,saralou1984@hotmail.com,n_Warmbold@web.de,349275085@qq.com,jasmin.schleicher89@googlemail.com,missbell_27@hotmail.com,darja.starikova.82@mail.ru,ludyabreu@hotmail.com,ninit1985@gmail.com,roxy.joyce@sky.com,annikawaeke@icloud.com,amber.bonnell45@gmail.com,filouise@rocketmail.com,druzinagostic@gmail.com,sheehamunavar@gmail.com,jouy.melody@orange.fr,tessaapxOx@hotmail.com,s-ckoch@t-online.de,kimerlyd82@gmail.com,irena_gotthardt@yahoo.de,vivienmiko@gmail.com,stewarthooker408@outlook.com,drikarkow@hotmail.com,carmengunziger84@hotmail.com,meganandtim091@gmail.com,amady69@gmail.com,niloufar_123@hotmail.co.uk,bolvig@hotmail.com,cibelemulatinho@hotmail.com,aamanda.rodrigues@hotmail.com,angie.krupp@yahoo.de,sophie_48b_road@hotmaik.co.uk,lyndahinton282@btinternet.com,no1giggsfan@hotmail.com";
		String[] emails = mails.split("\\,");
		for(String m:emails){
			if(!checkEmail(m)){
				System.out.println(m);
			}
		}
		
    }


}
