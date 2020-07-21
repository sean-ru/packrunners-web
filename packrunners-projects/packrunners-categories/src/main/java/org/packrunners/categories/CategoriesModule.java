package org.packrunners.categories;

/**
 * This class is optional and represents the configuration for the courses module. By exposing
 * simple getter/setter/adder methods, this bean can be configured via content2bean using the
 * properties and node from <tt>config:/modules/courses</tt>. If you don't need this, simply remove
 * the reference to this class in the module descriptor xml.
 */
public class CategoriesModule {

    /**
     * Specifies the root node of schools.
     */
    private String schoolRootNode;

    /**
     * Specifies the root node of courseType categories.
     */
    private String courseTypeRootNode;

    /**
     * Specifies the root node of resourceType categories.
     */
    private String resourceTypeRootNode;

    /**
     * Specifies the root node of courseName categories.
     */
    private String courseNumberRootNode;

    public String getSchoolRootNode() {
        return schoolRootNode;
    }

    public void setSchoolRootNode(String schoolRootNode) {
        this.schoolRootNode = schoolRootNode;
    }

    public String getCourseTypeRootNode() {
        return courseTypeRootNode;
    }

    public void setCourseTypeRootNode(String courseTypeRootNode) {
        this.courseTypeRootNode = courseTypeRootNode;
    }

    public String getResourceTypeRootNode() {
        return resourceTypeRootNode;
    }

    public void setResourceTypeRootNode(String resourceTypeRootNode) {
        this.resourceTypeRootNode = resourceTypeRootNode;
    }

    public String getCourseNumberRootNode() {
        return courseNumberRootNode;
    }

    public void setCourseNumberRootNode(String courseNumberRootNode) {
        this.courseNumberRootNode = courseNumberRootNode;
    }
}

