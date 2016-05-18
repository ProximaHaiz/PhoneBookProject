package com.proxsoftware.webapp.annotation.phone;

import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Proxima on 13.05.2016.
 */
public class MobileConstraintValidator implements ConstraintValidator<MobileNumber, String> {
    private List<String> mobileCodeOperatorList = Arrays.asList("093", "063", "073", "067", "068",
            "091", "092", "050", "094", "066", "096", "097", "098", "099");
    private static final Logger logger = Logger.getLogger(MobileConstraintValidator.class);

    @Override
    public void initialize(MobileNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String mobileField, ConstraintValidatorContext context) {
        if (mobileField == null || mobileField.length() != 18) {
            return false;
        } else {
            logger.info("mobileField: " + mobileField + ",length = " + mobileField.length());
            return checkField(mobileField);
        }
    }

    private boolean checkField(String field) {
        CharSequence charSequence = field.substring(4, 7);
        return mobileCodeOperatorList.contains((String) charSequence);
    }
}
