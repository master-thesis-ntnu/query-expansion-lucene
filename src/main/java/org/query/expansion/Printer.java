package org.query.expansion;

import org.query.expansion.models.Photo;

public class Printer {
    public static void printResult(Photo[] photos) {
        for (Photo photo : photos) {
            prettyPrintPhotoData(photo);
        }

        System.out.println("Number of hits in the result: " + photos.length);
    }

    private static void prettyPrintPhotoData(Photo photo) {
        System.out.println("---------------");
        System.out.println(photo.getTitle());

        String tags = "Tags: ";
        for (String tag : photo.getTags()) {
            tags += tag + ", ";
        }
        System.out.println(tags);

        System.out.println("---------------");
    }
}
