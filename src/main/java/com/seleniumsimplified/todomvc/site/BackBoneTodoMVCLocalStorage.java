package com.seleniumsimplified.todomvc.site;

import com.seleniumsimplified.selenium.support.html5.Storage;
import com.seleniumsimplified.todomvc.localstorage.TodoMvcLocalStorage;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackBoneTodoMVCLocalStorage implements TodoMvcLocalStorage {

    private final String storage_namespace;
    private final Storage localStorage;

    public BackBoneTodoMVCLocalStorage(String storage_namespace, JavascriptExecutor js) {
        this.localStorage = new Storage(js);
        this.storage_namespace = storage_namespace;
    }

    @Override
    public List<String> itemTitles() {

        List<String> titles = new ArrayList();

        String[] indexSplit = {};
        String index = "";
        index = localStorage.getItem(storage_namespace);

        try{
            indexSplit = index.split(",");
        }catch(NullPointerException e){
            // it might be null
        }

        for(String key : indexSplit){
            String itemKey = storage_namespace + "-" + key;
            String item = localStorage.getItem(itemKey);
            // should really parse json into an object

            String GET_TITLE_PARSER_REGEX = "\\{'title':'(.+)',.*}".replaceAll("'", "\"");
            Pattern titleParser = Pattern.compile(GET_TITLE_PARSER_REGEX);
            Matcher matcher = titleParser.matcher(item);

            if(matcher.matches()){
                titles.add(matcher.group(1));
            }
        }

        return titles;
    }

    @Override
    public String titleAt(int index) {
        List<String> titles = itemTitles();
        return titles.get(index);
   }

    @Override
    public boolean containsTitle(String title) {
        List<String> titles = itemTitles();
        return titles.contains(title);
    }

    @Override
    public long length() {
        return itemTitles().size();
    }
}
