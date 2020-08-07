package org.packrunners.courses.model;

import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import lombok.extern.slf4j.Slf4j;
import org.packrunners.categories.Category;
import org.packrunners.categories.model.definition.CategoryTemplateDefinition;
import org.packrunners.courses.service.Course;
import org.packrunners.courses.service.CourseServices;
import org.packrunners.studyguides.service.StudyGuide;
import org.packrunners.studyguides.service.StudyGuideServices;
import org.packrunners.videos.service.Video;
import org.packrunners.videos.service.VideoServices;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Model for displaying a course and its related courses by type or category.
 */
@Slf4j
public class CourseDetailModel<RD extends CategoryTemplateDefinition> extends
        RenderingModelImpl<RD> {

    private final CourseServices courseServices;
    private final StudyGuideServices studyGuideServices;
    private final VideoServices videoServices;

    @Inject
    public CourseDetailModel(Node content, RD definition, RenderingModel<?> parent,
                             CourseServices courseServices, StudyGuideServices studyGuideServices,
                             VideoServices videoServices
    ) {
        super(content, definition, parent);
        this.courseServices = courseServices;
        this.studyGuideServices = studyGuideServices;
        this.videoServices = videoServices;
    }

    public Course getCourse() {
        try {
            final Node node = courseServices.getCourseNodeByParameter();
            return courseServices.marshallCourseNode(node);
        } catch (RepositoryException e) {
            log.error("Could not get course by course parameter.", e);
        }
        return null;
    }

    public List<StudyGuide> getStudyGuidesByCourseNumber(List<Category> courseNumbers) {
        List<StudyGuide> studyGuides = new ArrayList<>();
        if (courseNumbers != null && !courseNumbers.isEmpty()) {
            studyGuides = courseNumbers.stream().map(category -> {
                String categoryName = definition.getCategory();
                String identifier = category.getIdentifier();
                return studyGuideServices.getStudyGuidesByCategory(categoryName, identifier);
            })
                    .flatMap(Collection::stream)
                    .filter(distinctByKey(StudyGuide::getIdentifier))
                    .collect(Collectors.toList());
        }
        return studyGuides;
    }

    public List<Video> getVideosByCourseNumber(List<Category> courseNumbers) {
        List<Video> videos = new ArrayList<>();
        if (courseNumbers != null && !courseNumbers.isEmpty()) {
            videos = courseNumbers.stream().map(category -> {
                String categoryName = definition.getCategory();
                String identifier = category.getIdentifier();
                return videoServices.getVideosByCategory(categoryName, identifier);
            })
                    .flatMap(Collection::stream)
                    .filter(distinctByKey(Video::getIdentifier))
                    .collect(Collectors.toList());
        }
        return videos;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
