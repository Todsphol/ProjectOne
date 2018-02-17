package th.co.todsphol.add.projectone.numbermanager;

import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;


public class PhoneNumberFilter extends NumberKeyListener {
    @Override
    public int getInputType() {
        return InputType.TYPE_CLASS_PHONE;
    }

    @Override
    protected char[] getAcceptedChars() {
        return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'};
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        if (source.length() == 1 && "0".equals(source.toString())) {
            return null;
        } else if (dstart == 0 && source.length() == 1) {
            if (!checkRuleFirstNumber(source)) return "";
        } else if (dest.length() == 1 && dest.charAt(0) == '6') {
            if (!checkRuleSecondNumber(source)) return "";
        }

        if (end > start && !"".equals(dest.toString())) {
            String destTxt = dest.toString();
            String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);

            // Phone number must match xxx-xxx-xxxx
            if (!resultingTxt.matches("^\\d{1,1}(\\d{1,1}(\\d{1,1}(\\-(\\d{1,1}(\\d{1,1}(\\d{1,1}(\\-(\\d{1,1}(\\d{1,1}(\\d{1,1}(\\d{1,1}?)?)?)?)?)?)?)?)?)?)?)?")) {
                return "";
            }
        }
        return null;
    }

    private boolean checkRuleFirstNumber(CharSequence s) {
        //CHECK ZERO OR SIX
        return s.charAt(0) == '0' || s.charAt(0) == '6';
    }

    private boolean checkRuleSecondNumber(CharSequence s) {
        return s.length() != 0 && s.charAt(0) == '6';
    }

}
