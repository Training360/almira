package locationsapp.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Locale;
import java.util.Scanner;

public class CreateLocationCommandValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateLocationCommand.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "errors.name.empty", "Name cannot be empty!");
        CreateLocationCommand command = (CreateLocationCommand) o;

        if (!command.getCoords().matches("^-?\\d*\\.?\\d*,-?\\d*\\.?\\d*$")) {
            errors.rejectValue("coords", "errors.coords.invalid", "Invalid coordinates format!");
        }
        else {
            Scanner scanner = new Scanner(command.getCoords()).useDelimiter(",")
                    .useLocale(Locale.UK);
            try {
                double lat = scanner.nextDouble();
                double lon = scanner.nextDouble();

                if (lat < -90 || lat > 90) {
                    errors.rejectValue("coords", "errors.coords.invalid", "Latitude must be between -90 and 90!");
                }
                if (lon < -180 || lat > 180) {
                    errors.rejectValue("coords", "errors.coords.invalid", "Longitude must be between -180 and 180!");
                }
            } catch (Exception e) {
                errors.rejectValue("coords", "errors.coords.invalid", "Invalid coordinates format!");
            }
        }
    }
}
