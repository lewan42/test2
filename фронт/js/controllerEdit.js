angular.module("Application3", ['ui.bootstrap'])
    .controller("edit", function ($scope, $http, $rootScope) {

        $scope.name = localStorage.getItem("name");
        $scope.lastName = localStorage.getItem("lastName");

        $scope.save = function (node) {

            if (!$scope.valid_fields(node)) {
                alert("Write all fields!");
                return;
            }

            var json_data = {
                id: localStorage.getItem("id"),
                name: node[0],
                lastName: node[1]
            }


            $http({
                method: 'PATCH',
                url: 'http://localhost:8080/edit',
                data: json_data

            }).then(function (response) {

                if (response.status !== 200) {
                    alert("Error " + response.status);
                    return;
                }

                alert("Edit success");

                window.location.href = '/';

            });
        }

        $scope.back = function () {
            window.location.href = '/';
        }

        $scope.valid_fields = function (node) {
            return node[0] != null && node[1] != null;
        }
    })
