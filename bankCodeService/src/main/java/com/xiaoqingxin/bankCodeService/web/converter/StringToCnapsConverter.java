package com.xiaoqingxin.bankCodeService.web.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.core.convert.converter.Converter;

import com.xiaoqingxin.bankCodeService.model.Cnaps;
import com.xiaoqingxin.bankCodeService.utils.StringUtils;

/**
* @ClassName: StringToCnapsConverter
* @Description: 自定义格式化器
* @author Administrator
* @date 2018年10月14日
 */
public class StringToCnapsConverter implements Converter<String, Cnaps> {

	@Override
	public Cnaps convert(String source) {

//		if (StringUtils.isBlank(source))
//			return null;
//
//		if (source.indexOf("-") == -1)
//			return null;

		String[] arr = source.split("-");

//		if (arr.length < 7)
//			return null;

		Cnaps cnaps = new Cnaps();
		cnaps.setCode(arr[0]);
		cnaps.setName(arr[1]);
		cnaps.setClearingBankCode(arr[2]);
		if (StringUtils.isBlank(arr[3])) {
			cnaps.setClearingBankLevel(0);
		} else {
			cnaps.setClearingBankLevel(Integer.parseInt(arr[3]));
		}
		cnaps.setProviderCode(arr[4]);
		cnaps.setAdCode(arr[5]);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

		if (StringUtils.isBlank(arr[6])) {
			cnaps.setCreateDate(null);
		} else {
			try {
				cnaps.setCreateDate(sdf.parse(arr[6]));
			} catch (ParseException e) {
				throw new RuntimeException("", e);
			}
		}
		
		if (StringUtils.isBlank(arr[7])) {
			cnaps.setCreateDate(null);
		} else {
			try {
				cnaps.setLastModifyDate(sdf.parse(arr[7]));
			} catch (ParseException e) {
				throw new RuntimeException("", e);
			}
		}

		if (StringUtils.isBlank(arr[8])) {
			cnaps.setVision(0);
		} else {
			cnaps.setVision(Integer.parseInt(arr[8]));
		}
		cnaps.setCreateDateStart(arr[9]);
		cnaps.setCreateDateEnd(arr[10]);
		cnaps.setLastModifyDateStart(arr[11]);
		cnaps.setLastModifyDateEnd(arr[12]);

		return cnaps;
	}

}
