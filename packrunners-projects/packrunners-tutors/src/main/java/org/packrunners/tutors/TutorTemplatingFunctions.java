package org.packrunners.tutors;

import info.magnolia.jcr.util.ContentMap;
import org.packrunners.categories.Category;
import org.packrunners.tutors.service.Tutor;
import org.packrunners.tutors.service.TutorServices;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Node;


/**
 * Useful functions for templating related to Tutor functions.
 */
@Singleton
public class TutorTemplatingFunctions {

    private final TutorServices tutorServices;

    @Inject
    public TutorTemplatingFunctions(TutorServices tutorServices) {
        this.tutorServices = tutorServices;
    }


    public Category getCategoryByUrl() {
        return tutorServices.getCategoryByUrl();
    }

    /**
     * Returns the link to the school this tutor belongs.
     *
     * <p>Will use given {@link ContentMap} to find feature page of type
     * {@link TutorsModule#TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW} to link to.</p>
     */
    public String getSchoolLink(ContentMap contentMap, String schoolName) {
        return getSchoolLink(contentMap.getJCRNode(), schoolName);
    }

    /**
     * Returns the link to a school.
     *
     * <p>Will use given {@link Node} to find feature page of type
     * {@link TutorsModule#TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW} to link to.</p>
     */
    public String getSchoolLink(Node content, String schoolName) {
        return tutorServices
                .getCategoryLink(content, schoolName, TutorsModule.TEMPLATE_SUB_TYPE_SCHOOL_OVERVIEW);
    }

    public String getTutorLink(ContentMap tutorContentMap) {
        return getTutorLink(tutorContentMap.getJCRNode());
    }

    public String getTutorLink(Node tutorNode) {
        return tutorServices.getTutorLink(tutorNode);
    }

    /**
     * Allows marshalling of tutor node from templates.
     */
    public Tutor marshallTutorNode(Node tutorNode) {
        return tutorServices.marshallTutorNode(tutorNode);
    }

    public Tutor marshallTutorNode(ContentMap tutorContentMap) {
        return marshallTutorNode(tutorContentMap.getJCRNode());
    }
}

