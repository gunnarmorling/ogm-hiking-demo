angular

    .module('hikesApp', ['ngRoute', 'restangular'])

    .config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('hiking-manager');
    })

    .config(function($routeProvider) {
        $routeProvider
            .when('/', {
                controller:'HikesCtrl',
                templateUrl:'list.html'
            })
            .when('/new', {
                controller:'DetailCtrl',
                templateUrl:'detail.html'
            })
            .otherwise({
                redirectTo:'/'
            });
    })

    .factory('PersistenceService', ['Restangular', function(Restangular) {
        return {
            getHikes: function(searchTerm) {
                if(searchTerm) {
                    var queryParamObj = { q: searchTerm };
                    return Restangular.all('hikes').getList(queryParamObj);
                }
                else {
                    return Restangular.all('hikes').getList();
                }
            },
            getPersons: function() {
                return Restangular.all('persons').getList();
            },
            createHike: function(hike, sections, organizer) {
                var hikeDesc = hike;
                hikeDesc.from = hike.from;
                hikeDesc.to = hike.to;
                hikeDesc.sections = [];

                var arrayLength = sections.length;
                for (var i = 0; i < arrayLength; i++) {
                    hikeDesc.sections.push({ from: sections[i].from, to: sections[i].to });
                }

                if ( organizer ) {
                    hikeDesc.organizer = { id: organizer.id };
                }

                return Restangular.all('hikes').post(hikeDesc);
            },
            deleteHike: function(hike) {
                return Restangular.one('hikes', hike.id).remove();
            }
        }
    }])

    .controller('HikesCtrl', function($scope, PersistenceService) {
        $scope.hikes;

        $scope.getHikes = function() {
            PersistenceService.getHikes($scope.searchTerm).then(function (hikes) {
                $scope.hikes = hikes;
            });
        };

        $scope.remove = function(hike) {
            PersistenceService.deleteHike(hike).then(function (hike) {
                $scope.getHikes();
            });
        };

        $scope.getHikes();
    })

    .controller('DetailCtrl', function($scope,  $location, PersistenceService) {
        PersistenceService.getPersons().then(function (persons) {
            $scope.persons = persons;
        });

        $scope.sections = [];

        $scope.save = function() {
            PersistenceService.createHike($scope.hike, $scope.sections, $scope.selectedOrganizer).then(function (hike, sections) {
                $location.path('/');
            });
        };
        $scope.cancel = function() {
            $location.path('/');
        };
    })

    .run(function($rootScope) {
        $rootScope.$on('$routeChangeSuccess', function(ev, data) {
            if (data.$$route && data.$$route.controller) {
                $rootScope.controller = data.$$route.controller;
            }
       })
    });
