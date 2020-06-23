var CourseFinder = CourseFinder || (function(){
    return {
        init : function(args) {
            angular
                .module('QuizFinder', ['ngAnimate', 'ngRoute'])
                .config(function($routeProvider) {
                    $routeProvider.when("/", {
                        templateUrl: args.contextPath + '/.resources/courses/webresources/views/course-finder-results.html',
                        controller: 'MainController',
                        reloadOnSearch: false
                    });
                })
                .controller('MainController', ['$scope', '$routeParams', '$http', '$location', function($scope, $routeParams, $http, $location) {
                    var notFoundMessages = ["courseFinder.search.noResults1",
                                            "courseFinder.search.noResults2",
                                            "courseFinder.search.noResults3"];
                    var randomIndex = Math.floor(Math.random() * notFoundMessages.length);
                    $scope.notFoundMessage = notFoundMessages[randomIndex];
                    $scope.useCourseTypes = {};
                    $scope.search = {};

                    $scope.contextPath = args.contextPath;
                    $scope.language = args.language;
                    $scope.i18n = args.i18n;

                    if ($routeParams.q) {
                        $scope.search.query = $routeParams.q;
                    }

                    // obtain the data
                    $http.get(args.restBase + '/courseTypes/v1/?lang=' + args.language).then(function(response) {
                        $scope.courseTypes = response.data.results;
                        if ($routeParams.courseTypes) {
                            var split = $routeParams.courseTypes.split(',');
                            for (var i in split) {
                                $scope.useCourseTypes[split[i]] = true;
                            }
                        }
                    }, function(response) {
                        console.error("Couldn't reach endpoint [/courseTypes/v1/].");
                    });

                    $http.get(args.restBase + '/quizzes/v1/?lang=' + args.language).then(function(response) {
                        $scope.courses = response.data.results;
                    }, function(response) {
                        console.error("Couldn't reach endpoint [/quizzes/v1/].");
                    });

                    // watch for changes
                    $scope.$watch(function() {
                        return {
                            useCourseTypes: $scope.useCourseTypes,
                            search: $scope.search,
                            courseTypes: $scope.courseTypes,
                        };
                    }, function (newValues, oldValues) {
                        // wait for both courseTypes to be populated by async calls
                        if (newValues !== oldValues && newValues.courseTypes) {
                            var randomIndex = Math.floor(Math.random() * notFoundMessages.length);
                            $scope.notFoundMessage = notFoundMessages[randomIndex];

                            var qs = '';
                            var parameters = {courseTypes: [], q: []};
                            var selectedCourseTypeKeys = Object.keys(newValues.useCourseTypes).reduce(function (filtered, key) {
                                    if (newValues.useCourseTypes[key]) filtered.push(key);
                                    return filtered;
                            }, []);
                            if (selectedCourseTypeKeys.length > 0 && selectedCourseTypeKeys.length < newValues.courseTypes.length) {
                                parameters.courseTypes = selectedCourseTypeKeys;
                            }
                            if (newValues.search.query) {
                                parameters.q = [newValues.search.query];
                            }
                            //parameters.lang = [args.language];

                            // -- Query Courses --
                            var qs = '';
                            if (Object.keys(parameters).length > 0) {
                                var p = [];
                                Object.keys(parameters).forEach(function(key) {
                                    if (parameters[key].length > 0) {
                                        var values = parameters[key];
                                        if (values.length>1) {
                                            p.push(key + '=' + values.join('|'));
                                        } else {
                                            p.push(key + '=' + values[0]);
                                        }
                                        $location.search(key, values.join(','));
                                    } else {
                                        $location.search(key, null);
                                    }
                                });
                                qs = '?' + p.join('&');
                            }

                            qs = encodeURI(args.restBase + '/quizzes/v1/' + qs);
                            $http.get(qs).then(function(response) {
                                $scope.filteredQuizzes = response.data.results;
                            });
                        }
                    }, true);
                }]);
        }
    }
})();
