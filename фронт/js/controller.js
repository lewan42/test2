var app = angular.module("Application", ['ui.bootstrap']);

app.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);


app.controller('TodoController', function ($scope, $http) {
    $scope.tree = [];
    $scope.filteredTodos = []
        , $scope.currentPage = 1
        , $scope.numPerPage = 10
        , $scope.maxSize = 5;

    $scope.generate = function (data) {
        var post = data.nodes.length + 1;
        var newName = data.name + '-' + post;
        var newLastName = data.lastName + '-' + post;

        var json_data = {
            name: newName,
            lastName: newLastName,
            parentID: data._Id
        }

        $scope.post(json_data, data);
    };

    $scope.create = function (data) {
        localStorage.setItem("id", data._Id);
    }

    $scope.edit = function (data) {
        localStorage.setItem("id", data._Id);
        localStorage.setItem("name", data.name);
        localStorage.setItem("lastName", data.lastName);
    }

    $scope.post = function (json_data, data) {
        $http({
            method: 'POST',
            url: 'http://localhost:8080/generate',
            data: json_data

        }).then(function (response) {

            if (response.status !== 200) {
                alert("error " + response.status);
                return;
            }

            data.nodes.push({
                _Id: response.data,
                name: json_data.name,
                lastName: json_data.lastName,
                parentID: data._Id,
                parentNode: data,
                nodes: []
            });

            alert("Generate success");
        });
    }


    $scope.search = function () {
        $scope.init();
        $scope.currentPage = 1;
    }


    $scope.init = function () {

        if (!$scope.text) {

            $http({
                method: 'GET',
                url: 'http://localhost:8080/getdata_count'

            }).then(function (response) {

                if (response.status != 200) {
                    alert("Error " + response.status);
                    return;
                }

                $scope.totalItems = response.data / $scope.numPerPage * 10;
                $scope.get();
            });

        } else {

            url = 'http://localhost:8080/getdata_count_filter'
            var params = {
                str: $scope.text
            };


            $http({
                method: 'GET',
                url: url,
                params: params

            }).then(function (response) {

                if (response.status != 200) {
                    alert("error " + response.status);
                    return;
                }

                $scope.totalItems = response.data / $scope.numPerPage * 10;
                $scope.get();
            });
        }
    };

    $scope.get = function () {
        $scope.tree = [];
        var begin = (($scope.currentPage - 1) * $scope.numPerPage);
        var end = begin + $scope.numPerPage;

        var url = 'http://localhost:8080/getdata'
        var params = {
            begin: begin,
            end: end
        };

        if ($scope.text) {

            url = 'http://localhost:8080/getdata_filter'
            var params = {
                begin: begin,
                end: end,
                str: $scope.text
            };
        }

        $http({
            method: 'GET',
            url: url,
            params: params

        }).then(function success(response) {

            if (response.status != 200) {
                alert("error " + response.status);
                return;
            }

            if ($scope.text)
                $scope.tree = response.data;
            else
                $scope.tree = $scope.transform_to_tree(response.data, -1);

        }).then(function error(response) {
            console.log("Error=" + response);
        });
    };


    $scope.$watch('currentPage + numPerPage', function () {

        var begin = (($scope.currentPage - 1) * $scope.numPerPage);
        var end = begin + $scope.numPerPage;
        $scope.init();
    });

    $scope.delete = function (data) {

        $http({
            method: 'DELETE',
            url: 'http://localhost:8080/delete',
            params: {
                delete: data._Id
            }

        }).then(function(response) {

            console.log(response.data);

            if (response.status != 200) {
                alert("error " + response.status);
                return;
            }

            if (response.data >= 1) {
                $scope.delete_child(data);
                $scope.delete_self(data);
                alert("Deleted success");
            }
            else {
                alert("Not found node");
            }

        });
    };

    $scope.delete_child = function (data) {
        data.nodes = [];
    };

    $scope.delete_self = function (data) {

        if (data.parentID == -1) {
            var index = $scope.tree.indexOf(data);
            if (index > -1) {
                $scope.tree.splice(index, 1);
            }
        } else {

            var index = data.parentNode.nodes.indexOf(data);
            if (index > -1) {
                data.parentNode.nodes.splice(index, 1);
            }
        }
    };


    $scope.transform_to_tree = function (arr, parentID) {
        var out = [];
        for (var i in arr) {

            if (arr[i].parentID == parentID) {
                var nodes = $scope.transform_to_tree(arr, arr[i]._Id);
                if (nodes !== null) {
                    arr[i].nodes = nodes;
                    for (var j in arr) {
                        if (arr[j]._Id == parentID)
                            arr[i].parentNode = arr[j];
                    }
                }
                out.push(arr[i]);
            }
        }
        return out;
    }
});

