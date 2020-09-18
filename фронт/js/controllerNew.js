angular.module("Application2", ['ui.bootstrap'])
    .controller("create", function ($scope, $http, $rootScope) {

        $scope.save = function (node) {

            if (!$scope.valid_fields(node)) {
                alert("Fill all fields!");
                return;
            }

            var json_data = {
                name: node[0],
                lastName: node[1],
                parentID: localStorage.getItem("id")
            }

            $http({
                method: 'POST',
                url: 'http://localhost:8080/generate',
                data: json_data

            }).then(function (response) {

                if (response.status !== 200) {
                    alert("Error " + response.status);
                    return;
                }

                alert("Save success");

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
