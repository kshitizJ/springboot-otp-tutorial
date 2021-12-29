package com.springboototp.emailTemplate;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

public class EmailTemplate {

    private String template;
    // private Map<String, String> replacementParams;

    /**
     * 
     * constructor which loads the template
     * 
     * @param customTemplate
     */
    public EmailTemplate(String customTemplate) {
        try {
            this.template = loadTemplate(customTemplate);
        } catch (Exception e) {
            this.template = "Empty";
            System.out.println(e.getMessage());
        }
    }

    /**
     * 
     * getting the template from the html file
     * 
     */
    private String loadTemplate(String customTemplate) throws Exception {

        // The Java ClassLoader is a part of the Java Runtime Environment that
        // dynamically loads Java classes into the Java Virtual Machine. The Java run
        // time system does not need to know about files and file systems because of
        // classloaders.
        ClassLoader classLoader = getClass().getClassLoader();

        // getting the URL of the template from the getResource() method
        URL resouce = classLoader.getResource(customTemplate);

        // loading the file from the resource instance which converts URL to URI.
        File file = new File(resouce.toURI());

        String content;

        try {

            // getting all the file as string
            content = new String(Files.readAllBytes(file.toPath()));

        } catch (Exception e) {

            // if something went wrong during reading the template then throw exception
            throw new Exception("Could not read template = " + customTemplate);

        }

        return content;
    }

    /**
     * 
     * this method returns the replaced string with the original value
     * 
     */
    public String getTemplate(Map<String, String> replacements) {

        // assigning the variable with the template String
        String cTemplate = this.template;

        // replace the String
        for (Map.Entry<String, String> entry : replacements.entrySet()) {

            // replacing each key of the template with it's value
            cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());

        }

        // returning the updated template which containst the OTP and the name
        return cTemplate;
    }

}
