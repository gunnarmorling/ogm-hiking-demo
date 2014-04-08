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
            .when('/edit/:projectId', {
                controller:'HikesCtrl',
                templateUrl:'detail.html'
            })
            .when('/new', {
                controller:'DetailCtrl',
                templateUrl:'detail.html'
            })
            .otherwise({
                redirectTo:'/'
            });
    })

    .factory('HikeFactory', ['Restangular', function(Restangular) {
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
            create: function(hike) {
                var hikeDesc = hike;
                hikeDesc.from = hike.from;
                hikeDesc.to = hike.to;

                return Restangular.all('hikes').post(hikeDesc);
            }
        }
    }])

    .controller('HikesCtrl', function($scope, HikeFactory) {
        $scope.hikes;

        $scope.getHikes = function() {
            HikeFactory.getHikes($scope.searchTerm).then(function (hikes) {
                $scope.hikes = hikes;
            });
        };

        $scope.getHikes();
    })

    .controller('DetailCtrl', function($scope,  $location, HikeFactory) {
        $scope.save = function() {
            HikeFactory.create($scope.hike).then(function (hike) {
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
