package com.ashraf.amr.mirvalidation;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import com.ashraf.amr.mirtoast.ToastCreator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by amr on 08/04/2018.
 */

public class Validation {

    private static String STRING_PATTERN = "^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$";
    private static String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static void cleanError(List<TextInputLayout> textInputLayoutList) {

        for (int i = 0; i < textInputLayoutList.size(); i++) {
            textInputLayoutList.get(i).setErrorEnabled(false);
        }

    }

    public static boolean validationLength(Activity activity, String text, String errorText) {

        if (text.length() <= 0) {
            ToastCreator.onCreateErrorToast(activity, errorText);
            return false;
        } else {
            return true;
        }
    }

    public static boolean validationLength(EditText text, String errorText) {
        if (text.length() <= 0) {
            text.setError(errorText);
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationLength(TextInputLayout text, String errorText) {
        if (Objects.requireNonNull(text.getEditText()).length() <= 0) {
            text.setError(errorText);
            return false;
        } else {
            return true;
        }
    }

    public static boolean validationLength(Activity activity, String text, String errorText, int length) {

        if (text.length() <= length) {
            //ToastCreator.onCreateErrorToast(activity, errorText);

            return false;
        } else {
            return true;
        }
    }

    public static boolean validationLength(TextInputEditText text, String errorText, int length) {
        if (text.length() <= length) {
            text.setError(errorText);
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationLength(TextInputLayout text, String errorText, int length) {
        if (Objects.requireNonNull(text.getEditText()).length() < length) {
            text.setError(errorText);
            return false;
        } else {
            return true;
        }
    }

    public static boolean validationStringIsCharAndNumber(Activity activity, String text, String errorText) {

        if (!text.matches(STRING_PATTERN)) {
            ToastCreator.onCreateErrorToast(activity, errorText);
            return false;
        } else {
            return true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationStringIsCharAndNumber(TextInputEditText text, String errorText) {

        if (!Objects.requireNonNull(text.getText()).toString().matches(STRING_PATTERN)) {
            text.setError(errorText);
            return false;
        } else {
            return true;
        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public static boolean validationStringIsCharAndNumber(TextInputLayout text, String errorText) {
//
//        if (!Objects.requireNonNull(text.getEditText()).getText().toString().matches(STRING_PATTERN)) {
//            text.setError(errorText);
//            return false;
//        } else {
//            return true;
//        }
//    }

//    public static boolean validationStringIsNumber(Activity activity, String text, String errorText) {
//
//        try {
//            int convert = Integer.parseInt(text);
//            return true;
//        } catch (Exception e) {
//            ToastCreator.onCreateErrorToast(activity, errorText);
//            return false;
//        }
//
//    }
//
//    public static boolean validationStringIsNumber(EditText text, String errorText) {
//
//        try {
//            int convert = Integer.parseInt(text.getText().toString());
//            return true;
//        } catch (Exception e) {
//            text.setError(errorText);
//            return false;
//        }
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public static boolean validationStringIsNumber(TextInputLayout text, String errorText) {
//
//        try {
//            int convert = Integer.parseInt(Objects.requireNonNull(text.getEditText()).getText().toString());
//            return true;
//        } catch (Exception e) {
//            Objects.requireNonNull(text.getEditText()).setError(errorText);
//            return false;
//        }
//    }

    private static boolean validationEditTextsEmpty(List<EditText> editTexts, String errorText) {

        List<Boolean> booleans = new ArrayList<>();

        for (int i = 0; i < editTexts.size(); i++) {
            if (!validationLength(editTexts.get(i), errorText)) {
                booleans.add(false);
            } else {
                booleans.add(true);
            }
        }

        return !booleans.contains(false) || booleans.contains(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationTextInputLayoutListEmpty(List<TextInputLayout> textInputLayoutList, String errorText) {

        List<Boolean> booleans = new ArrayList<>();

        for (int i = 0; i < textInputLayoutList.size(); i++) {
            if (!validationLength(textInputLayoutList.get(i), errorText)) {
                booleans.add(false);
            } else {
                booleans.add(true);
            }
        }

        return !booleans.contains(false) || booleans.contains(true);
    }

    private static boolean validationSpinnersEmpty(List<Spinner> spinners) {

        List<Boolean> booleans = new ArrayList<>();

        for (int i = 0; i < spinners.size(); i++) {
            if (spinners.get(i).getSelectedItemPosition() == 0) {
                booleans.add(false);
            } else {
                booleans.add(true);
            }
        }

        return !booleans.contains(false) || booleans.contains(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationAllEmpty(List<EditText> editTexts, List<TextInputLayout> textInputLayouts, List<Spinner> spinners, String errorText) {

        return validationEditTextsEmpty(editTexts, errorText) && validationTextInputLayoutListEmpty(textInputLayouts, errorText)
                && validationSpinnersEmpty(spinners);
    }

    public static boolean validationPhone(Activity activity, String phone) {

        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        assert manager != null;
        String locale = manager.getSimCountryIso().toUpperCase();

        Country country = new Country();

        Country country1 = country.getCountry(locale);

        String phone1 = phone.replace("+2", "");

        if (phone1.length() >= country1.getLength_min() && phone.length() <= country1.getLength_max()) {
            return true;
        } else {
            ToastCreator.onCreateErrorToast(activity, activity.getString(R.string.invalid_phone1) + " " + country1.getLength_min()
                    + " " + activity.getString(R.string.invalid_phone2));
            return false;
        }
    }

    public static boolean validationPhone(Activity activity, EditText phone) {

        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        assert manager != null;
        String locale = manager.getSimCountryIso().toUpperCase();

        Country country = new Country();

        Country country1 = country.getCountry(locale);

        String phone1 = phone.getText().toString().replace("+2", "");
        if (phone1.length() >= country1.getLength_min() && phone.getText().length() <= country1.getLength_max()) {
            return true;
        } else {
            phone.setError(activity.getString(R.string.invalid_phone1) + " " + country1.getLength_min()
                    + " " + activity.getString(R.string.invalid_phone2));
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationPhone(Activity activity, TextInputEditText phone) {

        TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        assert manager != null;
        String locale = manager.getSimCountryIso().toUpperCase();

        Country country = new Country();

        Country country1 = country.getCountry(locale);

        String phone1 = Objects.requireNonNull(phone.getText()).toString().replace("+2", "");

        if (phone1.length() >= country1.getLength_min() && phone.getText().length() <= country1.getLength_max()) {
            return true;
        } else {
            //phone.setError(activity.getString(R.string.empty));
            phone.setError(activity.getString(R.string.invalid_phone1) + " " + country1.getLength_min()
                    + " " + activity.getString(R.string.invalid_phone2));
            return false;
        }
    }

    public static boolean validationEmail(Activity activity, String email) {

        if (!email.matches(EMAIL_PATTERN)) {
            ToastCreator.onCreateErrorToast(activity, activity.getString(R.string.invalid_email));
            return false;
        } else {
            return true;
        }

    }

    public static boolean validationEmail(Activity activity, EditText email) {

        if (!email.getText().toString().matches(EMAIL_PATTERN)) {
            email.setError(activity.getString(R.string.invalid_email));
            return false;
        } else {
            return true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationEmail(Activity activity, TextInputLayout email) {

        if (!Objects.requireNonNull(email.getEditText()).getText().toString().matches(EMAIL_PATTERN)) {
            email.getEditText().setError(activity.getString(R.string.invalid_email));
            return false;
        } else {
            return true;
        }
    }

    public static boolean validationPassword(Activity activity, String password, int length, String errorText) {

        validationLength(activity, password, errorText, length);
        validationStringIsCharAndNumber(activity, password, errorText);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationPassword(TextInputEditText password, int length, String errorText) {

//        if (validationLength(password, errorText, length) && validationStringIsCharAndNumber(password, errorText)){
//            return true;
//        }else {
//            //validationLength(password, errorText, length);
//            //validationStringIsCharAndNumber(password, errorText);
//            return false;
//        }
        if (password.length() >= length ) {
            return true;
        } else {
            password.setError(errorText);
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationPassword(TextInputLayout password, int length, String errorText) {

        return validationLength(password, errorText, length);

    }

    public static boolean validationConfirmPassword(Activity activity, String password, String confirmPassword) {

        if (password.equals(confirmPassword)) {
            return true;
        } else {
            ToastCreator.onCreateErrorToast(activity, activity.getString(R.string.invalid_confirm_password));
            return false;
        }
    }

    public static boolean validationConfirmPassword(Activity activity, EditText password, EditText confirmPassword) {

        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            return true;
        } else {
            confirmPassword.setError(activity.getString(R.string.invalid_confirm_password));
            return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean validationConfirmPassword(Activity activity, TextInputLayout password, TextInputEditText confirmPassword) {

        if (Objects.requireNonNull(password.getEditText()).getText().toString().equals(Objects.requireNonNull(confirmPassword.getText()).toString())) {
            return true;
        } else {
            confirmPassword.setError(activity.getString(R.string.invalid_confirm_password));
            return false;
        }

    }

}
