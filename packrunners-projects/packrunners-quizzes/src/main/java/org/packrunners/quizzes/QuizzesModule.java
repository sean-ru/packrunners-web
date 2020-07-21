package org.packrunners.quizzes;

/**
 * This class is optional and represents the configuration for the courses module. By exposing
 * simple getter/setter/adder methods, this bean can be configured via content2bean using the
 * properties and node from <tt>config:/modules/courses</tt>. If you don't need this, simply remove
 * the reference to this class in the module descriptor xml.
 */
public class QuizzesModule {

    public static final String QUIZZES_REPOSITORY_NAME = "quizzes";

}

