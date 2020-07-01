package org.packrunners.videos;

/**
 * This class is optional and represents the configuration for the courses module. By exposing
 * simple getter/setter/adder methods, this bean can be configured via content2bean using the
 * properties and node from <tt>config:/modules/courses</tt>. If you don't need this, simply remove
 * the reference to this class in the module descriptor xml.
 */
public class VideosModule {

    public static final String VIDEOS_REPOSITORY_NAME = "videos";
    public final static String TEMPLATE_SUB_TYPE_COURSE_TYPE_OVERVIEW = "courseTypeOverview";

    /**
     * Specifies the default video node name (slug).
     */
    private String defaultVideoName;

    public String getDefaultVideoName() {
        return defaultVideoName;
    }

    public void setDefaultVideoName(String defaultVideoName) {
        this.defaultVideoName = defaultVideoName;
    }

}

