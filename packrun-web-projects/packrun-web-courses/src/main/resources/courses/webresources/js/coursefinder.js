var CourseFinder = CourseFinder || (function(){
    return {
        init : function(args) {
            angular
                .module('CourseFinder', ['ngAnimate', 'ngRoute'])
                .config(function($routeProvider) {
                    $routeProvider.when("/", {
                        templateUrl: args.contextPath + '/.resources/courses/webresources/views/index.html',
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
                    $scope.durations = [{ value: 2, name: 'courseFinder.resource.options.studyGuide' },
                                        { value: 7, name: 'courseFinder.resource.options.video' },
                                        { value: 21, name: 'courseFinder.resource.options.quiz' }];
                    $scope.useDurations = {};
                    $scope.useDestinations = {};
                    $scope.useCourseTypes = {};
                    $scope.search = {};

                    $scope.contextPath = args.contextPath;
                    $scope.language = args.language;
                    $scope.i18n = args.i18n;

                    if ($routeParams.duration) {
                        $scope.useDurations = {};
                        var split = $routeParams.duration.split(',');
                        for (var i in split) {
                            $scope.useDurations[split[i]] = true;
                        }
                    }
                    if ($routeParams.q) {
                        $scope.search.query = $routeParams.q;
                    }

                    // obtain the data
                    $http.get(args.restBase + '/tutors/v1/?lang=' + args.language).then(function(response) {
                        $scope.destinations = response.data.results;
                        if ($routeParams.destination) {
                            var split = $routeParams.destination.split(',');
                            for (var i in split) {
                                $scope.useDestinations[split[i]] = true;
                            }
                        }
                    }, function(response) {
                        console.error("Couldn't reach endpoint.");
                    });
                    $http.get(args.restBase + '/courseTypes/v1/?lang=' + args.language).then(function(response) {
                        $scope.courseTypes = response.data.results;
                        if ($routeParams.courseTypes) {
                            var split = $routeParams.courseTypes.split(',');
                            for (var i in split) {
                                $scope.useCourseTypes[split[i]] = true;
                            }
                        }
                    }, function(response) {
                        console.error("Couldn't reach endpoint.");
                    });
                    $http.get(args.restBase + '/courses/v1/?lang=' + args.language).then(function(response) {
                        $scope.courses = response.data.results;
                    }, function(response) {
                        console.error("Couldn't reach endpoint.");
                    });

                    // watch for changes
                    $scope.$watch(function() {
                        return {
                            useDurations: $scope.useDurations,
                            useDestinations: $scope.useDestinations,
                            useCourseTypes: $scope.useCourseTypes,
                            search: $scope.search,
                            destinations: $scope.destinations,
                            courseTypes: $scope.courseTypes,
                        };
                    }, function (newValues, oldValues) {
                        // wait for both courseTypes & tutors to be populated by async calls
                        if (newValues !== oldValues && newValues.courseTypes && newValues.tutors) {
                            var randomIndex = Math.floor(Math.random() * notFoundMessages.length);
                            $scope.notFoundMessage = notFoundMessages[randomIndex];

                            var qs = '';
                            var parameters = {duration: [], destination: [], courseTypes: [], q: []};
                            var durations = Object.keys(newValues.useDurations).reduce(function (filtered, key) {
                                    if (newValues.useDurations[key]) filtered.push(key);
                                    return filtered;
                            }, []);
                            var destinationKeys = Object.keys(newValues.useDestinations).reduce(function (filtered, key) {
                                    if (newValues.useDestinations[key]) filtered.push(key);
                                    return filtered;
                            }, []);
                            var courseTypeKeys = Object.keys(newValues.useCourseTypes).reduce(function (filtered, key) {
                                    if (newValues.useCourseTypes[key]) filtered.push(key);
                                    return filtered;
                            }, []);
                            if (durations.length > 0 && durations.length < $scope.durations.length) {
                                parameters.duration = durations;
                            }
                            if (destinationKeys.length > 0 && destinationKeys.length < newValues.destinations.length) {
                                parameters.destination = destinationKeys;
                            }
                            if (courseTypeKeys.length > 0 && courseTypeKeys.length < newValues.courseTypes.length) {
                                parameters.courseTypes = courseTypeKeys;
                            }
                            if (newValues.search.query) {
                                parameters.q = [newValues.search.query];
                            }
                            parameters.lang = [args.language];
                            var qs = '';
                            if (Object.keys(parameters).length > 0) {
                                var p = [];
                                Object.keys(parameters).forEach(function(key) {
                                    if (parameters[key].length > 0) {
                                        var values = parameters[key];
                                        p.push(key + '=' + values.join('|'));
                                        $location.search(key, values.join(','));
                                    } else {
                                        $location.search(key, null);
                                    }
                                });
                                qs = '?' + p.join('&');
                            }

                            $http.get(args.restBase + '/courses/v1/' + qs).then(function(response) {
                                $scope.filteredCourses = response.data.results;
                            });
                        }
                    }, true);
                }]);
        }
    }
})();
