package com.zheng.common.validator;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;

public class NumberValidator extends ValidatorHandler<String> implements Validator<String>{

	private String fieldName;
	
	public NumberValidator(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public boolean validate(ValidatorContext context, String t) {
		boolean pass = true;
		if (t == null) {
			pass = false;
		}
		try {
			Long.parseLong(t);
		}catch (NumberFormatException e){
			pass = false;
			
		}
		if (! pass) {
			context.addError(ValidationError.create(String.format("%s必须是一个数值！", fieldName))
                    .setErrorCode(-1)
                    .setField(fieldName)
                    .setInvalidValue(t));
            return pass;
		}
		return true;
	}

}
