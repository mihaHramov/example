package aaa.bbb.ccc.solidsnake.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aaa.bbb.ccc.solidsnake.R;

public class EmailParser {
    private Context context;

    public EmailParser(Context context) {
        this.context = context;
    }

    public List<String> pars(String html) {
        String pattern = context.getString(R.string.find_email_reg);
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(html);
        List<String> strings = new ArrayList<>();
        while (m.find()) {
            if (isCorrectEmail(m.group())){
                strings.add(m.group());
            }
        }
        return strings;
    }


    private Boolean isCorrectEmail(String email) {
        String[] shortcuts = context.getResources().getStringArray(R.array.file);
        for (String template : shortcuts) {
            if (email.endsWith(template)) {
                return false;
            }
        }
        return true;
    }
}
