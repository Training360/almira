package locationsapp.controller;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Scanner;

public class CoordinatesValidator implements ConstraintValidator<Coordinates, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!value.matches("^-?\\d*\\.?\\d*,-?\\d*\\.?\\d*$")) {
            return false;
        }

        try {
            Scanner scanner = new Scanner(value).useDelimiter(",")
                    .useLocale(Locale.UK);
            double lat = scanner.nextDouble();
            double lon = scanner.nextDouble();

            if (lat < -90 || lat > 90) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("latitude must be between -90 and 90").addConstraintViolation();
                return false;
            }
            if (lon < -180 || lat > 180) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("longitude must be between -180 and 180").addConstraintViolation();
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
