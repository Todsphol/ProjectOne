package th.co.todsphol.add.projectone.numbermanager;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberWatcher implements TextWatcher {
    private boolean isFormatting;
    private boolean deletingHyphen;
    private int hyphenStart;
    private boolean deletingBackward;
    public PhoneNumberWatcher(EditText vEdt) {
        vEdt.setFilters(new InputFilter[]{new PhoneNumberFilter(), new InputFilter.LengthFilter(12)});
    }
    @Override
    public void afterTextChanged(Editable text) {
        if (isFormatting)
            return;
        isFormatting = true;

        // If deleting hyphen, also delete character before or after it
        if (deletingHyphen && hyphenStart > 0) {
            if (deletingBackward) {
                if (hyphenStart - 1 < text.length()) {
                    text.delete(hyphenStart - 1, hyphenStart);
                }
            } else if (hyphenStart < text.length()) {
                text.delete(hyphenStart, hyphenStart + 1);
            }
        }
        if (text.length() >= 2 && text.length() <= 12) {
            StringBuffer sTemp = new StringBuffer(text.toString().replaceAll("-", "").replaceAll("\\+", ""));
            int length = 0;

            if (text.toString().charAt(1) == '0')
                text.delete(1, 2);

            if (sTemp.length() >= 2 && "66".equals(sTemp.substring(0, 2))) {
                sTemp = sTemp.replace(0, 2, "0");
                length = 2;
            }
            if (sTemp.length() == 10) {
                sTemp = sTemp.insert(3, "-");
                sTemp = sTemp.insert(7, "-");
                length = text.length();
            }
            if (length != 0) {
                text.replace(0, length, sTemp.toString());
            }
        }
        if (text.length() == 3 || text.length() == 7) {
            text.append('-');
        }
        isFormatting = false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (isFormatting)
            return;

        // Make sure user is deleting one char, without a selection
        final int selStart = Selection.getSelectionStart(s);
        final int selEnd = Selection.getSelectionEnd(s);
        if (s.length() > 1 // Can delete another character
                && count == 1 // Deleting only one character
                && after == 0 // Deleting
                && s.charAt(start) == '-' // a hyphen
                && selStart == selEnd) { // no selection
            deletingHyphen = true;
            hyphenStart = start;
            // Check if the user is deleting forward or backward
            if (selStart == start + 1) {
                deletingBackward = true;
                return;
            }
            deletingBackward = false;
        } else {
            deletingHyphen = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
