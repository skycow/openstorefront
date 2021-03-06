/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

'use strict';

app.controller('AdminUserMessageCtrl', ['$scope', 'business', function ($scope, Business) {
  
  $scope.userMessages = [];
  $scope.statusFilterOptions = [
  {code: 'A', desc: 'Active'},
  {code: 'I', desc: 'Archived'}
  ];
  $scope.queryFilter = angular.copy(utils.queryFilter);
  $scope.queryFilter.status = $scope.statusFilterOptions[0].code;
  $scope.predicate = [];
  $scope.reverse = [];  

  $scope.pagination = {};
  $scope.pagination.control;
  $scope.pagination.features = {'dates': true, 'max': true};  
  
  $scope.setPredicate = function (predicate, table) {
    if ($scope.predicate[table] === predicate) {
      $scope.reverse[table] = !$scope.reverse[table];
    } else {
      $scope.predicate[table] = predicate;
      $scope.reverse[table] = false;
    }
    if (table === 'userM') {
      $scope.pagination.control.changeSortOrder(predicate);
    }
  }; 
  
  $scope.showMessage = function(message){
     if (message.details) {
       message.details = !message.details;
     } else {
       message.details = true;
     }     
  };
  
  $scope.deleteUserMessage = function(message){     
    var response = window.confirm("Are you sure you want to delete this message for " + (message.username ? message.username : message.emailAddress) + " ?");
    if (message.userMessageId && response){
      Business.userservice.removeUserMessages(message.userMessageId).then(function(results){
        $scope.refreshData();          
      });
    }
  };    
  
  $scope.refreshData = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader');
    if ($scope.pagination.control && $scope.pagination.control.refresh) {
      $scope.pagination.control.refresh().then(function(){
        $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
      });
    } else {
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
    }     
  };
  
  $scope.processUserMessageNow = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader'); 
    Business.userservice.processUserMessagesNow().then(function() {
      $scope.refreshData();
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
      triggerAlert('Processing User Messages', 'processUserMessages', '', 5000, true);
    });                
  };
  
  $scope.cleanoldUserMessagesNow = function() {  
    $scope.$emit('$TRIGGERLOAD', 'messageLoader'); 
    Business.userservice.cleanoldUserMessagesNow().then(function() {
      $scope.refreshData();
      $scope.$emit('$TRIGGERUNLOAD', 'messageLoader');
      triggerAlert('Cleaned old User Messages', 'cleanedUserMessages', '', 5000, true);
    });                
  };
  
}]);
