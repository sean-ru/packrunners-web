package org.packrunners.tutors;

/**
 * This class is optional and represents the configuration for the courses module. By exposing
 * simple getter/setter/adder methods, this bean can be configured via content2bean using the
 * properties and node from <tt>config:/modules/courses</tt>. If you don't need this, simply remove
 * the reference to this class in the module descriptor xml.
 */
public class TutorsModule {

    public static final String TUTORS_REPOSITORY_NAME = "tutors";

    public final static String TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW = "schoolOverview";

    /**
     * Specifies the default tutor node name.
     */
    private String defaultTutorName;

    public String getDefaultTutorName() {
        return defaultTutorName;
    }

    public void setDefaultTutorName(String defaultTutorName) {
        this.defaultTutorName = defaultTutorName;
    }

}

