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
        var hikesResource = Restangular.all('hikes');
        var personsResource = Restangular.all('persons');

        return {
            getHikes: function(searchTerm) {
                if(searchTerm) {
                    return hikesResource.getList({ q: searchTerm });
                }
                else {
                    return hikesResource.getList();
                }
            },
            getPersons: function() {
                return personsResource.getList();
            },
            createHike: function(hike) {
                return hikesResource.post(hike);
            },
            deleteHike: function(hike) {
                return Restangular.one('hikes', hike.id).remove();
            }
        }
    }])

    .controller('HikesCtrl', function($scope, PersistenceService) {
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

        $scope.hike = { sections: [] };

        $scope.save = function() {
            PersistenceService.createHike($scope.hike).then(function (hike) {
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
